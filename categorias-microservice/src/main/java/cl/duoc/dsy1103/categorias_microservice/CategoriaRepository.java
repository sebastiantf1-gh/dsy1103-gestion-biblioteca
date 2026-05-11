package cl.duoc.dsy1103.categorias_microservice;

import cl.duoc.dsy1103.categorias_microservice.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {
    Optional<Categoria> findById(Long id);

    boolean existsById(Long id);
}
