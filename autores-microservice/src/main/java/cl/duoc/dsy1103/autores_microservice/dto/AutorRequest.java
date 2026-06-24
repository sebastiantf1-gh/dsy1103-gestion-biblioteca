package cl.duoc.dsy1103.autores_microservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AutorRequest", description = "Modelo DTO de entrada para la creación y registro formal de un nuevo autor literario")
public class AutorRequest {

    @NotBlank(message = "El nombre completo es obligatorio.")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres.")
    // @NotBlank asegura que el campo no sea nulo y que no contenga solo espacios en blanco.
    // @Size restringe el tamaño a nivel de aplicación antes de intentar enviar la query a la BD, protegiendo la integridad.
    @Schema(
            description = "Nombre y apellido completo del autor literario a registrar",
            example = "George R.R. Martin",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 2,
            maxLength = 100
    )
    private String nombreCompleto;

    @Schema(
            description = "Breve resumen biográfico, trayectoria o hitos del autor",
            example = "Escritor y guionista de literatura fantástica, ciencia ficción y terror, mundialmente conocido por ser el autor de la épica saga Canción de Hielo y Fuego."
    )
    private String biografia;

    @NotBlank(message = "La nacionalidad es obligatoria.")
    @Size(max = 50, message = "La nacionalidad debe tener como máximo 50 caracteres.")
    // 'message' define un mensaje explícito personalizado. Si la validación falla en el Controller,
    @Schema(
            description = "Nacionalidad o país de origen del autor",
            example = "Estadounidense",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 50
    )
    private String nacionalidad;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    @Past(message = "La fecha de nacimiento debe estar en tiempo pasado.")
    // @NotNull valida que tipos de objeto no-string (como LocalDate) no sean nulos.
    // @Past es una regla que impide que un cliente registre por error una fecha de nacimiento futura.
    @Schema(
            description = "Fecha de nacimiento cronológica del autor. Debe situarse estrictamente en tiempo pasado.",
            example = "1948-09-20",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate fechaNacimiento;
}