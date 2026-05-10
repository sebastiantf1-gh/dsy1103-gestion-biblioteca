package cl.duoc.dsy1103.resennas_microservice.dto;



import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResennaRequest {

    @Size(max = 255, message = "La descripcion no puede superar los 255 caracteres")
    private String descripcion;

    @NotNull(message = "La calificacion es obligatoria")
    @Min(value = 1, message = "La calificacion minima es  1")
    @Max(value = 5, message = "La calificacion maxima es de 5")
    private Integer calificacion;

    @NotNull(message = "El id del usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "El id del libro es obligatorio")
    private Long idLibro;
}
