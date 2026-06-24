package cl.duoc.dsy1103.libros_microservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "LibroRequest", description = "Modelo DTO de entrada para el registro e incorporación de un nuevo libro")
public class LibroRequest {

    @NotBlank(message = "El ISBN es obligatorio.")
    @Size(max = 20, message = "El ISBN debe tener como maximo 20 caracteres.")
    @Schema(
            description = "Código Internacional Normalizado para Libros (ISBN) único del ejemplar",
            example = "978-84-450-7372-8",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 20
    )
    private String isbn;

    @NotBlank(message = "El titulo es obligatorio.")
    @Size(max = 100, message = "El titulo debe tener como maximo 100 caracteres.")
    @Schema(
            description = "Título formal u oficial de la obra literaria",
            example = "El Señor de los Anillos: La Comunidad del Anillo",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100
    )
    private String titulo;

    @Schema(
            description = "Breve resumen, sinopsis o argumento comercial de la trama del libro",
            example = "En la adormecida e idílica Comarca, un joven hobbit recibe un encargo custodiado por siglos: custodiar el Anillo Único y emprender el viaje para destruirlo."
    )
    private String sinopsis;

    @NotNull(message = "El numero de paginas es obligatorio.")
    @Schema(
            description = "Cantidad total de páginas físicas registradas en la edición del volumen",
            example = "423",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Short numeroPaginas;

    @NotNull(message = "La disponibilidad es obligatoria.")
    @Schema(
            description = "Indicador lógico que define si el libro cuenta con existencias inmediatas en estantería para ser prestado",
            example = "true",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Boolean disponible;

    @NotNull(message = "El ID de autor es obligatorio.")
    @Schema(
            description = "Identificador único relacional del autor de la obra.",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long idAutor;

    @NotNull(message = "El ID de categoria es obligatorio.")
    @Schema(
            description = "Identificador único relacional de la categoría del catálogo.",
            example = "2",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long idCategoria;

    @NotNull(message = "El ID de genero es obligatorio.")
    @Schema(
            description = "Identificador único relacional del género literario específico.",
            example = "3",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long idGenero;
}