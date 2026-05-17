package cl.duoc.dsy1103.generos_microservice.mapper;


import cl.duoc.dsy1103.generos_microservice.dto.GeneroRequest;
import cl.duoc.dsy1103.generos_microservice.dto.GeneroResponse;
import cl.duoc.dsy1103.generos_microservice.model.Genero;
import org.springframework.stereotype.Component;


@Component
public class GeneroMapper {

    //Transforma los datos que llegan en una entidad para la base de datos
    public Genero fromRequest(GeneroRequest generoRequest){
        return Genero.builder()
                .nombreGenero(generoRequest.getNombreGenero())
                .descripcion(generoRequest.getDescripcion())
                .build();
    }

    //Convierte la entidad en un response para mostrar los datos necesarios
    public GeneroResponse toResponse(Genero genero){
        return GeneroResponse.builder()
                .id(genero.getId())
                .nombreGenero(genero.getNombreGenero())
                .descripcion(genero.getDescripcion())
                .build();
    }


}
