package cl.duoc.dsy1103.libros_microservice.service;

import cl.duoc.dsy1103.libros_microservice.client.AutorClient;
import cl.duoc.dsy1103.libros_microservice.client.CategoriaClient;
import cl.duoc.dsy1103.libros_microservice.dto.*;
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

    @Autowired
    private CategoriaClient categoriaClient;

    //todos los libros
    public List<LibroResponse> buscarLibros(){
        log.info("Obteniendo todos los libros...");
        return libroRepository.findAll().stream()
                .map(libro -> {
                    AutorResponse autor = autorClient.buscarAutorPorId(libro.getIdAutor());
                    CategoriaResponse categoria = categoriaClient.buscarCategoriaPorId(libro.getIdCategoria());
                    return libroMapper.toResponse(libro, autor, categoria);
                })
                .toList();
    }

    //libros por autor
    public List<LibroResponse> buscarLibrosPorAutor(Long idAutor){
        log.info("Obteniendo libros de autor con ID: {} ", idAutor);
        return libroRepository.findByIdAutor(idAutor).stream()
                .map(libro -> {
                    AutorResponse autor = autorClient.buscarAutorPorId(libro.getIdAutor());
                    CategoriaResponse categoria = categoriaClient.buscarCategoriaPorId(libro.getIdCategoria());
                    return libroMapper.toResponse(libro, autor, categoria);
                })
                .toList();
    }
    //libro por ID
    public LibroResponse buscarLibroPorId(Long id){
        log.info("Obteniendo libros con ID: {} ",id);
        return libroRepository.findById(id)
                .map(libro -> {
                AutorResponse autor = autorClient.buscarAutorPorId(libro.getIdAutor());
                CategoriaResponse categoria = categoriaClient.buscarCategoriaPorId(libro.getIdCategoria());
                return libroMapper.toResponse(libro, autor, categoria);
                })
                .orElseThrow(()-> new NoSuchElementException("No se encontro libro con ID: " + id));

    }

    //crear libro
    public LibroResponse crearLibro(LibroRequest libroRequest){
        log.info("Creando nuevo libro: {}",libroRequest.getTitulo());
        Libro libro = libroMapper.toEntity(libroRequest);

        AutorResponse autor = autorClient.buscarAutorPorId(libro.getIdAutor());
        CategoriaResponse categoria = categoriaClient.buscarCategoriaPorId(libro.getIdCategoria());
        if (autor == null || categoria == null){
            log.warn("Intento de creación de libro fallido. Autor ID: {} o Categoría ID: {} no encontrados.",
                    libro.getIdAutor(), libro.getIdCategoria());
            throw new NoSuchElementException("No se pudo crear el libro porque el autor o la categoría especificados no existen.");
        }
        return libroMapper.toResponse(libroRepository.save(libro), autor, categoria);
    }

    //actualizar libro
    public LibroResponse actualizarLibro(Long id, LibroUpdateRequest request){
        log.info("Actualizando libro con ID: {}",id);
        Libro libroExistente = libroRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontro libro con ID: "+ id));

        //verificar si se usa IDs nuevos o los existentes
        Long idAutorFinal = (request.getIdAutor() != null) ? request.getIdAutor() : libroExistente.getIdAutor();
        Long idCatFinal = (request.getIdCategoria() != null) ? request.getIdCategoria() : libroExistente.getIdCategoria();

        AutorResponse autor = autorClient.buscarAutorPorId(idAutorFinal);
        CategoriaResponse categoria = categoriaClient.buscarCategoriaPorId(idCatFinal);

        if (autor == null || categoria == null){
            log.warn("Intento de actualizacion de libro fallido. Autor ID: {} o Categoría ID: {} no encontrados.",
                    idAutorFinal, idCatFinal);
            throw new NoSuchElementException("No se pudo actualizar el libro porque el autor o la categoría especificados no existen.");
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
            libroExistente.setIdAutor(idAutorFinal);
        }
        if (request.getIdCategoria() != null){
            libroExistente.setIdCategoria(idCatFinal);
        }
        if (request.getIdGenero() != null){
            libroExistente.setIdGenero(request.getIdGenero());
        }
        Libro libroActualizado = libroRepository.save(libroExistente);
        return libroMapper.toResponse(libroActualizado, autor, categoria);
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
        CategoriaResponse categoria = categoriaClient.buscarCategoriaPorId(libroActualizado.getIdCategoria());
        return libroMapper.toResponse(libroActualizado, autor, categoria);
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
        CategoriaResponse categoria = categoriaClient.buscarCategoriaPorId(libroActualizado.getIdCategoria());
        return libroMapper.toResponse(libroActualizado, autor, categoria);

    }

}
