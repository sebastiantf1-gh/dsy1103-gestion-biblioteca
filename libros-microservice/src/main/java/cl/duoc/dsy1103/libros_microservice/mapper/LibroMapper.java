package cl.duoc.dsy1103.libros_microservice.mapper;

import cl.duoc.dsy1103.libros_microservice.dto.AutorResponse;
import cl.duoc.dsy1103.libros_microservice.dto.LibroRequest;
import cl.duoc.dsy1103.libros_microservice.dto.LibroResponse;
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
    public LibroResponse toResponse(Libro libro, AutorResponse autor){
        return LibroResponse.builder()
                .id(libro.getId())
                .isbn(libro.getIsbn())
                .titulo(libro.getTitulo())
                .sinopsis(libro.getSinopsis())
                .numeroPaginas(libro.getNumeroPaginas())
                .disponible(libro.getDisponible())
                .fechaRegistro(libro.getFechaRegistro())
                .autor(autor)
                .idCategoria(libro.getIdCategoria())
                .idGenero(libro.getIdGenero())
                .build();
    }
}
