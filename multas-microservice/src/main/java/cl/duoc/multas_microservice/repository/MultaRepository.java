package cl.duoc.multas_microservice.repository;

import cl.duoc.multas_microservice.model.Multa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MultaRepository extends JpaRepository<Multa, Long> {
    List<Multa> findByIdUsuario(Long idUsuario);
    List<Multa> findByIdPrestamo(Long idPrestamo);
}
