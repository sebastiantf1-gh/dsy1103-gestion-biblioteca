package cl.duoc.dsy1103.prestamos_microservice.dto;

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
public class PrestamoRequest {

    @Future(message = "La fecha de entrega debe ser posterior al dia de hoy")
    private LocalDateTime fechaDevolucion;

    @NotNull(message = "El id del usuario obligatorio")
    private Long idUsuario;

    @NotNull(message = "El id del libro es obligatorio")
    private Long idLibro;

    @NotBlank(message = "El estado es obligatorio.")
    @Pattern(regexp = "activo|devuelto", message = "El estado debe ser 'activo' o 'devuelto'")
    private String estado;
}
