package cl.duoc.multas_microservice.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MultaRequest {
    @FutureOrPresent(message = "La fecha limite no puede ser pasada.")
    private LocalDateTime fechaLimitePago;

    @NotNull(message = "El monto es obligatorio.")
    @Min(value = 1, message = "El monto debe ser mayor a 0")
    private Integer monto;

    @NotNull(message = "El id del usuario es obligatorio.")
    @Positive(message = "El id debe ser un numero positivo.")
    private Long idUsuario;

    @NotNull(message = "El id del prestamo es obligatorio.")
    @Positive(message = "El id debe ser un numero positivo.")
    private Long idPrestamo;

}
