package cl.duoc.dsy1103.prestamos_microservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UsuarioResponse", description = "Modelo DTO simplificado que representa la información básica de un usuario dentro del contexto transaccional de préstamos")
public class UsuarioResponse {

    @Schema(description = "Identificador único original del usuario, administrado y validado por el microservicio de usuarios", example = "10")
    private Long id;

    @Schema(description = "Nombre y apellido completo del usuario o lector que registra el préstamo", example = "Juan Carlos Pérez")
    private String nombreCompleto;
}