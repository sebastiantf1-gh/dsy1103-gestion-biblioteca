package cl.duoc.dsy1103.generos_microservice.mapper;


import cl.duoc.dsy1103.generos_microservice.dto.GeneroRequest;
import cl.duoc.dsy1103.generos_microservice.dto.GeneroResponse;
import cl.duoc.dsy1103.generos_microservice.model.Genero;
import org.springframework.stereotype.Component;
import tools.jackson.core.base.GeneratorBase;

@Component
public class GeneroMapper {

    public Genero fromRequest(GeneroRequest generoRequest){
        return Genero.builder()
                .nombreGenero(generoRequest.getNombreGenero())
                .descripcion(generoRequest.getDescripcion())
                .build();
    }

    public GeneroResponse toRespone(Genero genero){
        return GeneroResponse.builder()
                .idGenero(genero.getIdGenero())
                .nombreGenero(genero.getNombreGenero())
                .descripcion(genero.getDescripcion())
                .build();
    }


}
