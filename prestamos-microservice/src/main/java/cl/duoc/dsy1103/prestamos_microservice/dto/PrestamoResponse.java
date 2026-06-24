package cl.duoc.dsy1103.prestamos_microservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "PrestamoResponse", description = "Modelo DTO que representa la información completa de salida de una transacción de préstamo")
public class PrestamoResponse {

    @Schema(description = "Identificador único auto-incremental del registro de préstamo en la base de datos", example = "1")
    private Long id;

    @Schema(description = "Fecha y hora exacta en la que se efectuó el retiro y registro del libro", example = "2026-06-24T10:00:00")
    private LocalDateTime fechaPrestamo;

    @Schema(description = "Fecha y hora límite establecida para la devolución del ejemplar en la biblioteca", example = "2026-07-15T18:30:00")
    private LocalDateTime fechaDevolucion;

    @Schema(description = "Estructura de datos anidada con la información del lector. Objeto resuelto dinámicamente consultando al microservicio de usuarios mediante WebClient.")
    private UsuarioResponse usuario;

    @Schema(description = "Estructura de datos anidada con la ficha técnica básica del libro. Objeto resuelto dinámicamente consultando al catálogo mediante WebClient.")
    private LibroResponse libro;

    @Schema(description = "Estado operativo actual de la transacción en el sistema", example = "activo", allowableValues = {"activo", "devuelto"})
    private String estado;
}