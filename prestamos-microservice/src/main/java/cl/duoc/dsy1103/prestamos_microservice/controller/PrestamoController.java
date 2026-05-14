package cl.duoc.dsy1103.prestamos_microservice.controller;

import cl.duoc.dsy1103.prestamos_microservice.dto.PrestamoRequest;
import cl.duoc.dsy1103.prestamos_microservice.dto.PrestamoResponse;
import cl.duoc.dsy1103.prestamos_microservice.service.PrestamoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/prestamos")
@Slf4j
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    //listar todos
    @GetMapping
    public ResponseEntity<List<PrestamoResponse>> buscarPrestamos() {
        log.info("GET /prestamos");
        return ResponseEntity.ok(prestamoService.listarTodosLosPrestamos());
    }

    //buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponse> obtenerPrestamoPorId(@PathVariable Long id) {
        log.info("GET /prestamos/{}", id);
        return ResponseEntity.ok(prestamoService.obtenerPrestamoPorId(id));
    }
    //historial de préstamos por lsuario
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<PrestamoResponse>> historialPorUsuario(@PathVariable Long idUsuario) {
        log.info("GET /prestamos/usuario/{}", idUsuario);
        return ResponseEntity.ok(prestamoService.historialPorUsuario(idUsuario));
    }

    //historial de préstamos por libro
    @GetMapping("/libro/{idLibro}")
    public ResponseEntity<List<PrestamoResponse>> historialPorLibro(@PathVariable Long idLibro) {
        log.info("GET /prestamos/libro/{}", idLibro);
        return ResponseEntity.ok(prestamoService.obtenerHistorialPorLibro(idLibro));
    }

    // listar por estado del prestamo (activos o devueltos)
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PrestamoResponse>> listarPorEstado(@PathVariable String estado) {
        log.info("GET /prestamos/estado/{}", estado);
        return ResponseEntity.ok(prestamoService.listarPrestamosPorEstado(estado));
    }

    //crear un nuevo préstamo
    @PostMapping
    public ResponseEntity<PrestamoResponse> crearPrestamo(@Valid @RequestBody PrestamoRequest request) {
        log.info("POST /prestamos");
        PrestamoResponse crearPrestamo = prestamoService.crearPrestamo(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(crearPrestamo.getId())
                .toUri();
        return ResponseEntity.created(location).body(crearPrestamo);
    }

    //devolución de un libro (accion de negocio)
    @PatchMapping("/{id}/devolucion")
    public ResponseEntity<PrestamoResponse> devolverLibro(@PathVariable Long id) {
        log.info("PATCH /prestamos/{}/devolucion", id);
        return ResponseEntity.ok(prestamoService.devolverLibro(id));
    }

    //en este microservicio se descarta eliminar y actualizar, porque es un servicio de negocio (transaccional), no de mantenimiento (CRUD).
}
