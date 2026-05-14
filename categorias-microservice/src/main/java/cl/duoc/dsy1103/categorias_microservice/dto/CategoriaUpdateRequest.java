package cl.duoc.dsy1103.categorias_microservice.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriaUpdateRequest {
    @Size(max = 90, message = "El nombre debe tener como maximo 90 caracteres.")
    private String nombre;

    @Size(max = 250, message = "La descripcion debe tener como maximo 250 caracteres.")
    private String descripcion;

}
