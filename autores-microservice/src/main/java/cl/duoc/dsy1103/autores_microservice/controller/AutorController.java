package cl.duoc.dsy1103.autores_microservice.controller;

import cl.duoc.dsy1103.autores_microservice.dto.AutorRequest;
import cl.duoc.dsy1103.autores_microservice.dto.AutorResponse;
import cl.duoc.dsy1103.autores_microservice.dto.AutorUpdateRequest;
import cl.duoc.dsy1103.autores_microservice.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController // Combina @Controller y @ResponseBody. Indica a Spring que esta clase procesará peticiones REST y que todos los retornos de los métodos serán convertidos de forma automática a formato JSON en el cuerpo de la respuesta HTTP.
@RequestMapping("/autores") // Establece la ruta base semántica a nivel de clase, centralizando la raíz del recurso bajo las mejores prácticas REST
@Slf4j // Inyecta el Logger de SLF4J para mantener la trazabilidad de los verbos HTTP y endpoints consumidos a nivel de consola.
@Tag(name = "Autores", description = "Endpoints para el mantenimiento, registro, actualización y consulta de los autores literarios en el sistema")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @Operation(summary = "Obtener todos los autores", description = "Retorna una lista completa con todos los autores registrados en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de autores recuperada con éxito"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT faltante, expirado o inválido"),
            @ApiResponse(responseCode = "403", description = "Prohibido - No posees los roles necesarios para acceder a este recurso")
    })
    @GetMapping
    public ResponseEntity<List<AutorResponse>> buscarAutores(){
        log.info("GET /autores");
        // Retorna un código HTTP 200 OK de forma explícita encapsulado a través de ResponseEntity.
        // El controlador no manipula datos, solo delega al servicio y despacha el JSON resultante.
        return ResponseEntity.ok(autorService.buscarAutores());
    }

    @Operation(summary = "Buscar autor por ID", description = "Recupera los detalles de un autor específico proporcionando su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor encontrado y retornado con éxito"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT faltante o inválido"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Permisos insuficientes"),
            @ApiResponse(responseCode = "404", description = "No encontrado - El ID del autor suministrado no existe en el sistema")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AutorResponse> buscarAutorPorId(
            @Parameter(description = "ID numérico del autor a consultar", example = "1", required = true)
            @PathVariable Long id){
        log.info("GET /autores/{}", id);
        return ResponseEntity.ok(autorService.buscarAutorPorId(id));
    }

    @Operation(summary = "Agregar un nuevo autor", description = "Registra un nuevo autor en el sistema. El cuerpo de la solicitud es validado de forma estricta mediante Bean Validation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Autor creado de manera exitosa (Incluye la cabecera 'Location' con la URI del nuevo recurso)"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta - Datos de entrada inválidos o mal estructurados"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Rol de usuario no autorizado para crear recursos")
    })
    @PostMapping
    public ResponseEntity<AutorResponse> crearAutor(@Valid @RequestBody AutorRequest autor){
        log.info("POST /autores");
        // Integración del Bean Validation. La anotación '@Valid' intercepta el request y dispara las reglas del DTO (como @NotBlank) antes de entrar al cuerpo del método. Si falla, corta la petición de inmediato.
        AutorResponse crearAutor = autorService.crearAutor(autor);

        // Mediante 'ServletUriComponentsBuilder', se genera de forma dinámica la URI del nuevo recurso creado (ej: http://localhost:8084/api/autores/5) y se inyecta en la cabecera 'Location' de la respuesta de red, retornando un estado 201 Created.
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(crearAutor.getId())
                .toUri();
        return ResponseEntity.created(location).body(crearAutor);
    }

    @Operation(summary = "Modificar un autor existente", description = "Modifica los atributos de un autor basándose en su ID y en el payload estructurado enviado en el cuerpo de la petición.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor modificado con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta - Error en la validación de los datos del cuerpo"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token de autenticación inválido"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Privilegios insuficientes para realizar modificaciones"),
            @ApiResponse(responseCode = "404", description = "No encontrado - No se encontró ningún autor con el ID proveído")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AutorResponse> actualizarAutor(
            @Parameter(description = "ID numérico del autor a modificar", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody AutorUpdateRequest autor){
        log.info("PUT /autores/{}", id);
        // Mapea semánticamente la actualización de recursos mediante PUT. Se inyecta '@Valid' para garantizar que la nueva información opcional respete las restricciones básicas de extensión y semántica.
        return ResponseEntity.ok(autorService.actualizarAutor(id, autor));
    }

    @Operation(summary = "Eliminar autor por ID", description = "Realiza la remoción física de un autor del sistema utilizando su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Autor eliminado de manera exitosa (Sin contenido de respuesta)"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Operación protegida por JWT"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Acción exclusiva de roles administrativos"),
            @ApiResponse(responseCode = "404", description = "No encontrado - El ID especificado no coincide con ningún registro activo")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAutor(
            @Parameter(description = "ID numérico del autor que se desea remover", example = "5", required = true)
            @PathVariable Long id){
        log.info("DELETE /autores/{}", id);
        autorService.eliminarAutor(id);
        // Se utiliza '.noContent().build()' para despachar un estado HTTP 204 No Content explícito hacia el cliente, notificando que la acción se completó con éxito en la persistencia real pero que no existe una respuesta JSON de retorno.
        return ResponseEntity.noContent().build(); // si no hay body, con .build() se cierra configuracion y se envía vacío.
    }
}