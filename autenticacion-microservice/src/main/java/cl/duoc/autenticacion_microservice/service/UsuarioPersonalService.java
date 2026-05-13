package cl.duoc.autenticacion_microservice.service;


import cl.duoc.autenticacion_microservice.dto.UsuarioPersonalResponse;
import cl.duoc.autenticacion_microservice.mapper.UsuarioPersonalMapper;
import cl.duoc.autenticacion_microservice.model.UsuarioPersonal;
import cl.duoc.autenticacion_microservice.repository.UsuarioPersonalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UsuarioPersonalService {
    @Autowired
    private UsuarioPersonalRepository usuarioRepository;

    @Autowired
    private UsuarioPersonalMapper usuarioMapper;

    public List<UsuarioPersonalResponse> obtenerUsuarios() {
        List<UsuarioPersonal> usuarios = usuarioRepository.findAll();
        return usuarios.stream().map(usuarioMapper::toResponse).toList();
    }

    public UsuarioPersonalResponse obtenerUsuarioPorId(Long id) {
        UsuarioPersonal usuario = usuarioRepository.findById(id).get();
        return usuarioMapper.toResponse(usuario);
    }

    public UsuarioPersonalResponse obtenerUsuarioPorNombre(String nombre) {
        UsuarioPersonal usuario = usuarioRepository.findByNombreUsuario(nombre).get();
        return usuarioMapper.toResponse(usuario);
    }

    public UsuarioPersonalResponse crear(UsuarioPersonal usuario) {
        if (usuarioRepository.existsByNombreUsuario(usuario.getNombreUsuario())) {
            throw new IllegalArgumentException("Usuario ya existe");
        }
        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuario);
    }
}
