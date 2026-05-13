package cl.duoc.dsy1103.libros_microservice.mapper;

import cl.duoc.dsy1103.libros_microservice.dto.*;
import cl.duoc.dsy1103.libros_microservice.model.Libro;
import org.springframework.stereotype.Component;

@Component
public class LibroMapper {
    public Libro toEntity(LibroRequest request){
        return Libro.builder()
                .isbn(request.getIsbn())
                .titulo(request.getTitulo())
                .sinopsis(request.getSinopsis())
                .numeroPaginas(request.getNumeroPaginas())
                .disponible(request.getDisponible())
                .idAutor(request.getIdAutor())
                .idCategoria(request.getIdCategoria())
                .idGenero(request.getIdGenero())
                .build();
    }
    public LibroResponse toResponse(Libro libro, AutorResponse autor, CategoriaResponse categoria, GeneroResponse genero){
        return LibroResponse.builder()
                .id(libro.getId())
                .isbn(libro.getIsbn())
                .titulo(libro.getTitulo())
                .sinopsis(libro.getSinopsis())
                .numeroPaginas(libro.getNumeroPaginas())
                .disponible(libro.getDisponible())
                .fechaRegistro(libro.getFechaRegistro())
                .autor(autor)
                .categoria(categoria)
                .genero(genero)
                .build();
    }
}
