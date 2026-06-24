package cl.duoc.dsy1103.autores_microservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AutorResponse", description = "Modelo DTO que representa la información técnica, biográfica y de auditoría completa de salida de un autor literario")
public class AutorResponse {

    @Schema(description = "Identificador único auto-incremental del autor en la base de datos", example = "5")
    private Long id;

    @Schema(description = "Nombre y apellido completo del autor literario", example = "George R.R. Martin")
    private String nombreCompleto;

    @Schema(description = "Resumen biográfico oficial, trayectoria o hitos destacados de la carrera del autor", example = "Escritor y guionista estadounidense de literatura fantástica, ciencia ficción y terror, mundialmente conocido por ser el autor de la épica saga Canción de Hielo y Fuego.")
    private String biografia;

    @Schema(description = "Nacionalidad o país de origen del autor", example = "Estadounidense")
    private String nacionalidad;

    @Schema(description = "Fecha de nacimiento cronológica del autor", example = "1948-09-20")
    private LocalDate fechaNacimiento;

    @Schema(description = "Fecha y hora exacta en la que se incorporó y persistió el registro del autor en el sistema", example = "2026-05-16T01:17:44")
    private LocalDateTime fechaRegistro;
}