package cl.duoc.dsy1103.libros_microservice.controller;

import cl.duoc.dsy1103.libros_microservice.dto.LibroRequest;
import cl.duoc.dsy1103.libros_microservice.dto.LibroResponse;
import cl.duoc.dsy1103.libros_microservice.dto.LibroUpdateRequest;
import cl.duoc.dsy1103.libros_microservice.service.LibroService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/libros")
@Slf4j
public class LibroController {
    @Autowired
    private LibroService libroService;

    @GetMapping
    public ResponseEntity<List<LibroResponse>> buscarLibros(){
        log.info("GET /libros");
        return ResponseEntity.ok(libroService.buscarLibros());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> buscarLibroPorId(@PathVariable Long id){
        log.info("GET /libros/{}", id);
        return ResponseEntity.ok(libroService.buscarLibroPorId(id));
    }

    @PostMapping
    public ResponseEntity<LibroResponse> crearLibro(@Valid @RequestBody LibroRequest libro){
        log.info("POST /libros");
        LibroResponse crearLibro = libroService.crearLibro(libro);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(crearLibro.getId())
                .toUri();
        return ResponseEntity.created(location).body(crearLibro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroResponse> actualizarLibro(@PathVariable Long id, @Valid @RequestBody LibroUpdateRequest libro){
        log.info("PUT /libros/{}", id);
        return ResponseEntity.ok(libroService.actualizarLibro(id,libro));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Long id){
        log.info("DELETE /libros/{}", id);
        libroService.eliminarLibro(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/prestamo")
    public ResponseEntity<LibroResponse> prestarLibro(@PathVariable Long id){
        log.info("PATCH /libros/{}/prestamo", id);
        return ResponseEntity.ok(libroService.prestarLibro(id));
    }
    @PatchMapping("/{id}/devolucion")
    public ResponseEntity<LibroResponse> devolverLibro(@PathVariable Long id){
        log.info("PATCH /libros/{}/devolucion", id);
        return ResponseEntity.ok(libroService.devolverLibro(id));
    }

}
