package cl.duoc.reservas_microservice.dto;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResponse {
    private Long id;
    private LocalDate fechaInicio;
    private LocalDate fechaTermino;
    private Long idLibro;
    private Long idUsuario;
    private String estado;
}
