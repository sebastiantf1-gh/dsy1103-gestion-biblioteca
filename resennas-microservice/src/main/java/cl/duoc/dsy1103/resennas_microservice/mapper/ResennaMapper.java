package cl.duoc.dsy1103.resennas_microservice.mapper;

import cl.duoc.dsy1103.resennas_microservice.dto.LibroResponse;
import cl.duoc.dsy1103.resennas_microservice.dto.ResennaRequest;
import cl.duoc.dsy1103.resennas_microservice.dto.ResennaResponse;
import cl.duoc.dsy1103.resennas_microservice.dto.UsuarioResponse;
import cl.duoc.dsy1103.resennas_microservice.model.Resenna;
import org.springframework.stereotype.Component;

@Component
public class ResennaMapper {

    public Resenna fromRequest(ResennaRequest resennaRequest) {
        return Resenna.builder()
                .calificacion(resennaRequest.getCalificacion())
                .descripcion(resennaRequest.getDescripcion())
                .idLibro(resennaRequest.getIdLibro())
                .idUsuario(resennaRequest.getIdUsuario())
                .build();
    }

    public ResennaResponse toResponse(Resenna resenna, LibroResponse libroResponse, UsuarioResponse usuarioResponse) {
        return ResennaResponse.builder()
                .id(resenna.getId())
                .tituloLibro(libroResponse.getTitulo())
                .nombreDeUsuario(usuarioResponse.getNombreCompleto())
                .calificacion(resenna.getCalificacion())
                .descripcion(resenna.getDescripcion())
                .fechaRegistro(resenna.getFechaRegistro())
                .build();
    }

    public ResennaResponse toResponse(Resenna resenna) {
        if (resenna == null) return null;

        return ResennaResponse.builder()
                .id(resenna.getId())
                .tituloLibro(null)
                .nombreDeUsuario(null)
                .calificacion(resenna.getCalificacion())
                .descripcion(resenna.getDescripcion())
                .fechaRegistro(resenna.getFechaRegistro())
                .build();
    }
}