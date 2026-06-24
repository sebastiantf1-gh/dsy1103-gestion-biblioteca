package cl.duoc.dsy1103.prestamos_microservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "PrestamoRequest", description = "Modelo DTO de entrada para la solicitud y registro transaccional de un nuevo préstamo")
public class PrestamoRequest {

    @Future(message = "La fecha de entrega debe ser posterior al dia de hoy")
    @Schema(
            description = "Fecha y hora límite pactada para la devolución física del libro en la biblioteca. Debe ser estrictamente una fecha futura.",
            example = "2026-07-15T18:30:00"
    )
    private LocalDateTime fechaDevolucion;

    @NotNull(message = "El id del usuario obligatorio")
    @Schema(
            description = "Identificador único relacional del usuario que solicita el préstamo. Su existencia activa es validada dinámicamente mediante WebClient.",
            example = "10",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long idUsuario;

    @NotNull(message = "El id del libro es obligatorio")
    @Schema(
            description = "Identificador único relacional del libro que se desea retirar. Su stock y disponibilidad se comprueban en tiempo real mediante WebClient.",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long idLibro;

    @NotBlank(message = "El estado es obligatorio.")
    @Pattern(regexp = "activo|devuelto", message = "El estado debe ser 'activo' o 'devuelto'")
    @Schema(
            description = "Estado operativo inicial con el que se registra la transacción.",
            example = "activo",
            allowableValues = {"activo", "devuelto"},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String estado;
}