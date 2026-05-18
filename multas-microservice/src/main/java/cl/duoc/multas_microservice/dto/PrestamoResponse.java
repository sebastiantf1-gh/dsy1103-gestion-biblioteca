package cl.duoc.multas_microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrestamoResponse {
    private Long id;
    private LocalDateTime fechaPrestamo;
    private LocalDateTime fechaDevolucion;
    private UsuarioResponse usuario;
    private String estado;
}
