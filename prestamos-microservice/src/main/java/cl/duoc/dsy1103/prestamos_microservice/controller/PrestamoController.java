package cl.duoc.dsy1103.prestamos_microservice.controller;

import cl.duoc.dsy1103.prestamos_microservice.dto.PrestamoRequest;
import cl.duoc.dsy1103.prestamos_microservice.dto.PrestamoResponse;
import cl.duoc.dsy1103.prestamos_microservice.service.PrestamoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Préstamos", description = "Endpoints transaccionales para el control, registro, devoluciones y seguimiento del ciclo de vida de los préstamos de libros")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @Operation(summary = "Obtener todos los préstamos", description = "Retorna un listado histórico y global con todos los préstamos registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de préstamos recuperado con éxito"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT faltante, expirado o inválido"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Privilegios de lectura insuficientes para personal administrativo")
    })
    @GetMapping
    public ResponseEntity<List<PrestamoResponse>> buscarPrestamos() {
        log.info("GET /prestamos");
        return ResponseEntity.ok(prestamoService.listarTodosLosPrestamos());
    }

    @Operation(summary = "Buscar préstamo por ID", description = "Recupera la información detallada de una transacción de préstamo específica proporcionando su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Préstamo encontrado y retornado con éxito"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Credenciales de autenticación no válidas"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Acceso denegado a este recurso"),
            @ApiResponse(responseCode = "404", description = "No encontrado - El ID de préstamo especificado no existe en el sistema")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponse> obtenerPrestamoPorId(
            @Parameter(description = "ID numérico del préstamo a consultar", example = "1", required = true)
            @PathVariable Long id) {
        log.info("GET /prestamos/{}", id);
        return ResponseEntity.ok(prestamoService.obtenerPrestamoPorId(id));
    }

    @Operation(summary = "Historial de préstamos por Usuario", description = "Recupera todos los préstamos (activos y devueltos) asociados a un usuario específico mediante la integración con el microservicio de usuarios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial del usuario obtenido con éxito"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Error en la validación del token perimetral"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Roles insuficientes")
    })
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<PrestamoResponse>> historialPorUsuario(
            @Parameter(description = "ID numérico del usuario para filtrar su historial", example = "10", required = true)
            @PathVariable Long idUsuario) {
        log.info("GET /prestamos/usuario/{}", idUsuario);
        return ResponseEntity.ok(prestamoService.historialPorUsuario(idUsuario));
    }

    @Operation(summary = "Historial de préstamos por Libro", description = "Recupera la trazabilidad completa de préstamos asociados a un libro en particular mediante la integración distribuida con el catálogo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial de transacciones del libro recuperado con éxito"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Acción denegada")
    })
    @GetMapping("/libro/{idLibro}")
    public ResponseEntity<List<PrestamoResponse>> historialPorLibro(
            @Parameter(description = "ID numérico del libro para auditar sus movimientos", example = "5", required = true)
            @PathVariable Long idLibro) {
        log.info("GET /prestamos/libro/{}", idLibro) ;
        return ResponseEntity.ok(prestamoService.obtenerHistorialPorLibro(idLibro));
    }

    @Operation(summary = "Listar préstamos por Estado", description = "Filtra las transacciones de préstamos vigentes basándose en su estado operativo actual ('ACTIVO' o 'DEVUELTO').")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado filtrado por estado obtenido con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta - El estado proporcionado no es válido"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Error de firma digital"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Permisos denegados")
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PrestamoResponse>> listarPorEstado(
            @Parameter(description = "Estado actual del préstamo", example = "ACTIVO", required = true)
            @PathVariable String estado) {
        log.info("GET /prestamos/estado/{}", estado);
        return ResponseEntity.ok(prestamoService.listarPrestamosPorEstado(estado));
    }

    @Operation(summary = "Registrar un nuevo préstamo", description = "Crea un registro de préstamo en el sistema. Valida dinámicamente mediante WebClient que el usuario exista y que el libro tenga existencias disponibles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Préstamo registrado exitosamente (Incluye cabecera 'Location' con la URI de auditoría)"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta - Datos de entrada inválidos o reglas de negocio infringidas (ej: Libro no disponible)"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o ausente"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Rol no autorizado para efectuar préstamos")
    })
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

    @Operation(summary = "Registrar la devolución de un libro", description = "Realiza la acción de negocio para dar término a un préstamo. Cambia el estado transaccional a 'DEVUELTO' y sincroniza el inventario del libro a través de WebClient.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devolución procesada con éxito y estados sincronizados correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Operación resguardada por firma perimetral"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Acción exclusiva de roles autorizados"),
            @ApiResponse(responseCode = "404", description = "No encontrado - El ID suministrado no corresponde a ninguna transacción activa")
    })
    @PatchMapping("/{id}/devolucion")
    public ResponseEntity<PrestamoResponse> devolverLibro(
            @Parameter(description = "ID numérico de la transacción de préstamo que finaliza", example = "1", required = true)
            @PathVariable Long id) {
        log.info("PATCH /prestamos/{}/devolucion", id);
        return ResponseEntity.ok(prestamoService.devolverLibro(id));
    }
}