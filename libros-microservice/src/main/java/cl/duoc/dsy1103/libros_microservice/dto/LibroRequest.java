package cl.duoc.dsy1103.libros_microservice.dto;

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
public class LibroRequest {
    @NotBlank(message = "El ISBN es obligatorio.")
    @Size(max = 20, message = "El ISBN debe tener como maximo 20 caracteres.")
    private String isbn;

    @NotBlank(message = "El titulo es obligatorio.")
    @Size(max = 100, message = "El titulo debe tener como maximo 100 caracteres.")
    private String titulo;

    private String sinopsis;

    @NotNull(message = "El numero de paginas es obligatorio.")
    private Short numeroPaginas;

    @NotNull(message = "La disponibilidad es obligatoria.")
    private Boolean disponible;

    @NotNull(message = "El ID de autor es obligatorio.")
    private Long idAutor;

    @NotNull(message = "El ID de categoria es obligatorio.")
    private Long idCategoria;

    @NotNull(message = "El ID de genero es obligatorio.")
    private Long idGenero;

}
