package cl.duoc.dsy1103.autores_microservice.repository;

import cl.duoc.dsy1103.autores_microservice.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepository extends JpaRepository<Autor,Long> {
}
