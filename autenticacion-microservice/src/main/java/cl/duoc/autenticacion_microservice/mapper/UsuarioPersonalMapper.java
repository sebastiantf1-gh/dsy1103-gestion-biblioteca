package cl.duoc.autenticacion_microservice.mapper;

import cl.duoc.autenticacion_microservice.dto.CrearUsuarioRequest;
import cl.duoc.autenticacion_microservice.dto.UsuarioPersonalResponse;
import cl.duoc.autenticacion_microservice.model.UsuarioPersonal;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UsuarioPersonalMapper {

    //Convierte el Request de registro a la entidad UsuarioPersonal.

    public UsuarioPersonal toEntity(CrearUsuarioRequest request) {
        return UsuarioPersonal.builder()
                .nombreUsuario(request.getNombreUsuario())
                .password(request.getPassword())
                .email(request.getEmail())
                .numeroTelefono(request.getNumeroTelefono())
                .fechaRegistro(LocalDateTime.now())
                .build();
    }

    //Convierte la entidad UsuarioPersonal a tu nuevo UsuarioPersonalResponse.

    public UsuarioPersonalResponse toResponse(UsuarioPersonal entity) {

        return UsuarioPersonalResponse.builder()
                .idPersonalBiblioteca(entity.getIdPersonalBiblioteca())
                .nombreUsuario(entity.getNombreUsuario())
                .email(entity.getEmail())
                .numeroTelefono(entity.getNumeroTelefono())
                .fechaRegistro(entity.getFechaRegistro())
                .build();
    }
}
