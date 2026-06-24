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
@Schema(name = "AutorUpdateRequest", description = "Modelo DTO de entrada para la modificación parcial o total de los datos de un autor existente")
public class AutorUpdateRequest {

    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres.")
    //Se omiten intencionalmente las anotaciones @NotBlank o @NotNull. Esto permite que el cliente actualice solo
    // ciertos campos (por ejemplo, solo la biografía) sin verse obligado a reenviar el nombre completo u otros campos.
    @Schema(
            description = "Nuevo nombre y apellido completo del autor. Si se envía como nulo o se omite, el registro conservará su valor actual en la persistencia.",
            example = "George Raymond Richard Martin",
            minLength = 2,
            maxLength = 100
    )
    private String nombreCompleto;

    @Schema(
            description = "Nueva biografía, trayectoria o modificaciones en el resumen de hitos del autor.",
            example = "Escritor y guionista estadounidense de literatura fantástica, ciencia ficción y terror. Famoso por su obra maestra Canción de Hielo y Fuego."
    )
    private String biografia;

    @Size(max = 50, message = "La nacionalidad debe tener como máximo 50 caracteres.")
    @Schema(
            description = "Nueva nacionalidad o país de origen corregido para el autor.",
            example = "Estadounidense (Norteamericano)",
            maxLength = 50
    )
    private String nacionalidad;

    @Past(message = "La fecha de nacimiento debe estar en tiempo pasado.")
    @Schema(
            description = "Fecha de nacimiento cronológica del autor en caso de requerir enmiendas. Debe situarse estrictamente en tiempo pasado.",
            example = "1948-09-20"
    )
    private LocalDate fechaNacimiento;
}