package cl.duoc.dsy1103.resennas_microservice.repository;

import cl.duoc.dsy1103.resennas_microservice.model.Resenna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResennaRepository extends JpaRepository<Resenna, Long> {

    boolean existsByIdUsuarioAndIdLibro(Long idUsuario, Long idLibro);
}
