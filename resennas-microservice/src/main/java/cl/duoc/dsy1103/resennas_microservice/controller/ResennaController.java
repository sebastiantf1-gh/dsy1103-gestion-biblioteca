package cl.duoc.dsy1103.resennas_microservice.controller;

import cl.duoc.dsy1103.resennas_microservice.dto.ResennaRequest;
import cl.duoc.dsy1103.resennas_microservice.dto.ResennaResponse;
import cl.duoc.dsy1103.resennas_microservice.dto.ResennaUpdateRequest;
import cl.duoc.dsy1103.resennas_microservice.service.ResennaService;
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
@RequestMapping("/resennas")
@Slf4j
@Tag(name = "Resennas", description = "Gestión de resennas")
public class ResennaController {

    @Autowired
    private ResennaService resennaService;

    @Operation(
            summary = "Buscar resenna por ID",
            description = "Retorna los datos de una resenna especifica segun su Id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resenna encontrada",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "tituloLibro": "Cien años de soledad",
                                      "nombreDeUsuario": "Juan Pérez",
                                      "descripcion": "Excelente libro",
                                      "calificacion": 5,
                                      "fechaRegistro": "2026-06-19T10:00:00"
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "Resenna no encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResennaResponse> buscarResennaPorId(
            @Parameter(description = "ID de la resenna a buscar", example = "1")
            @PathVariable Long id){
        log.info("Get /resennas/{}",id);
        return ResponseEntity.ok(resennaService.buscarResennaPorId(id));
    }

    @Operation(
            summary = "Crear una nueva resenna",
            description = "Registra una nueva resenna sobre un libro, validando que el usuario y el libro existan y que no exista una resenna previa del mismo usuario para ese libro."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resenna creada correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "tituloLibro": "Cien años de soledad",
                                      "nombreDeUsuario": "Juan Pérez",
                                      "descripcion": "Excelente libro",
                                      "calificacion": 5,
                                      "fechaRegistro": "2026-06-19T10:00:00"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos en la solicitud", content = @Content),
            @ApiResponse(responseCode = "404", description = "El usuario o el libro especificado no existe", content = @Content),
            @ApiResponse(responseCode = "409", description = "El usuario ya hizo una resenna para este libro", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ResennaResponse> crearResenna(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la resenna a registrar",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                              "descripcion": "Excelente libro",
                              "calificacion": 5,
                              "idUsuario": 1,
                              "idLibro": 1
                            }
                            """)))
            @Valid @RequestBody ResennaRequest resennaRequest){
        log.info("Post /resennas");
        ResennaResponse crearResenna = resennaService.crearResenna(resennaRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(crearResenna.getId())
                .toUri();
        return ResponseEntity.created(location).body(crearResenna);
    }

    @Operation(
            summary = "Listar todas las resennas",
            description = "Retorna la lista completa de resennas registradas en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de resennas obtenida correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = """
                                    [
                                      {
                                        "id": 1,
                                        "tituloLibro": "Cien años de soledad",
                                        "nombreDeUsuario": "Juan Pérez",
                                        "descripcion": "Excelente libro",
                                        "calificacion": 5,
                                        "fechaRegistro": "2026-06-19T10:00:00"
                                      }
                                    ]
                                    """)))
    })
    @GetMapping
    public ResponseEntity<List<ResennaResponse>>listarResennas(){
        log.info("Get /resennas");
        return ResponseEntity.ok(resennaService.listarResennas());
    }

    @Operation(
            summary = "Modificar una resenna existente",
            description = "Actualiza parcialmente los datos de una resenna. Solo se modifican los campos enviados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resenna modificada correctamente"),
            @ApiResponse(responseCode = "404", description = "Resenna no encontrada", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResennaResponse> modificarResenna(
            @Parameter(description = "ID de la resenna a modificar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ResennaUpdateRequest resennaUpdateRequest){
        log.info("Put /resennas/{}", id);
        return ResponseEntity.ok(resennaService.modificarResenna(id,resennaUpdateRequest));
    }

    @Operation(
            summary = "Eliminar una resenna",
            description = "Elimina permanentemente una resenna del sistema segun su id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Resenna eliminada correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Resenna no encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResenna(
            @Parameter(description = "ID de la resenna a eliminar", example = "1")
            @PathVariable Long id){
        log.info("Delete /resennas/{}", id);
        resennaService.eliminarResenna(id);
        return ResponseEntity.noContent().build();
    }
}