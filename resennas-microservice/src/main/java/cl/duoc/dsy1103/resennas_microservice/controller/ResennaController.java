package cl.duoc.dsy1103.resennas_microservice.controller;

import cl.duoc.dsy1103.resennas_microservice.dto.ResennaRequest;
import cl.duoc.dsy1103.resennas_microservice.dto.ResennaResponse;
import cl.duoc.dsy1103.resennas_microservice.dto.ResennaUpdateRequest;
import cl.duoc.dsy1103.resennas_microservice.service.ResennaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/resennas")
@Slf4j
public class ResennaController {

    @Autowired
    private ResennaService resennaService;

    @GetMapping("/{id}")
    public ResponseEntity<ResennaResponse> buscarResennaPorId(@PathVariable Long id){
        log.info("Get /resennas/{}",id);
        return ResponseEntity.ok(resennaService.buscarResennaPorId(id));
    }

    @PostMapping
    public ResponseEntity<ResennaResponse> crearResenna(@Valid @RequestBody ResennaRequest resennaRequest){
        log.info("Post /resennas");
        ResennaResponse crearResenna = resennaService.crearResenna(resennaRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(crearResenna.getId())
                .toUri();
        return ResponseEntity.created(location).body(crearResenna);
    }

    @GetMapping
    public ResponseEntity<List<ResennaResponse>>listarResennas(){
        log.info("Get /resennas");
        return ResponseEntity.ok(resennaService.listarResennas());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResennaResponse> modificarResenna(@PathVariable Long id,@Valid @RequestBody ResennaUpdateRequest resennaUpdateRequest){
        log.info("Put /resennas/{}", id);
        return ResponseEntity.ok(resennaService.modificarResenna(id,resennaUpdateRequest));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResenna(@PathVariable Long id){
        log.info("Delete /resennas/{}", id);
        resennaService.eliminarResenna(id);
        return ResponseEntity.noContent().build();

    }

}
