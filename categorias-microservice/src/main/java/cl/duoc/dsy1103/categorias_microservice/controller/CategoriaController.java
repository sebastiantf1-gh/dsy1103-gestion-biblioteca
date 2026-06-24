package cl.duoc.dsy1103.categorias_microservice.controller;

import cl.duoc.dsy1103.categorias_microservice.dto.CategoriaRequest;
import cl.duoc.dsy1103.categorias_microservice.dto.CategoriaResponse;
import cl.duoc.dsy1103.categorias_microservice.dto.CategoriaUpdateRequest;
import cl.duoc.dsy1103.categorias_microservice.service.CategoriaService;
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

@RestController
@RequestMapping("/categorias")
@Slf4j
@Tag(name = "Categorias", description = "Endpoints para el mantenimiento, consulta y administración de las categorías de libros en la biblioteca")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Operation(summary = "Obtener todas las categorías", description = "Retorna una lista completa con todas las categorías registradas en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías recuperada con éxito"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT faltante, expirado o inválido"),
            @ApiResponse(responseCode = "403", description = "Prohibido - No posees los roles necesarios para acceder a este recurso")
    })
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> buscarCategorias(){
        log.info("GET /categorias");
        return ResponseEntity.ok(categoriaService.buscarCategorias());
    }

    @Operation(summary = "Buscar categoría por ID", description = "Recupera los detalles de una categoría específica proporcionando su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada y retornada con éxito"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT faltante o inválido"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Permisos insuficientes"),
            @ApiResponse(responseCode = "404", description = "No encontrado - La categoría con el ID suministrado no existe")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscarCategoriaPorId(
            @Parameter(description = "ID numérico de la categoría a consultar", example = "1", required = true)
            @PathVariable Long id){
        log.info("GET /categorias/{}", id);
        return ResponseEntity.ok(categoriaService.buscarCategoriaPorId(id));
    }

    @Operation(summary = "Crear una nueva categoría", description = "Registra una categoría en el sistema. El cuerpo de la solicitud es validado de forma estricta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada de manera exitosa (Incluye la cabecera 'Location' con la URI del nuevo recurso)"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta - Datos de entrada inválidos o faltantes"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Rol de usuario no autorizado para crear recursos")
    })
    @PostMapping
    public ResponseEntity<CategoriaResponse> crearCategoria(@Valid @RequestBody CategoriaRequest categoria){
        log.info("POST /categorias");
        CategoriaResponse crearCategoria = categoriaService.crearCategoria(categoria);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(crearCategoria.getId())
                .toUri();
        return ResponseEntity.created(location).body(crearCategoria);
    }

    @Operation(summary = "Actualizar una categoría existente", description = "Modifica los atributos de una categoría basándose en su ID y en el payload estructurado enviado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría modificada con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta - Error en la validación de los datos del cuerpo"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token de autenticación inválido"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Privilegios insuficientes para realizar modificaciones"),
            @ApiResponse(responseCode = "404", description = "No encontrado - No se encontró ninguna categoría con el ID proveído")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizarCategoria(
            @Parameter(description = "ID numérico de la categoría a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody CategoriaUpdateRequest categoria){
        log.info("PUT /categorias/{}", id);
        return ResponseEntity.ok(categoriaService.actualizarCategoria(id,categoria));
    }

    @Operation(summary = "Eliminar una categoría", description = "Realiza la remoción física de una categoría del sistema utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría dada de baja de manera exitosa (Sin contenido de respuesta)"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Operación protegida por JWT"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Acción exclusiva de roles administrativos"),
            @ApiResponse(responseCode = "404", description = "No encontrado - El ID especificado no coincide con ningún registro")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(
            @Parameter(description = "ID numérico de la categoría que se desea remover", example = "1", required = true)
            @PathVariable Long id){
        log.info("DELETE /categorias/{}", id);
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}