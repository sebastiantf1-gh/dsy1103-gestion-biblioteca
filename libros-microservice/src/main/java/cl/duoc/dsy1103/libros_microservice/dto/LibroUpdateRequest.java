package cl.duoc.dsy1103.libros_microservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibroUpdateRequest {
    private String titulo;
    private String sinopsis;
    private Short numeroPaginas;
    private Boolean disponible;
    private Long idAutor;
    private Long idCategoria;
    private Long idGenero;
}
