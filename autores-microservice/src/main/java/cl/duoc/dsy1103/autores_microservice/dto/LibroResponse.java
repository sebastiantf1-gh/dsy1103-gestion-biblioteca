package cl.duoc.dsy1103.autores_microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroResponse {
    private Long id;
    private String titulo;
    private boolean disponible;
}
