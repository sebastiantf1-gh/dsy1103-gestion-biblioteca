package cl.duoc.autenticacion_microservice.controller;

import cl.duoc.autenticacion_microservice.dto.UsuarioPersonalResponse;
import cl.duoc.autenticacion_microservice.service.UsuarioPersonalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/usuarios")
@Slf4j
public class UsuarioPersonalController {
    @Autowired
    private UsuarioPersonalService usuarioService;

    @GetMapping
    public List<UsuarioPersonalResponse> obtenerUsuarios() {
        return usuarioService.obtenerUsuarios();
    }

    @GetMapping("/{id}")
    public UsuarioPersonalResponse obtenerUsuarioPorId(@PathVariable Long id) {
        return usuarioService.obtenerUsuarioPorId(id);
    }

    @GetMapping("/nombre/{nombre}")
    public UsuarioPersonalResponse obtenerUsuarioPorNombre(@PathVariable String nombre) {
        return usuarioService.obtenerUsuarioPorNombre(nombre);
    }
}
