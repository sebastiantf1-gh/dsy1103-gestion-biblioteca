package cl.duoc.autenticacion_microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AutenticacionResponse {
    private String token;
    private String nombreUsuario;
    private Long expiresIn;
}
