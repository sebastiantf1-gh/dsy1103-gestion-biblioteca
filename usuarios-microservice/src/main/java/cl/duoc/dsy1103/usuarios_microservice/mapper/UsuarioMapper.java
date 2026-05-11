package cl.duoc.dsy1103.usuarios_microservice.mapper;

import cl.duoc.dsy1103.usuarios_microservice.dto.UsuarioRequest;
import cl.duoc.dsy1103.usuarios_microservice.dto.UsuarioResponse;
import cl.duoc.dsy1103.usuarios_microservice.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario fromRequest(UsuarioRequest request){
        return Usuario.builder()
                .nombreCompleto(request.getNombreCompleto())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .build();
    }

    public UsuarioResponse toResponse(Usuario usuario){
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nombreCompleto(usuario.getNombreCompleto())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .build();

    }
}
