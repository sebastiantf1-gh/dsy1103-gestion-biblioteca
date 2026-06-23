package cl.duoc.autenticacion_microservice.controller;

import cl.duoc.autenticacion_microservice.dto.AutenticacionResponse;
import cl.duoc.autenticacion_microservice.dto.CrearUsuarioRequest;
import cl.duoc.autenticacion_microservice.dto.LoginRequest;
import cl.duoc.autenticacion_microservice.service.AutenticacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Autenticación", description = "Controlador encargado de la emisión de Tokens JWT y registro de nuevo personal")
public class AutenticacionController {
    @Autowired
    private AutenticacionService autenticacionService;

    @Operation(
            summary = "Iniciar sesión (Login)",
            description = "Valida las credenciales del personal de biblioteca y genera un token de tipo Bearer JWT."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa. Retorna el token de acceso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AutenticacionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada mal estructurados o inválidos"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas - El usuario no existe o la contraseña es incorrecta")
    })
    @PostMapping
    public ResponseEntity<AutenticacionResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Intento de inicio de sesión para el usuario: {}", request.getNombreUsuario());
        AutenticacionResponse response = autenticacionService.login(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Registrar nuevo usuario de personal",
            description = "Crea una nueva credencial en la base de datos cifrando la contraseña mediante BCrypt."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado de manera exitosa en el sistema"),
            @ApiResponse(responseCode = "400", description = "Campos obligatorios faltantes o duplicados (email o username ya registrados)")
    })
    @PostMapping("/register")
    public ResponseEntity<String> registrar(@Valid @RequestBody CrearUsuarioRequest request) {
        log.info("Registrando un nuevo usuario en el sistema: {}", request.getNombreUsuario());
        String respuesta = autenticacionService.registrarUsuario(request);
        return ResponseEntity.ok(respuesta);
    }
}
