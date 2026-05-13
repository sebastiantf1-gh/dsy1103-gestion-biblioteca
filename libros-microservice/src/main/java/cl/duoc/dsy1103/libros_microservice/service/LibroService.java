package cl.duoc.dsy1103.libros_microservice.service;

import cl.duoc.dsy1103.libros_microservice.client.AutorClient;
import cl.duoc.dsy1103.libros_microservice.dto.AutorResponse;
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

    @Autowired
    private AutorClient autorClient;

    //todos los libros
    public List<LibroResponse> buscarLibros(){
        log.info("Obteniendo todos los libros...");
        return libroRepository.findAll().stream()
                .map(libro -> {
                    AutorResponse autor = autorClient.buscarAutorPorId(libro.getIdAutor());
                    return libroMapper.toResponse(libro, autor);
                })
                .toList();
    }

    //libros por autor
    public List<LibroResponse> buscarLibrosPorAutor(Long idAutor){
        log.info("Obteniendo libros de autor con ID: {} ", idAutor);
        return libroRepository.findByIdAutor(idAutor).stream()
                .map(libro -> {
                    AutorResponse autor = autorClient.buscarAutorPorId(libro.getIdAutor());
                    return libroMapper.toResponse(libro, autor);
                })
                .toList();
    }
    //libro por ID
    public LibroResponse buscarLibroPorId(Long id){
        log.info("Obteniendo libros con ID: {} ",id);
        return libroRepository.findById(id)
                .map(libro -> {
                AutorResponse autor = autorClient.buscarAutorPorId(libro.getIdAutor());
                return libroMapper.toResponse(libro, autor);
                })
                .orElseThrow(()-> new NoSuchElementException("No se encontro libro con ID: " + id));

    }

    //crear libro
    public LibroResponse crearLibro(LibroRequest libroRequest){
        log.info("Creando nuevo libro: {}",libroRequest.getTitulo());
        Libro libro = libroMapper.toEntity(libroRequest);

        AutorResponse autor = autorClient.buscarAutorPorId(libro.getIdAutor());
        if (autor == null){
            log.warn("Autor con ID: {} no encontrado.", libro.getIdAutor());
            throw new NoSuchElementException("No se encontro autor con ID: " + libro.getIdAutor());
        }
        return libroMapper.toResponse(libroRepository.save(libro), autor);
    }

    //actualizar libro
    public LibroResponse actualizarLibro(Long id, LibroUpdateRequest request){
        log.info("Actualizando libro con ID: {}",id);
        Libro libroExistente = libroRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontro libro con ID: "+ id));
        AutorResponse autor = autorClient.buscarAutorPorId(request.getIdAutor());
        if (autor == null){
            log.warn("Autor con ID: {} no encontrado.", request.getIdAutor());
            throw new NoSuchElementException("No se encontro autor con ID: " + request.getIdAutor());
        }
        if (request.getTitulo() != null){
            libroExistente.setTitulo(request.getTitulo());
        }
        if (request.getSinopsis() != null){
            libroExistente.setSinopsis(request.getSinopsis());
        }
        if (request.getNumeroPaginas() != null){
            libroExistente.setNumeroPaginas(request.getNumeroPaginas());
        }
        if (request.getDisponible() != null){
            libroExistente.setDisponible(request.getDisponible());
        }
        if (request.getIdAutor() != null){
            libroExistente.setIdAutor(request.getIdAutor());
        }
        if (request.getIdCategoria() != null){
            libroExistente.setIdCategoria(request.getIdCategoria());
        }
        if (request.getIdGenero() != null){
            libroExistente.setIdGenero(request.getIdGenero());
        }
        Libro libroActualizado = libroRepository.save(libroExistente);
        return libroMapper.toResponse(libroActualizado, autor);
    }

    //eliminar libro
    public void eliminarLibro (Long id){
        log.info("Eliminando libro con ID: {}", id);
        if(!libroRepository.existsById(id)){
            throw new NoSuchElementException("No se encontro libro con ID: "+ id);
        }
        libroRepository.deleteById(id);
    }

    //marcar libro como prestado (no disponible - false)
    public LibroResponse prestarLibro(Long id){
        log.info("Prestando libro con ID: {}", id);
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontro libro con ID: "+ id));
        if(!libro.getDisponible()){
            throw new IllegalStateException("Libro con ID: "+ id + " no esta disponible para prestamo.");
        }
        libro.setDisponible(false);
        Libro libroActualizado = libroRepository.save(libro);
        AutorResponse autor = autorClient.buscarAutorPorId(libroActualizado.getIdAutor());
        return libroMapper.toResponse(libroActualizado, autor);
    }

    //marcar libro como disponible - true
    public LibroResponse devolverLibro(Long id){
        log.info("Devolviendo libro con ID: {}",id);
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontro libro con ID: "+ id));
        if (!libro.getDisponible()){
            throw new IllegalStateException("Libro con ID: "+ id + " no esta prestado en este momento.");
        }
        libro.setDisponible(true);
        Libro libroActualizado = libroRepository.save(libro);
        AutorResponse autor = autorClient.buscarAutorPorId(libroActualizado.getIdAutor());
        return libroMapper.toResponse(libroActualizado, autor);

    }

}
