package cl.duoc.dsy1103.generos_microservice.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneroResponse {
    private Long id;
    private String nombreGenero;
    private String descripcion;
}
