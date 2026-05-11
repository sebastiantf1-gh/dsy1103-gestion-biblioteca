package cl.duoc.dsy1103.categorias_microservice.controller;

import cl.duoc.dsy1103.categorias_microservice.dto.CategoriaRequest;
import cl.duoc.dsy1103.categorias_microservice.dto.CategoriaResponse;
import cl.duoc.dsy1103.categorias_microservice.dto.CategoriaUpdateRequest;
import cl.duoc.dsy1103.categorias_microservice.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categorias")
@Slf4j
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> buscarCategorias(){
        log.info("GET /categorias");
        return ResponseEntity.ok(categoriaService.buscarCategorias());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscarCategoriaPorId(@PathVariable Long id){
        log.info("GET /categorias/{}", id);
        return ResponseEntity.ok(categoriaService.buscarCategoriaPorId(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> crearCategoria(@Valid @RequestBody CategoriaRequest categoria){
        log.info("POST /categorias");
        CategoriaResponse crearCategoria = categoriaService.crearCategoria(categoria);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(crearCategoria.getId())
                .toUri();
        return ResponseEntity.created(location).body(crearCategoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizarCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaUpdateRequest categoria){
        log.info("PUT /categorias/{}", id);
        return ResponseEntity.ok(categoriaService.actualizarCategoria(id,categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id){
        log.info("DELETE /categorias/{}", id);
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
