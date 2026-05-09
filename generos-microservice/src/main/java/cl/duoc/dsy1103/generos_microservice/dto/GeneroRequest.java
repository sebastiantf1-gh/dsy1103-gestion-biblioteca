package cl.duoc.dsy1103.generos_microservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneroRequest {

    @NotBlank(message = "El nombre del genero no puede estar en blanco")
    @Size(max = 100,message = "el nombre tiene un limite de 100 caracteres")
    private String nombreGenero;

    @NotBlank(message = "la descripcion no puede estar vacía")
    @Size(max = 200,message = "la descripcion tiene un limite de 200 caracteres")
    private String descripcion;
}
