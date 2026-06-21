package cl.duoc.dsy1103.generos_microservice.controller;


import cl.duoc.dsy1103.generos_microservice.dto.GeneroRequest;
import cl.duoc.dsy1103.generos_microservice.dto.GeneroResponse;
import cl.duoc.dsy1103.generos_microservice.dto.GeneroUpdateRequest;
import cl.duoc.dsy1103.generos_microservice.service.GeneroService;
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
@RequestMapping("/generos")
@Slf4j
@Tag(name = "Generos", description = "Gestion de generos literarios")
public class GeneroController {

    @Autowired
    private GeneroService generoService;

    @Operation(
            summary = "Agregar un nuevo genero",
            description = "Registra una nueva categoria de genero literario"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Genero creado correctamente",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        examples = @ExampleObject(value = """
                                {
                                 "id": 1,
                                 "nombreGenero": "Didáctico",
                                 "descripcion": "Su propósito es transmitir conocimiento"
                                }
                                """))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos en la solicitud", content = @Content)
    })
    @PostMapping
    public ResponseEntity<GeneroResponse> agregarGenero(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del genero a registrar",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                             "nombreGenero": "Didáctico",
                             "descripcion": "Su propósito es transmitir conocimiento"
                            }
                            """))
            )
            @Valid @RequestBody GeneroRequest generoRequest){
        log.info("Post /generos");
        GeneroResponse generoAgregado = generoService.agregarGenero(generoRequest);
        // Construccion de la URL del nuevo genero
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(generoAgregado.getId())
                .toUri();
        return ResponseEntity.created(location).body(generoAgregado);

    }

    @Operation(
            summary = "Listar todos los generos",
            description = "Retorna la lista completa de generos literarios registrados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de generos obtenida con exito",
                 content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(value = """
                            [
                             {
                               "id": 1,
                               "nombreGenero": "Didáctico",
                               "descripcion": "Su propósito es transmitir conocimiento"
                             }
                            ]
                            """)))
    })
    @GetMapping
    public ResponseEntity<List<GeneroResponse>> listarGeneros(){
        log.info("Get /generos");
        return ResponseEntity.ok(generoService.listarGeneros());
    }

    @Operation(
            summary = "Buscar genero por ID",
            description = "Retorna lo datos de un genero en especifico segun su id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genero encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = """
                                    {
                                     "id": 1,
                                     "nombreGenero": "Didáctico",
                                     "descripcion": "Su propósito es transmitir conocimiento"
                                    }
                                    
                                    """))),
            @ApiResponse(responseCode = "404", description = "Genero no encontrado", content = @Content)

    })
    @GetMapping("/{id}")
    public ResponseEntity<GeneroResponse> buscarGeneroPorId(
            @Parameter(description = "ID del genero a buscar", example = "1")
            @PathVariable Long id){
        log.info("Get /generos/{}", id);
        return ResponseEntity.ok(generoService.buscarGeneroPorId(id));

    }

    @Operation(
            summary = "Modificar un genero existente",
            description = "Actualiza parcialmente los datos de un genero. Solo se modifican los campos enviados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genero modificado correctamente"),
            @ApiResponse(responseCode = "404", description = "Genero no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<GeneroResponse> modificarGenero(
            @Parameter(description = "ID del genero a modificar", example = "1")
            @PathVariable Long id, @Valid @RequestBody GeneroUpdateRequest generoUpdateRequest){
        log.info("Put /generos/{}", id);
        return ResponseEntity.ok(generoService.modificarGenero(id, generoUpdateRequest));

    }

    @Operation(
            summary = "Eliminar un genero",
            description = "Elimina permanentemente un genero del sistema segun su id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Genero eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Genero no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarGenero(
            @Parameter(description = "ID del genero a eliminar", example = "1")
            @PathVariable Long id){
        log.info("Delete /generos/{}", id);
        generoService.eliminarGenero(id);
        return ResponseEntity.noContent().build();

    }
}
