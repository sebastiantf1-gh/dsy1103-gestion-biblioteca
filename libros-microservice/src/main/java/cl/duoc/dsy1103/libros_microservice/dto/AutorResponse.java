package cl.duoc.dsy1103.libros_microservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AutorResponse", description = "Modelo DTO simplificado que representa la información agregada de un autor dentro del catálogo de libros")
public class AutorResponse {

    @Schema(description = "Identificador único original del autor, administrado por el microservicio de autores", example = "1")
    private Long id;

    @Schema(description = "Nombre y apellido completo del autor literario", example = "Jack Marston")
    private String nombreCompleto;
}