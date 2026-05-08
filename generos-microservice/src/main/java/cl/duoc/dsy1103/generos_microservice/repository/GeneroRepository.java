package cl.duoc.dsy1103.generos_microservice.repository;

import cl.duoc.dsy1103.generos_microservice.model.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Long> {
}
