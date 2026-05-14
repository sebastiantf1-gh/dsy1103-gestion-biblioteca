package cl.duoc.dsy1103.categorias_microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroResponse {
    private Long id;
    private String titulo;
    private boolean disponible;
}
