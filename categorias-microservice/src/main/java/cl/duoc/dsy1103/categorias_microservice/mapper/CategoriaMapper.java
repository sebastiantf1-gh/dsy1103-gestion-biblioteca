package cl.duoc.dsy1103.categorias_microservice.mapper;

import cl.duoc.dsy1103.categorias_microservice.dto.CategoriaRequest;
import cl.duoc.dsy1103.categorias_microservice.dto.CategoriaResponse;
import cl.duoc.dsy1103.categorias_microservice.dto.LibroResponse;
import cl.duoc.dsy1103.categorias_microservice.model.Categoria;
import org.springframework.stereotype.Component;

import java.util.List;

@Component //Registra la clase como un Bean de mapeo. Esto permite inyectarlo limpiamente mediante @Autowired en el CategoriaService,
            // centralizando la responsabilidad de conversión estructural.
public class CategoriaMapper {
    public Categoria toEntity(CategoriaRequest request){
        return Categoria.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .build();
    }
    public CategoriaResponse toResponse(Categoria categoria){
        return CategoriaResponse.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .build();
    }
}
