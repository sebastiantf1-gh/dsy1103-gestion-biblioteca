package cl.duoc.dsy1103.categorias_microservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "CategoriaUpdateRequest", description = "Modelo DTO de entrada para la modificación parcial o total de una categoría existente")
public class CategoriaUpdateRequest {

    @Size(max = 90, message = "El nombre debe tener como maximo 90 caracteres.")
    @Schema(
            description = "Nuevo nombre opcional para la categoría literaria. Si se envía como nulo o se omite, se mantendrá el valor actual en la base de datos.",
            example = "Novela Histórica y Biográfica",
            maxLength = 90
    )
    private String nombre;

    @Size(max = 250, message = "La descripcion debe tener como maximo 250 caracteres.")
    @Schema(
            description = "Nueva descripción opcional sobre las temáticas que abarca la categoría.",
            example = "Obras narrativas de ficción y relatos biográficos basados en minuciosas reconstrucciones de hechos reales pasados.",
            maxLength = 250
    )
    private String descripcion;
}