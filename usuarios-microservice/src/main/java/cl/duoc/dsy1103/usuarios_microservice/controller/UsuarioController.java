package cl.duoc.dsy1103.usuarios_microservice.controller;

import cl.duoc.dsy1103.usuarios_microservice.dto.UsuarioResponse;
import cl.duoc.dsy1103.usuarios_microservice.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
