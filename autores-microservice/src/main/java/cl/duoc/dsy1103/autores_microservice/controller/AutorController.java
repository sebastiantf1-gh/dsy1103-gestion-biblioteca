package cl.duoc.dsy1103.autores_microservice.controller;

import cl.duoc.dsy1103.autores_microservice.dto.AutorRequest;
import cl.duoc.dsy1103.autores_microservice.dto.AutorResponse;
import cl.duoc.dsy1103.autores_microservice.dto.AutorUpdateRequest;
import cl.duoc.dsy1103.autores_microservice.service.AutorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/autores")
@Slf4j
public class AutorController {

    @Autowired
    private AutorService autorService;

    //Obtener todos los autores
    @GetMapping
    public ResponseEntity<List<AutorResponse>> buscarAutores(){
    log.info("GET /autores");
    return ResponseEntity.ok(autorService.buscarAutores());
    }

    //Obtener autor por ID
    @GetMapping("/{id}")
    public ResponseEntity<AutorResponse> buscarAutorPorId(@PathVariable Long id){
        log.info("GET /autores/{}", id);
        return ResponseEntity.ok(autorService.buscarAutorPorId(id));
    }

    //Crear autor
    @PostMapping
    public ResponseEntity<AutorResponse> crearAutor(@Valid @RequestBody AutorRequest autor){//@Valid para validar anotaciones del dto
        log.info("Post /autores");
        AutorResponse crearAutor = autorService.crearAutor(autor);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(crearAutor.getId())
                .toUri();
        return ResponseEntity.created(location).body(crearAutor);
    }

    //Actualizar autor
    @PutMapping("/{id}")
    public ResponseEntity<AutorResponse> actualizarAutor(@PathVariable Long id, @Valid @RequestBody AutorUpdateRequest autor){
        log.info("PUT /autores/{}", id);
        return ResponseEntity.ok(autorService.actualizarAutor(id, autor));
    }

    //Eliminar autor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAutor(@PathVariable Long id){
        log.info("DELETE /autores/{}", id);
        autorService.eliminarAutor(id);
        return ResponseEntity.noContent().build(); // no body, con .build() se cierra configuracion y se envía vacío.
    }



}
