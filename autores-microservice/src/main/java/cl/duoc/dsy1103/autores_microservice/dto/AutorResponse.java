package cl.duoc.dsy1103.autores_microservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Entidad que maneja la estructura de la informacion que se proyecta al Cliente.")
public class AutorResponse {
    @Schema(description = "ID del autor", example = "5")
    private Long id;
    @Schema(description = "Nombre completo del autor", example = "George R.R. Martin")
    private String nombreCompleto;
    @Schema(description = "Breve biografia del autor", example = "Escritor de la épica saga Canción de Hielo y Fuego." )
    private String biografia;
    @Schema(description = "Nacionalidad del autor", example = "Estadounidense")
    private String nacionalidad;
    @Schema(description = "Fecha de nacimiento del autor", example = "1948-09-20")
    private LocalDate fechaNacimiento;
    @Schema(description = "Fecha de registro del autor en el sistema", example = "2026-05-16 01:17:44")
    private LocalDateTime fechaRegistro;
}
