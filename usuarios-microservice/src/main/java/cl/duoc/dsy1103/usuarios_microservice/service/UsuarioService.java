package cl.duoc.dsy1103.usuarios_microservice.service;

import cl.duoc.dsy1103.usuarios_microservice.dto.UsuarioRequest;
import cl.duoc.dsy1103.usuarios_microservice.dto.UsuarioResponse;
import cl.duoc.dsy1103.usuarios_microservice.dto.UsuarioUpdateRequest;
import cl.duoc.dsy1103.usuarios_microservice.mapper.UsuarioMapper;
import cl.duoc.dsy1103.usuarios_microservice.model.Usuario;
import cl.duoc.dsy1103.usuarios_microservice.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional//Si falla algo en la base de datos se hace un rollback
@Slf4j
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    //Trae todos los usuario de la base de datos y los pasa a response
    public List<UsuarioResponse> listarUsuarios(){
        log.info("listando usuarios");
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(usuarioMapper::toResponse)
                .toList();

    }
    //Busca a un usuario por la ID. Si no existe manda un error para ser capturado por el GlobalException
    public UsuarioResponse buscarUsuarioPorId(Long id){
        log.info("buscando usuario por ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
        return usuarioMapper.toResponse(usuario);
    }
    //Crea el usuario usando los datos mandados por el cliente y el mapper los transforma a response
    public UsuarioResponse crearUsuario(UsuarioRequest usuarioRequest){
        log.info("creando nuevo usuario");
        Usuario usuario = usuarioMapper.fromRequest(usuarioRequest);
        //setea la fecha de ingreso automaticamente
        usuario.setFechaRegistro(LocalDateTime.now());
        Usuario usuario1 = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuario1);
    }
    //Modifica los campos que no vengan nulos
    public UsuarioResponse modificarUsuario(Long id, UsuarioUpdateRequest request){
        log.info("modificando usuario id: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("usuario no encontrado :" + id));

        //validaciones por si el campo viene vacio
        if(request.getNombreCompleto()!=null){
            usuario.setNombreCompleto(request.getNombreCompleto());
        }
        if(request.getTelefono()!=null){
            usuario.setTelefono(request.getTelefono());
        }
        if(request.getEmail()!=null){
            usuario.setEmail(request.getEmail());
        }

        Usuario usuarioModificado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuarioModificado);
    }

    //Verifica la existencia del id antes de borrar, si no existe tira un error
    public void eliminarUsuario(Long id){
        log.info("Eliminando usuario id: {}", id);
        if(!usuarioRepository.existsById(id)){
            throw new NoSuchElementException("Usuario no encontrado " + id);
        }
        usuarioRepository.deleteById(id);
    }






}
