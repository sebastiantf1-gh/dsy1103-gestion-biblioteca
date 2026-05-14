package cl.duoc.dsy1103.prestamos_microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroResponse {
    private Long id;
    private String titulo;

}
