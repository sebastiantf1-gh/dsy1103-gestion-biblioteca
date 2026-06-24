package cl.duoc.dsy1103.libros_microservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "LibroUpdateRequest", description = "Modelo DTO de entrada para la modificación parcial o total de la ficha técnica de un libro existente")
public class LibroUpdateRequest {

    @Schema(
            description = "Nuevo título opcional para la obra literaria. Si se envía como nulo o se omite, se mantendrá el valor actual en la base de datos.",
            example = "El Señor de los Anillos: Las Dos Torres"
    )
    private String titulo;

    @Schema(
            description = "Nueva sinopsis o resumen argumental opcional de la trama de la obra.",
            example = "La Comunidad se ha disuelto, pero la búsqueda para destruir el Anillo Único continúa mientras Frodo y Sam arriesgan sus vidas guiados por la criatura Gollum."
    )
    private String sinopsis;

    @Schema(
            description = "Nueva cantidad opcional de páginas físicas asociadas a la edición del volumen.",
            example = "352"
    )
    private Short numeroPaginas;

    @Schema(
            description = "Nuevo estado lógico opcional de disponibilidad en inventario (útil para auditorías manuales de stock fuera de los flujos de préstamo).",
            example = "false"
    )
    private Boolean disponible;

    @Schema(
            description = "Nuevo ID de autor relacional opcional si se requiere corregir la vinculación de la obra. Su existencia base se valida mediante WebClient.",
            example = "1"
    )
    private Long idAutor;

    @Schema(
            description = "Nuevo ID de categoría relacional opcional para reclasificar la obra en el catálogo. Su existencia base se valida mediante WebClient.",
            example = "2"
    )
    private Long idCategoria;

    @Schema(
            description = "Nuevo ID de género relacional opcional para la catalogación avanzada de la obra. Su existencia base se valida mediante WebClient.",
            example = "3"
    )
    private Long idGenero;
}