package cl.duoc.dsy1103.categorias_microservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "CategoriaResponse", description = "Modelo DTO que representa la información de salida de una categoría literaria")
public class CategoriaResponse {

    @Schema(description = "Identificador único auto-incremental de la categoría en la base de datos", example = "1")
    private Long id;

    @Schema(description = "Nombre formal del género o categoría literaria", example = "Ciencia Ficción")
    private String nombre;

    @Schema(description = "Breve descripción sobre el tipo de obras o temáticas que abarca esta categoría", example = "Libros basados en desarrollos científicos especulativos, exploración espacial, futuros alternativos y tecnología avanzada.")
    private String descripcion;
}
