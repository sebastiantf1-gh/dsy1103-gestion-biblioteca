package cl.duoc.dsy1103.libros_microservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "LibroResponse", description = "Modelo DTO que representa la información técnica y relacional completa de salida de un libro")
public class LibroResponse {

    @Schema(description = "Identificador único auto-incremental del libro en la base de datos", example = "1")
    private Long id;

    @Schema(description = "Código Internacional Normalizado para Libros (ISBN) del ejemplar", example = "978-84-450-7372-8")
    private String isbn;

    @Schema(description = "Título formal u oficial de la obra literaria", example = "El Señor de los Anillos: La Comunidad del Anillo")
    private String titulo;

    @Schema(description = "Breve resumen, sinopsis o argumento comercial de la trama del libro", example = "En la adormecida e idílica Comarca, un joven hobbit recibe un encargo custodiado por siglos: custodiar el Anillo Único y emprender el viaje para destruirlo.")
    private String sinopsis;

    @Schema(description = "Cantidad total de páginas físicas registradas en la edición del volumen", example = "423")
    private Short numeroPaginas;

    @Schema(description = "Indicador lógico que define si el libro cuenta con existencias en estantería para préstamo inmediato", example = "true")
    private Boolean disponible;

    @Schema(description = "Fecha y hora exacta en la que se incorporó el ejemplar al catálogo general del sistema", example = "2026-06-24T10:15:30")
    private LocalDateTime fechaRegistro;

    @Schema(description = "Estructura de datos anidada con la ficha del Autor. Información resuelta dinámicamente mediante WebClient perimetral.")
    private AutorResponse autor;

    @Schema(description = "Estructura de datos anidada con el detalle de la Categoría. Información resuelta dinámicamente mediante WebClient perimetral.")
    private CategoriaResponse categoria;

    @Schema(description = "Estructura de datos anidada con el detalle del Género Literario. Información resuelta dinámicamente mediante WebClient perimetral.")
    private GeneroResponse genero;
}