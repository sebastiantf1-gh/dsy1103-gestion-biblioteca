package cl.duoc.dsy1103.prestamos_microservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LibroResponse", description = "Modelo DTO simplificado que representa la información básica de un libro dentro del contexto transaccional de préstamos")
public class LibroResponse {

    @Schema(description = "Identificador único original del libro, administrado y validado por el microservicio de catálogo (Libros)", example = "1")
    private Long id;

    @Schema(description = "Título formal u oficial de la obra literaria prestada", example = "El Señor de los Anillos: La Comunidad del Anillo")
    private String titulo;
}