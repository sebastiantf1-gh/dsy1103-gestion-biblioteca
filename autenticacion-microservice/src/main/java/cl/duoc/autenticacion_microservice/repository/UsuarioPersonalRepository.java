package cl.duoc.autenticacion_microservice.repository;

import cl.duoc.autenticacion_microservice.model.UsuarioPersonal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioPersonalRepository extends JpaRepository<UsuarioPersonal, Long> {
    Optional<UsuarioPersonal> findByNombreUsuario(String nombreUsuario);

    // Para validar antes de registrar
    boolean existsByNombreUsuario(String nombreUsuario);
    boolean existsByEmail(String email);
}
