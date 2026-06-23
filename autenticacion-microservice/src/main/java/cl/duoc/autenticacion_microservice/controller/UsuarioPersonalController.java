package cl.duoc.autenticacion_microservice.controller;

import cl.duoc.autenticacion_microservice.dto.UsuarioPersonalResponse;
import cl.duoc.autenticacion_microservice.service.UsuarioPersonalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Personal de Biblioteca", description = "Endpoints privados para la consulta y gestión del personal autenticado")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioPersonalController {
    @Autowired
    private UsuarioPersonalService usuarioService;

    @Operation(
            summary = "Obtener todo el personal",
            description = "Recupera una lista con todo el personal registrado en la biblioteca. Requiere un Token JWT válido."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios recuperada exitosamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioPersonalResponse.class)))),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token inválido, ausente o expirado")
    })
    @GetMapping
    public List<UsuarioPersonalResponse> obtenerUsuarios() {
        log.info("REST request para obtener todo el personal de biblioteca");
        return usuarioService.obtenerUsuarios();
    }

    @Operation(
            summary = "Buscar personal por ID",
            description = "Busca los detalles  del personal utilizando su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioPersonalResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token inválido"),
            @ApiResponse(responseCode = "404", description = "No encontrado - El ID proporcionado no existe en la base de datos")
    })
    @GetMapping("/{id}")
    public UsuarioPersonalResponse obtenerUsuarioPorId(@PathVariable Long id) {
        log.info("REST request para obtener personal por ID: {}", id);
        return usuarioService.obtenerUsuarioPorId(id);
    }

    @Operation(
            summary = "Buscar personal por nombre de usuario",
            description = "Busca los detalles de un miembro del personal utilizando su nombre de usuario (username)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioPersonalResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "No encontrado - El nombre de usuario no existe")
    })
    @GetMapping("/nombre/{nombre}")
    public UsuarioPersonalResponse obtenerUsuarioPorNombre(@PathVariable String nombre) {
        log.info("REST request para obtener personal por nombre: {}", nombre);
        return usuarioService.obtenerUsuarioPorNombre(nombre);
    }
}
