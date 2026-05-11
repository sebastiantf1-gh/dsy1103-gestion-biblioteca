package cl.duoc.dsy1103.categorias_microservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaRequest {

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 90, message = "El nombre debe tener como maximo 90 caracteres.")
    private String nombre;

    @NotBlank(message = "La descripcion es obligatoria.")
    @Size(max = 250, message = "La descripcion debe tener como maximo 250 caracteres.")
    private String descripcion;

    @NotNull(message = "El ID de libro es obligatorio.")
    private Long idLibro;
}
