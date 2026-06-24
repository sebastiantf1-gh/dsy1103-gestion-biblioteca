package cl.duoc.dsy1103.libros_microservice.controller;

import cl.duoc.dsy1103.libros_microservice.dto.LibroRequest;
import cl.duoc.dsy1103.libros_microservice.dto.LibroResponse;
import cl.duoc.dsy1103.libros_microservice.dto.LibroUpdateRequest;
import cl.duoc.dsy1103.libros_microservice.service.LibroService;
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
@RequestMapping("/libros")
@Slf4j
@Tag(name = "Libros", description = "Endpoints para la gestión, catalogación, búsqueda avanzada y control de existencias de libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @Operation(summary = "Obtener todos los libros", description = "Retorna una lista completa con todos los libros registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catálogo de libros recuperado con éxito"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT faltante, expirado o inválido"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Privilegios de lectura insuficientes")
    })
    @GetMapping
    public ResponseEntity<List<LibroResponse>> buscarLibros(){
        log.info("GET /libros");
        return ResponseEntity.ok(libroService.buscarLibros());
    }

    @Operation(summary = "Buscar libro por ID", description = "Recupera la ficha técnica detallada de un libro específico proporcionando su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro encontrado y retornado con éxito"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT faltante o inválido"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Permisos insuficientes"),
            @ApiResponse(responseCode = "404", description = "No encontrado - El ID de libro especificado no existe en el catálogo")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> buscarLibroPorId(
            @Parameter(description = "ID numérico del libro a consultar", example = "1", required = true)
            @PathVariable Long id){
        log.info("GET /libros/{}", id);
        return ResponseEntity.ok(libroService.buscarLibroPorId(id));
    }

    @Operation(summary = "Buscar libros por Autor", description = "Filtra y retorna una lista de todos los libros asociados a un autor específico consultado mediante WebClient.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de libros del autor recuperada con éxito"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Error de token perimetral"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Acción no permitida para el rol actual")
    })
    @GetMapping("/autor/{idAutor}")
    public ResponseEntity<List<LibroResponse>> buscarLibrosPorAutor(
            @Parameter(description = "ID numérico del autor para filtrar sus obras", example = "1", required = true)
            @PathVariable Long idAutor) {
        log.info("GET /libros/autor/{}", idAutor);
        List<LibroResponse> libros = libroService.buscarLibrosPorAutor(idAutor);
        return ResponseEntity.ok(libros);
    }

    @Operation(summary = "Buscar libros por Categoría", description = "Filtra y retorna una lista de todos los libros catalogados bajo una categoría específica consultada mediante WebClient.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de libros por categoría obtenida con éxito"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Fallo de autenticación"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Acceso denegado")
    })
    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<LibroResponse>> buscarLibrosPorCategoria(
            @Parameter(description = "ID numérico de la categoría para el filtrado", example = "1", required = true)
            @PathVariable Long idCategoria) {
        log.info("GET /libros/categoria/{}", idCategoria);
        List<LibroResponse> libros = libroService.buscarLibrosPorCategoria(idCategoria);
        return ResponseEntity.ok(libros);
    }

    @Operation(summary = "Buscar libros por Género", description = "Filtra y retorna una lista de todos los libros mapeados a un género literario específico consultado mediante WebClient.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de libros por género recuperada con éxito"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Rol no autorizado")
    })
    @GetMapping("/genero/{idGenero}")
    public ResponseEntity<List<LibroResponse>> buscarLibrosPorGenero(
            @Parameter(description = "ID numérico del género literario para el filtro", example = "1", required = true)
            @PathVariable Long idGenero) {
        log.info("GET /libros/genero/{}", idGenero);
        List<LibroResponse> libros = libroService.buscarLibrosPorGenero(idGenero);
        return ResponseEntity.ok(libros);
    }

    @Operation(summary = "Crear un nuevo libro", description = "Registra un nuevo ejemplar en el catálogo. Valida dinámicamente campos obligatorios e integridad relacional.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Libro creado con éxito (Incluye cabecera 'Location' con la URI de acceso)"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta - Payload estructurado inválido o faltan campos obligatorios"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token de seguridad faltante o inválido"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Rol de usuario no cuenta con privilegios de escritura administrativos")
    })
    @PostMapping
    public ResponseEntity<LibroResponse> crearLibro(@Valid @RequestBody LibroRequest libro){
        log.info("POST /libros");
        LibroResponse crearLibro = libroService.crearLibro(libro);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(crearLibro.getId())
                .toUri();
        return ResponseEntity.created(location).body(crearLibro);
    }

    @Operation(summary = "Actualizar un libro existente", description = "Modifica los atributos editables de un ejemplar basándose en su ID y en las propiedades provistas en el cuerpo de la petición.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro modificado y guardado con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta - Fallo en las restricciones de validación del DTO"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Error de autenticación JWT"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Privilegios insuficientes para realizar modificaciones"),
            @ApiResponse(responseCode = "404", description = "No encontrado - No se ubicó ningún libro con el ID suministrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LibroResponse> actualizarLibro(
            @Parameter(description = "ID numérico del libro a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody LibroUpdateRequest libro){
        log.info("PUT /libros/{}", id);
        return ResponseEntity.ok(libroService.actualizarLibro(id,libro));
    }

    @Operation(summary = "Eliminar un libro del catálogo", description = "Remueve físicamente el registro del libro del sistema por medio de su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Libro dado de baja de manera exitosa (Sin contenido de retorno en el cuerpo)"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Operación resguardada por firma perimetral"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Acción exclusiva de roles de administración"),
            @ApiResponse(responseCode = "404", description = "No encontrado - El ID ingresado no coincide con ningún registro")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(
            @Parameter(description = "ID numérico del libro que se desea remover", example = "1", required = true)
            @PathVariable Long id){
        log.info("DELETE /libros/{}", id);
        libroService.eliminarLibro(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Registrar préstamo de libro", description = "Modifica el estado interno del ejemplar disminuyendo su stock disponible o alterando su bandera de disponibilidad.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Préstamo registrado exitosamente; estado del libro actualizado"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Permisos transaccionales denegados"),
            @ApiResponse(responseCode = "404", description = "No encontrado - El ID especificado no pertenece a ningún libro")
    })
    @PatchMapping("/{id}/prestamo")
    public ResponseEntity<LibroResponse> prestarLibro(
            @Parameter(description = "ID numérico del libro que sale en préstamo", example = "1", required = true)
            @PathVariable Long id){
        log.info("PATCH /libros/{}/prestamo", id);
        return ResponseEntity.ok(libroService.prestarLibro(id));
    }

    @Operation(summary = "Registrar devolución de libro", description = "Modifica el estado interno del ejemplar incrementando su stock disponible tras la entrega física de la obra.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devolución registrada exitosamente; estado del libro actualizado"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Error de firma digital"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Acción restringida"),
            @ApiResponse(responseCode = "404", description = "No encontrado - No existe libro con el ID ingresado")
    })
    @PatchMapping("/{id}/devolucion")
    public ResponseEntity<LibroResponse> devolverLibro(
            @Parameter(description = "ID numérico del libro devuelto al inventario", example = "1", required = true)
            @PathVariable Long id){
        log.info("PATCH /libros/{}/devolucion", id);
        return ResponseEntity.ok(libroService.devolverLibro(id));
    }
}