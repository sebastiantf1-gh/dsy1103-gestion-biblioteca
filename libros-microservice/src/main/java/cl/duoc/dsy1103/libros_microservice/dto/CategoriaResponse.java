package cl.duoc.dsy1103.libros_microservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CategoriaResponse", description = "Modelo DTO simplificado que representa la información agregada de una categoría dentro del catálogo de libros")
public class CategoriaResponse {

    @Schema(description = "Identificador único original de la categoría, administrado y validado por el microservicio de categorías", example = "2")
    private Long id;

    @Schema(description = "Nombre formal del género o categoría literaria", example = "Novela Histórica")
    private String nombre;
}