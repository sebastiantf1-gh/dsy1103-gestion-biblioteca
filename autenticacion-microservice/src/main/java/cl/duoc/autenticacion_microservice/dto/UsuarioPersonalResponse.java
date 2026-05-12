package cl.duoc.autenticacion_microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioPersonalResponse {
    private Long idPersonalBiblioteca;
    private String nombreUsuario;
    private String email;
    private String numeroTelefono;
    private LocalDateTime fechaRegistro;
}
