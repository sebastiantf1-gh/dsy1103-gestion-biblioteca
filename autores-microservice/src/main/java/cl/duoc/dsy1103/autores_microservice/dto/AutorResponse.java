package cl.duoc.dsy1103.autores_microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutorResponse {
    private Long id;
    private String nombreCompleto;
    private String biografia;
    private String nacionalidad;
    private LocalDate fechaNacimiento;
    private LocalDateTime fechaRegistro;
    private List<LibroResponse> libros;
}
