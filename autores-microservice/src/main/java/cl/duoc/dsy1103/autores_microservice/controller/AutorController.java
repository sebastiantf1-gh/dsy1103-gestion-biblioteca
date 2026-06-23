package cl.duoc.dsy1103.autores_microservice.controller;

import cl.duoc.dsy1103.autores_microservice.dto.AutorRequest;
import cl.duoc.dsy1103.autores_microservice.dto.AutorResponse;
import cl.duoc.dsy1103.autores_microservice.dto.AutorUpdateRequest;
import cl.duoc.dsy1103.autores_microservice.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

@RestController //Combina @Controller y @ResponseBody. Indica a Spring que esta clase procesará peticiones REST y que
                // todos los retornos de los métodos serán convertidos de forma automática a formato JSON en el
                // cuerpo de la respuesta HTTP.
@RequestMapping("/autores") // Establece la ruta base semántica a nivel de clase, centralizando la raíz del recurso bajo las mejores prácticas REST
@Slf4j // Inyecta el Logger de SLF4J para mantener la trazabilidad de los verbos HTTP y endpoints consumidos a nivel de consola.
@Tag(name = "Autores", description = "Operaciones relacionadas con los autores.")
public class AutorController {

    @Autowired
    private AutorService autorService;

    //Obtener todos los autores
    @GetMapping
    @Operation(summary = "Obtener todos los autores.", description = "Obtiene una lista de todos los autores.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autores listados correctamente.",
                content =  @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AutorResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Autores no encontrados.", content = @Content),
            @ApiResponse(responseCode = "401", description = "No se puede acceder al recurso. Se requieren credenciales de autenticacion.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos para acceder al recurso.", content = @Content)
    })
    public ResponseEntity<List<AutorResponse>> buscarAutores(){
    log.info("GET /autores");
        // Retorna un código HTTP 200 OK de forma explícita encapsulado a través de ResponseEntity.
        // El controlador no manipula datos, solo delega al servicio y despacha el JSON resultante.
    return ResponseEntity.ok(autorService.buscarAutores());
    }

    //Obtener autor por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener autor por ID.", description = "Obtiene un autor por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor encontrado con éxito",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AutorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Autor no encontrado. ID no existe.", content = @Content),
            @ApiResponse(responseCode = "401", description = "No se puede acceder al recurso. Se requieren credenciales de autenticacion.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos para acceder al recurso.", content = @Content)
    })
    public ResponseEntity<AutorResponse> buscarAutorPorId(
            @Parameter(description = "ID del autor a buscar", required = true, example = "1") @PathVariable Long id){
        log.info("GET /autores/{}", id);
        return ResponseEntity.ok(autorService.buscarAutorPorId(id));
    }

    //Crear autor
    @PostMapping
    @Operation(summary = "Agregar un nuevo autor", description = "Agrega un autor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Autor creado con éxito.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AutorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Peticion invalida",  content = @Content),
            @ApiResponse(responseCode = "401", description = "No se puede realizar la peticion. Se requieren credenciales de autenticacion.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos para realizar la peticion.", content = @Content)
    })
    public ResponseEntity<AutorResponse> crearAutor(@Valid @RequestBody AutorRequest autor){
        log.info("POST /autores");
        //Integración del Bean Validation. La anotación '@Valid' intercepta el request
        //y dispara las reglas del DTO (como @NotBlank) antes de entrar al cuerpo del método. Si falla, corta la petición de inmediato.
        AutorResponse crearAutor = autorService.crearAutor(autor);
        //Mediante 'ServletUriComponentsBuilder', se genera de forma
        // dinámica la URI del nuevo recurso creado (ej: http://localhost:8084/api/autores/5) y se inyecta en la cabecera 'Location'
        // de la respuesta de red, retornando un estado 201 Created.
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(crearAutor.getId())
                .toUri();
        return ResponseEntity.created(location).body(crearAutor);
    }

    //Actualizar autor
    @PutMapping("/{id}")
    @Operation(summary = "Modificar un autor", description = "Modifica un autor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor modificado con exito.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AutorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Autor no encontrado. ID no existe.", content =  @Content),
            @ApiResponse(responseCode = "401", description = "No se puede realizar la peticion. Se requieren credenciales de autenticacion.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos para realizar la peticion.", content = @Content)
    })
    public ResponseEntity<AutorResponse> actualizarAutor(
            @Parameter(description = "ID del autor a modficar", required = true, example = "1")
            @PathVariable Long id, @Valid @RequestBody AutorUpdateRequest autor){
        log.info("PUT /autores/{}", id);
        // Mapea semánticamente la actualización de recursos mediante PUT. Se inyecta '@Valid'
        // para garantizar que la nueva información opcional respete las restricciones básicas de extensión y semántica.
        return ResponseEntity.ok(autorService.actualizarAutor(id, autor));
    }

    //Eliminar autor
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar autor por ID.", description = "Elimina un autor por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Autor eliminado con exito.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Autor no encontrado. ID no existe.", content = @Content),
            @ApiResponse(responseCode = "401", description = "No se puede realizar la peticion. Se requieren credenciales de autenticacion.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sin permisos para realizar la peticion.", content = @Content)
    })
    public ResponseEntity<Void> eliminarAutor(
            @Parameter(description = "ID del autor a eliminar", required = true, example = "5")
            @PathVariable Long id){
        log.info("DELETE /autores/{}", id);
        autorService.eliminarAutor(id);
        // Se utiliza '.noContent().build()' para despachar un estado HTTP 204 No Content explícito hacia el cliente,
        // notificando que la acción se completó con éxito en la persistencia real pero que no existe una respuesta JSON de retorno.
        return ResponseEntity.noContent().build(); // si no hay body, con .build() se cierra configuracion y se envía vacío.
    }
}
