package cl.duoc.dsy1103.libros_microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutorResponse {
    private Long id;
    private String nombreCompleto;
}
