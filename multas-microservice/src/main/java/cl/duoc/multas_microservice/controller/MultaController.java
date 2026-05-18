package cl.duoc.multas_microservice.controller;

import cl.duoc.multas_microservice.dto.MultaRequest;
import cl.duoc.multas_microservice.dto.MultaResponse;
import cl.duoc.multas_microservice.service.MultaService;
import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/multas")
@Slf4j
public class MultaController {
    @Autowired
    private MultaService multaService;

    @PostMapping
    public ResponseEntity<MultaResponse> crearMulta(@Valid @RequestBody MultaRequest multaRequest,
                                                    @RequestHeader("Authorization") String token){
        log.info("REST request para crear una multa: {}", multaRequest);
        MultaResponse response = multaService.crearMulta(multaRequest, token);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MultaResponse>> listarTodas() {
        log.info("REST request para listar todas las multas");
        List<MultaResponse> multas = multaService.obtenerTodasLasMultas();
        return ResponseEntity.ok(multas);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MultaResponse>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        log.info("REST request para obtener multas del usuario ID: {}", usuarioId);
        List<MultaResponse> multas = multaService.obtenerMultasPorUsuarioId(usuarioId);
        return ResponseEntity.ok(multas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMulta(@PathVariable Long id) {
        log.info("REST request para eliminar multa ID: {}", id);
        multaService.eliminarMulta(id);
        return ResponseEntity.noContent().build();
    }
}
