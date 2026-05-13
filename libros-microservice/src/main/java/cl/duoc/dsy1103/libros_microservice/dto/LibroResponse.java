package cl.duoc.dsy1103.libros_microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibroResponse {
    private Long id;
    private String isbn;
    private String titulo;
    private String sinopsis;
    private Short numeroPaginas;
    private Boolean disponible;
    private LocalDateTime fechaRegistro;
    private AutorResponse autor;
    private Long idCategoria;
    private Long idGenero;
}
