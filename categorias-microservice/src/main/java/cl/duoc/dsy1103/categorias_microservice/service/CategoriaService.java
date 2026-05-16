package cl.duoc.dsy1103.categorias_microservice.service;

import cl.duoc.dsy1103.categorias_microservice.dto.CategoriaRequest;
import cl.duoc.dsy1103.categorias_microservice.dto.CategoriaResponse;
import cl.duoc.dsy1103.categorias_microservice.dto.CategoriaUpdateRequest;
import cl.duoc.dsy1103.categorias_microservice.mapper.CategoriaMapper;
import cl.duoc.dsy1103.categorias_microservice.model.Categoria;
import cl.duoc.dsy1103.categorias_microservice.repository.CategoriaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaMapper categoriaMapper;

    public List<CategoriaResponse> buscarCategorias(){
        log.info("Obteniendo todas las categorias con sus libros...");
        return categoriaRepository.findAll().stream()
                .map(categoria -> {
                    return categoriaMapper.toResponse(categoria);
                })
                .toList();
    }

    public CategoriaResponse buscarCategoriaPorId(Long id){
        log.info("Obteniendo categoria con ID: {}", id);
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("No se encontro categoria con ID: " + id));
        return categoriaMapper.toResponse(categoria);
    }

    public CategoriaResponse crearCategoria (CategoriaRequest categoriaRequest){
        log.info("Creando nueva categoria: {}", categoriaRequest.getNombre());
        Categoria categoria = categoriaRepository.save(categoriaMapper.toEntity(categoriaRequest));
        return categoriaMapper.toResponse(categoria);
    }

    public CategoriaResponse actualizarCategoria(Long id, CategoriaUpdateRequest request){
        log.info("Actualizando categoria con ID: {}", id);
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontro categoria con ID: "+ id));

        if(request.getNombre() != null){
            categoria.setNombre(request.getNombre());
        }
        if(request.getDescripcion() != null){
            categoria.setDescripcion(request.getDescripcion());
        }
        Categoria categoriaActualizada = categoriaRepository.save(categoria);
         return categoriaMapper.toResponse(categoriaActualizada);
    }

    public void eliminarCategoria(Long id){
        log.info("Eliminando categoria con ID: {}", id);
        if(!categoriaRepository.existsById(id)){
            throw new NoSuchElementException("No se encontro categoria con ID: "+ id);
        }
        categoriaRepository.deleteById(id);
    }

}
