package cl.duoc.dsy1103.libros_microservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "GeneroResponse", description = "Modelo DTO simplificado que representa la información agregada de un género literario dentro del catálogo de libros")
public class GeneroResponse {

    @Schema(description = "Identificador único original del género literario, administrado y validado por el microservicio de géneros", example = "3")
    private Long id;

    @Schema(description = "Nombre formal de la clasificación o género literario", example = "Fantasía Épica")
    private String nombreGenero;
}