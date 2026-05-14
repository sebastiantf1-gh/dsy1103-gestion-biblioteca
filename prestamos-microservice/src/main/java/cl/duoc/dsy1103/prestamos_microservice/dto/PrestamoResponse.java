package cl.duoc.dsy1103.prestamos_microservice.dto;

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
    private Long idUsuario;
    private Long idLibro;
}
