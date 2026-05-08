package cl.duoc.dsy1103.autores_microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutorResponse {
    private Long id;
    private String nombreCompleto;
    private String biografia;
    private String nacionalidad;
    private LocalDateTime fechaNacimiento;
    private LocalDateTime fechaRegistro;
}
