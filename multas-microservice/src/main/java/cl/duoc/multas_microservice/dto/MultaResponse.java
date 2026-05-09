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
public class MultaResponse {
    private Long id;
    private Integer monto;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaLimitePago;
    private Long idUsuario;
    private Long idPrestamo;
}
