package cl.duoc.dsy1103.prestamos_microservice.repository;

import cl.duoc.dsy1103.prestamos_microservice.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    // Buscar todos los préstamos asociados a un usuario
    List<Prestamo> findByIdUsuario(Long idUsuario);

    // Buscar todos los préstamos asociados a un libro
    List<Prestamo> findByIdLibro(Long idLibro);

    // Buscar por estado(ej: obtener solo los activos)
    List<Prestamo> findByEstado(String estado);
}
