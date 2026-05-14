package cl.duoc.autenticacion_microservice.controller;

import cl.duoc.autenticacion_microservice.dto.AutenticacionResponse;
import cl.duoc.autenticacion_microservice.dto.LoginRequest;
import cl.duoc.autenticacion_microservice.service.AutenticacionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@Slf4j
public class AutenticacionController {
    @Autowired
    private AutenticacionService autenticacionService;

    @PostMapping
    public ResponseEntity<AutenticacionResponse> login(@Valid @RequestBody LoginRequest request) {
        AutenticacionResponse response = autenticacionService.login(request);
        return ResponseEntity.ok(response);
    }
}
