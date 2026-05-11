package cl.duoc.dsy1103.categorias_microservice.mapper;

import cl.duoc.dsy1103.categorias_microservice.dto.CategoriaRequest;
import cl.duoc.dsy1103.categorias_microservice.dto.CategoriaResponse;
import cl.duoc.dsy1103.categorias_microservice.model.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {
    public Categoria toEntity(CategoriaRequest request){
        return Categoria.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .idLibro(request.getIdLibro())
                .build();
    }
    public CategoriaResponse toResponse(Categoria categoria){
        return CategoriaResponse.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .idLibro(categoria.getIdLibro())
                .build();
    }
}
