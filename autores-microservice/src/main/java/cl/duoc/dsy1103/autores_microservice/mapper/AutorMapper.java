package cl.duoc.dsy1103.autores_microservice.mapper;

import cl.duoc.dsy1103.autores_microservice.dto.AutorRequest;
import cl.duoc.dsy1103.autores_microservice.dto.AutorResponse;
import cl.duoc.dsy1103.autores_microservice.model.Autor;
import org.springframework.stereotype.Component;

@Component
public class AutorMapper {
    public Autor toEntity(AutorRequest request){
        return Autor.builder()
                .nombreCompleto(request.getNombreCompleto())
                .biografia(request.getBiografia())
                .nacionalidad(request.getNacionalidad())
                .fechaNacimiento(request.getFechaNacimiento())
                .build();
    }

    public AutorResponse toResponse(Autor autor){
        return AutorResponse.builder()
                .id(autor.getId())
                .nombreCompleto(autor.getNombreCompleto())
                .biografia(autor.getBiografia())
                .nacionalidad(autor.getNacionalidad())
                .fechaNacimiento(autor.getFechaNacimiento())
                .fechaRegistro(autor.getFechaRegistro())
                .build();
    }
}
