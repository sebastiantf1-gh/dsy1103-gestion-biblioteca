package cl.duoc.dsy1103.usuarios_microservice.controller;

import cl.duoc.dsy1103.usuarios_microservice.dto.UsuarioRequest;
import cl.duoc.dsy1103.usuarios_microservice.dto.UsuarioResponse;
import cl.duoc.dsy1103.usuarios_microservice.dto.UsuarioUpdateRequest;

import cl.duoc.dsy1103.usuarios_microservice.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;



@RestController
@RequestMapping("/usuarios")
@Slf4j
@Tag(name = "Usuarios", description = "Gestión de usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(
            summary = "Listar todos los usuarios",
            description = "Retorna la lista completa de usuarios registrados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida con exito",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(value = """
                            [
                             {
                              "id": 1,
                              "nombreCompleto": "Juan Pérez",
                              "email": "juan@email.com",
                              "telefono": "912345678",
                              "fechaRegistro": "2026-06-19T10:00:00"
                             }
                            ]
                            """)))
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios(){
        log.info("Get /usuarios");
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }
    @Operation(
            summary = "Buscar usuarios por ID",
            description = "Retorna los datos de un usuarios especifico segun su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(value = """
                            {
                             "id": 1,
                             "nombreCompleto": "Juan Pérez",
                             "email": "juan@email.com",
                             "telefono": "912345678",
                             "fechaRegistro": "2026-06-19T10:00:00"  
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorId(
            @Parameter(description = "ID del usuario a buscar", example = "1")
            @PathVariable Long id){
        log.info("Get /usuarios/{}", id);
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorId(id));
    }

    @Operation(
            summary = "Crear un nuevo usuario",
            description = "Registra un nuevo usuario en el sistema. La fecha de registro se asigna automaticamente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(value = """
                            {
                             "id": 1,
                             "nombreCompleto": "Juan Pérez",
                             "email": "juan@email.com",
                             "telefono": "912345678",
                             "fechaRegistro": "2026-06-19T10:00:00"
                            }
                            """))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos en la solicitud", content = @Content)
    })
    @PostMapping
    public  ResponseEntity <UsuarioResponse> crearUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description= "Datos del usuarios a registrar",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                             "nombreCompleto": "Juan Pérez",
                             "email": "juan@email.com",
                             "telefono": "912345678"
                            }
                            """))
            )
            @Valid @RequestBody UsuarioRequest usuarioRequest){
        log.info("Post /usuarios");
        UsuarioResponse usuarioCreado = usuarioService.crearUsuario(usuarioRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuarioCreado.getId())
                .toUri();
        return ResponseEntity.created(location).body(usuarioCreado);
    }

    @Operation(
            summary = "Modifica un usuario existente",
            description = "Actualiza parcialmente los datos de un usuario. Solo se modifican los campos enviados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario modificado con exito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> modificarUsuario(
            @Parameter(description = "ID del usuario a modificar", example = "1")
            @PathVariable Long id, @Valid @RequestBody UsuarioUpdateRequest usuarioUpdateRequest){
        log.info("Put /usuarios/{}", id);
        return ResponseEntity.ok(usuarioService.modificarUsuario(id, usuarioUpdateRequest));
    }

    @Operation(
            summary = "Eliminar un usuario",
            description = "Elimina permanentemente un usuario del sistema segun su ID "
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado con exito", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(
            @Parameter(description = "ID del usuario a eliminar", example = "1")
            @PathVariable Long id){
        log.info("Delete /usuarios/{}", id);
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
