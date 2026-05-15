package cl.duoc.dsy1103.resennas_microservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResennaUpdateRequest {

    @Size(max = 255, message = "La descripcion no puede superar los 255 caracteres")
    private String descripcion;


    @Min(value = 1, message = "La calificacion minima es  1")
    @Max(value = 5, message = "La calificacion maxima es de 5")
    private Integer calificacion;

}
