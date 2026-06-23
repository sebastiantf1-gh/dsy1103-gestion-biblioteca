package cl.duoc.multas_microservice.controller;

import cl.duoc.multas_microservice.dto.MultaRequest;
import cl.duoc.multas_microservice.dto.MultaResponse;
import cl.duoc.multas_microservice.service.MultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/multas")
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class MultaController {
    @Autowired
    private MultaService multaService;
    // CREAR
    @Operation(
            summary = "Crear una nueva multa",
            description = "Registra una multa en la base de datos asociada a un usuario y a un préstamo activo. Requiere la validación remota mediante WebClient y un token JWT."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Multa generada exitosamente en el sistema",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o mal estructurados"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT ausente, expirado o mal firmado"),
            @ApiResponse(responseCode = "404", description = "No encontrado - El ID del usuario o del préstamo no existen en los otros microservicios")
    })
    @PostMapping
    public ResponseEntity<MultaResponse> crearMulta(@Valid @RequestBody MultaRequest multaRequest){
        log.info("REST request para crear una multa: {}", multaRequest);
        MultaResponse response = multaService.crearMulta(multaRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Listar Todo

    @Operation(
            summary = "Listar todas las multas",
            description = "Retorna una lista completa con todas las multas registradas en la biblioteca de forma global."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de multas obtenido exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultaResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<MultaResponse>> listarTodas() {
        log.info("REST request para listar todas las multas");
        List<MultaResponse> multas = multaService.obtenerTodasLasMultas();
        return ResponseEntity.ok(multas);
    }
    //OBTENER POR USUARIO ID

    @Operation(
            summary = "Obtener multas por ID de Usuario",
            description = "Busca e identifica todas las multas específicas que tiene asignadas un usuario mediante su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial de multas del usuario recuperado de forma correcta",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MultaResponse.class)))
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MultaResponse>> obtenerPorUsuario(@Parameter(description = "Identificador único del usuario (ID)", required = true, example = "1")
            @PathVariable Long usuarioId) {
        log.info("REST request para obtener multas del usuario ID: {}", usuarioId);
        List<MultaResponse> multas = multaService.obtenerMultasPorUsuarioId(usuarioId);
        return ResponseEntity.ok(multas);
    }

    // ELIMINAR MULTA

    @Operation(
            summary = "Eliminar una multa por ID",
            description = "Realiza la remoción física o eliminación de una multa del sistema a través de su ID. Si no existe, arroja un error controlado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Multa eliminada exitosamente del registro (Sin contenido)"),
            @ApiResponse(responseCode = "404", description = "No encontrado - El ID de la multa ingresado no existe en la base de datos")
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMulta(
            @Parameter(description = "Identificador de la multa a eliminar", required = true, example = "12")
            @PathVariable Long id) {
        log.info("REST request para eliminar multa ID: {}", id);
        multaService.eliminarMulta(id);
        return ResponseEntity.noContent().build();
    }
}
