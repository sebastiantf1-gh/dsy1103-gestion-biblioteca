package cl.duoc.dsy1103.usuarios_microservice.controller;

import cl.duoc.dsy1103.usuarios_microservice.dto.UsuarioRequest;
import cl.duoc.dsy1103.usuarios_microservice.dto.UsuarioResponse;
import cl.duoc.dsy1103.usuarios_microservice.dto.UsuarioUpdateRequest;

import cl.duoc.dsy1103.usuarios_microservice.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;



@RestController
@RequestMapping("/usuarios")
@Slf4j
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios(){
        log.info("Get /usuarios");
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorId(@PathVariable Long id){
        log.info("Get /usuarios/{}", id);
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorId(id));
    }

    @PostMapping
    public  ResponseEntity <UsuarioResponse> crearUsuario(@Valid @RequestBody UsuarioRequest usuarioRequest){
        log.info("Post /usuarios");
        UsuarioResponse usuarioCreado = usuarioService.crearUsuario(usuarioRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuarioCreado.getId())
                .toUri();
        return ResponseEntity.created(location).body(usuarioCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> modificarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateRequest usuarioUpdateRequest){
        log.info("Put /usuarios/{}", id);
        return ResponseEntity.ok(usuarioService.modificarUsuario(id, usuarioUpdateRequest));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id){
        log.info("Delete /usuarios/{}", id);
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
