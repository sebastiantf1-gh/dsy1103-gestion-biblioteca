package cl.duoc.dsy1103.autores_microservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Entidad que maneja la estructura de la informacion que debe enviar el Cliente para modificar un recurso.")
public class AutorUpdateRequest {
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres.")
    //Se omiten intencionalmente las anotaciones @NotBlank o @NotNull. Esto permite que el cliente actualice solo
    // ciertos campos (por ejemplo, solo la biografía) sin verse obligado a reenviar el nombre completo u otros campos.
    @Schema(description = "Nombre completo del autor", example = "George R.R. Martin")
    private String nombreCompleto;

    @Schema(description = "Breve biografia del autor", example = "Escritor de la épica saga Canción de Hielo y Fuego." )
    private String biografia;

    @Size(max = 50, message = "La nacionalidad debe tener como máximo 50 caracteres.")
    @Schema(description = "Nacionalidad del autor", example = "Estadounidense")
    private String nacionalidad;

    @Past(message = "La fecha de nacimiento debe estar en tiempo pasado.")
    @Schema(description = "Fecha de nacimiento del autor", example = "1948-09-20")
    private LocalDate fechaNacimiento;
}
