package cl.duoc.reservas_microservice.controller;

import cl.duoc.reservas_microservice.dto.CrearReservaRequest;
import cl.duoc.reservas_microservice.dto.ReservaResponse;
import cl.duoc.reservas_microservice.service.ReservaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para gestionar las operaciones relacionadas con las reservas de libros.
 */
@RestController
@RequestMapping("/v1/reservas")
@Slf4j
public class ReservaController {
    @Autowired
    private ReservaService reservaService;

    /**
     * Endpoint para crear una nueva reserva de libro para un usuario.
     * * @param request La solicitud para crear una nueva reserva (fechas e IDs).
     * @param token El token JWT recibido en la cabecera de la petición.
     * @return La reserva creada con estado HTTP 201 Created.
     */
    @PostMapping
    public ResponseEntity<ReservaResponse> crearReserva(
            @Valid @RequestBody CrearReservaRequest request,
            @RequestHeader("Authorization") String token) {

        log.info("POST /v1/reservas - Iniciando creación de reserva");

        ReservaResponse reservaResponse = reservaService.crearReserva(request, token);

        return new ResponseEntity<>(reservaResponse, HttpStatus.CREATED);
    }
    /**
     * Endpoint para listar todas las reservas del sistema.
     */
    @GetMapping
    public List<ReservaResponse> obtenerTodas() {
        log.info("GET /v1/reservas - Buscando historial global");
        return reservaService.obtenerTodasLasReservas();
    }
    /**
     * Endpoint para obtener las reservas de un usuario específico.
     */
    @GetMapping("/usuario/{idUsuario}")
    public List<ReservaResponse> obtenerPorUsuario(@PathVariable Long idUsuario) {
        log.info("GET /v1/reservas/usuario/{} - Buscando reservas del usuario", idUsuario);
        return reservaService.obtenerReservasPorUsuarioId(idUsuario);
    }

    /**
     * Endpoint para eliminar una reserva específica por su ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Long id) {
        log.info("DELETE /v1/reservas/{} - Solicitud de eliminación", id);
        reservaService.eliminarReserva(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    /**
     * Endpoint para eliminar todas las reservas de un usuario específico.
     */
    @DeleteMapping("/usuario/{idUsuario}")
    public ResponseEntity<Void> eliminarPorUsuario(@PathVariable Long idUsuario) {
        log.info("DELETE /v1/reservas/usuario/{} - Solicitud de vaciado", idUsuario);
        reservaService.eliminarReservasPorUsuarioId(idUsuario);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
