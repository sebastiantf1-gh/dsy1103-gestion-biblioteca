package cl.duoc.dsy1103.categorias_microservice.repository;

import cl.duoc.dsy1103.categorias_microservice.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //Registra esta interfaz como un componente de persistencia.
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {
}
