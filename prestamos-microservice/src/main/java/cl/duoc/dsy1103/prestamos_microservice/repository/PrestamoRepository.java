package cl.duoc.dsy1103.prestamos_microservice.repository;

import cl.duoc.dsy1103.prestamos_microservice.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
}
