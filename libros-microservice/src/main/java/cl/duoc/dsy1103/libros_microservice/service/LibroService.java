package cl.duoc.dsy1103.libros_microservice.service;

import cl.duoc.dsy1103.libros_microservice.dto.LibroRequest;
import cl.duoc.dsy1103.libros_microservice.dto.LibroResponse;
import cl.duoc.dsy1103.libros_microservice.dto.LibroUpdateRequest;
import cl.duoc.dsy1103.libros_microservice.mapper.LibroMapper;
import cl.duoc.dsy1103.libros_microservice.model.Libro;
import cl.duoc.dsy1103.libros_microservice.repository.LibroRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class LibroService {
    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private LibroMapper libroMapper;

    public List<LibroResponse> buscarLibros(){
        log.info("Obteniendo todos los libros...");
        List<Libro> libros = libroRepository.findAll();
        return libros.stream()
                .map(libroMapper::toResponse)
                .toList();
    }

    public LibroResponse buscarLibroPorId(Long id){
        log.info("Obteniendo libros con ID: {} ",id);
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontro libro con ID: "+ id));
        return libroMapper.toResponse(libro);
    }

    public LibroResponse crearLibro (LibroRequest libroRequest){
        log.info("Creando nuevo libro: {}",libroRequest.getTitulo());
        Libro libro = libroRepository.save(libroMapper.toEntity(libroRequest));
        return libroMapper.toResponse(libro);
    }

    public LibroResponse actualizarLibro(Long id, LibroUpdateRequest request){
        log.info("Actualizando libro con ID: {}",id);
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontro libro con ID: "+ id));

        if(request.getTitulo() != null){
            libro.setTitulo(request.getTitulo());
        }
        if(request.getSinopsis() != null){
            libro.setSinopsis(request.getSinopsis());
        }
        if(request.getNumeroPaginas() != null){
            libro.setNumeroPaginas(request.getNumeroPaginas());
        }
        if(request.getDisponible() != null){
            libro.setDisponible(request.getDisponible());
        }
        if(request.getIdAutor() != null){
            libro.setIdAutor(request.getIdAutor());
        }
        if(request.getIdCategoria() != null){
            libro.setIdCategoria(request.getIdCategoria());
        }
        if(request.getIdGenero() != null){
            libro.setIdGenero(request.getIdGenero());
        }
        libro = libroRepository.save(libro);
        return libroMapper.toResponse(libro);

    }

    public void eliminarLibro (Long id){
        log.info("Eliminando libro con ID: {}", id);
        if(!libroRepository.existsById(id)){
            throw new NoSuchElementException("No se encontro libro con ID: "+ id);
        }
        libroRepository.deleteById(id);
    }

}
