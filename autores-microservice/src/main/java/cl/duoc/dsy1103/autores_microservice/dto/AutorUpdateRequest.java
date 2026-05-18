package cl.duoc.dsy1103.autores_microservice.dto;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AutorUpdateRequest {
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres.")
    //Se omiten intencionalmente las anotaciones @NotBlank o @NotNull. Esto permite que el cliente actualice solo
    // ciertos campos (por ejemplo, solo la biografía) sin verse obligado a reenviar el nombre completo u otros campos.
    private String nombreCompleto;

    private String biografia;

    @Size(max = 50, message = "La nacionalidad debe tener como máximo 50 caracteres.")
    private String nacionalidad;

    @Past(message = "La fecha de nacimiento debe estar en tiempo pasado.")
    private LocalDate fechaNacimiento;
}
