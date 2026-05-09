package cl.duoc.dsy1103.generos_microservice.controller;


import cl.duoc.dsy1103.generos_microservice.dto.GeneroRequest;
import cl.duoc.dsy1103.generos_microservice.dto.GeneroResponse;
import cl.duoc.dsy1103.generos_microservice.service.GeneroService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/generos")
@Slf4j
public class GeneroController {

    @Autowired
    private GeneroService generoService;

    @PostMapping
    public ResponseEntity agregarGenero(@Valid @RequestBody GeneroRequest generoRequest){
        log.info("Post /generos");
        GeneroResponse generoAgregado = generoService.agregarGenero(generoRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(generoAgregado.getIdGenero())
                .toUri();
        return ResponseEntity.created(location).body(generoAgregado);

    }

    @GetMapping
    public ResponseEntity listarGeneros(){
        log.info("Get /usuarios");
        return ResponseEntity.ok(generoService.listarGeneros());
    }

    @GetMapping
    public ResponseEntity<GeneroResponse> buscarGeneroPorId(@PathVariable Long id){
        log.info("Get /generos/{}", id);
        return ResponseEntity.ok(generoService.buscarGeneroPorId(id));

    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneroResponse> modificarGenero(@PathVariable Long id, @Valid @RequestBody GeneroRequest generoRequest){
        log.info("Put /usuarios/{}", id);
        return ResponseEntity.ok(generoService.modificarGenero(id, generoRequest));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarGenero(@PathVariable Long id){
        log.info("Delete /generos/{}", id);
        generoService.eliminarGenero(id);
        return ResponseEntity.noContent().build();

    }
}
