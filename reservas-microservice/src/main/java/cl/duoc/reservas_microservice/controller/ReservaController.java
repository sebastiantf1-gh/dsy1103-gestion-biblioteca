package cl.duoc.reservas_microservice.controller;

import cl.duoc.reservas_microservice.dto.ApiErrorResponse;
import cl.duoc.reservas_microservice.dto.CrearReservaRequest;
import cl.duoc.reservas_microservice.dto.ReservaResponse;
import cl.duoc.reservas_microservice.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/reservas")
@Slf4j
@Tag(name = "Gestión de Reservas", description = "Endpoints para la administración del ciclo de vida de las reservas de libros físicamente habilitados")
@SecurityRequirement(name = "bearerAuth") // 🔐 Candado de seguridad para Swagger
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Operation(
            summary = "Crear una nueva reserva",
            description = "Registra una reserva temporal de un libro. Verifica de forma síncrona mediante WebClient que el usuario exista, el libro esté disponible y no existan solapamientos de fechas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación o conflicto de reglas de negocio (libro no disponible, fechas cruzadas)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token ausente, inválido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "No encontrado - El ID del usuario o del libro proporcionado no existen de forma remota",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ReservaResponse> crearReserva(
            @Valid @RequestBody CrearReservaRequest request,
            @RequestHeader("Authorization") String token) {
        log.info("POST /v1/reservas - Iniciando creación de reserva");
        ReservaResponse reservaResponse = reservaService.crearReserva(request, token);
        return new ResponseEntity<>(reservaResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todas las reservas", description = "Recupera un listado histórico global con todas las reservas registradas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial recuperado exitosamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReservaResponse.class)))),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping
    public List<ReservaResponse> obtenerTodas() {
        log.info("GET /v1/reservas - Buscando historial global");
        return reservaService.obtenerTodasLasReservas();
    }

    @Operation(summary = "Listar reservas por ID de Usuario", description = "Busca y retorna todas las reservas que pertenecen a un usuario específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reservas del usuario encontrada",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReservaResponse.class)))),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/usuario/{idUsuario}")
    public List<ReservaResponse> obtenerPorUsuario(@PathVariable Long idUsuario) {
        log.info("GET /v1/reservas/usuario/{} - Buscando reservas del usuario", idUsuario);
        return reservaService.obtenerReservasPorUsuarioId(idUsuario);
    }

    @Operation(summary = "Eliminar o cancelar una reserva por ID", description = "Remueve físicamente el registro de una reserva mediante su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reserva eliminada con éxito (No Content)"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "No encontrado - El ID de la reserva no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Long id) {
        log.info("DELETE /v1/reservas/{} - Solicitud de eliminación", id);
        reservaService.eliminarReserva(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Eliminar todas las reservas de un usuario", description = "Vacía el historial completo de reservas asociadas a un identificador de usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Historial del usuario vaciado con éxito"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @DeleteMapping("/usuario/{idUsuario}")
    public ResponseEntity<Void> eliminarPorUsuario(@PathVariable Long idUsuario) {
        log.info("DELETE /v1/reservas/usuario/{} - Solicitud de vaciado", idUsuario);
        reservaService.eliminarReservasPorUsuarioId(idUsuario);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}