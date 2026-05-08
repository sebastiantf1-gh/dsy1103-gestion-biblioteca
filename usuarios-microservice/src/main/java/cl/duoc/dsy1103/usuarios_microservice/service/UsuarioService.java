package cl.duoc.dsy1103.usuarios_microservice.service;

import cl.duoc.dsy1103.usuarios_microservice.dto.UsuarioResponse;
import cl.duoc.dsy1103.usuarios_microservice.mapper.UsuarioMapper;
import cl.duoc.dsy1103.usuarios_microservice.model.Usuario;
import cl.duoc.dsy1103.usuarios_microservice.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
