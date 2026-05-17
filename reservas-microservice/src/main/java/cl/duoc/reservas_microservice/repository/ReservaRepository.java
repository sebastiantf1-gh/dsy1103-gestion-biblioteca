package cl.duoc.reservas_microservice.repository;

import cl.duoc.reservas_microservice.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    // 1. Busca todas las reservas de un libro que estén activas
    List<Reserva> findByIdLibroAndEstado(Long idLibro, String estado);

    // 2. Busca si el usuario ya tiene otra reserva activa para EL MISMO libro
    boolean existsByIdUsuarioAndIdLibroAndEstado(Long idUsuario, Long idLibro, String estado);

    // 3. Verifica si las nuevas fechas se cruzan con una reserva existente
    @Query("SELECT COUNT(r) > 0 FROM Reserva r WHERE r.idLibro = :idLibro AND r.estado = 'ACTIVA' " +
            "AND (:fechaInicio <= r.fechaTermino AND :fechaTermino >= r.fechaInicio)")
    boolean existeCruceDeFechas(@Param("idLibro") Long idLibro,
                                @Param("fechaInicio") LocalDate fechaInicio,
                                @Param("fechaTermino") LocalDate fechaTermino);
    // 4. Buscar todas las reservas de un usuario específico
    List<Reserva> findAllByIdUsuario(Long idUsuario);

    // 5. Eliminar (o cancelar) todas las reservas de un usuario
    void deleteAllByIdUsuario(Long idUsuario);
}
