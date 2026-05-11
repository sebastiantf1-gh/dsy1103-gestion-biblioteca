package cl.duoc.dsy1103.usuarios_microservice.service;

import cl.duoc.dsy1103.usuarios_microservice.dto.UsuarioRequest;
import cl.duoc.dsy1103.usuarios_microservice.dto.UsuarioResponse;
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
@Transactional
@Slf4j
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    public List<UsuarioResponse> listarUsuarios(){
        log.info("listando usuarios");
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(usuarioMapper::toResponse)
                .toList();

    }

    public UsuarioResponse buscarUsuarioPorId(Long id){
        log.info("buscando usuario por ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
        return usuarioMapper.toResponse(usuario);
    }
    public UsuarioResponse crearUsuario(UsuarioRequest usuarioRequest){
        log.info("creando nuevo usuario");
        Usuario usuario = usuarioMapper.fromRequest(usuarioRequest);
        usuario.setFechaRegistro(LocalDateTime.now());
        Usuario usuario1 = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuario1);
    }

    public UsuarioResponse modificarUsuario(Long id,UsuarioRequest usuarioRequest){
        log.info("modificando usuario id: {}", id);
        Usuario usuarioAModificar = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("usuario no encontrado :" + id));

        usuarioAModificar.setNombreCompleto(usuarioRequest.getNombreCompleto());
        usuarioAModificar.setEmail(usuarioRequest.getEmail());
        usuarioAModificar.setTelefono(usuarioRequest.getTelefono());

        Usuario usuarioModificado = usuarioRepository.save(usuarioAModificar);
        return usuarioMapper.toResponse(usuarioModificado);
    }

    public void eliminarUsuario(Long id){
        log.info("Eliminando usuario id: {}", id);
        if(!usuarioRepository.existsById(id)){
            throw new NoSuchElementException("Usuario no encontrado " + id);
        }
        usuarioRepository.deleteById(id);
    }






}
