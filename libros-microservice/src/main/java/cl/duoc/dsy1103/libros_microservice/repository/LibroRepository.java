package cl.duoc.dsy1103.libros_microservice.repository;

import cl.duoc.dsy1103.libros_microservice.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findById(Long Id);

    boolean existsById(Long id);

    List<Libro> findByIdAutor(Long idAutor);

}
