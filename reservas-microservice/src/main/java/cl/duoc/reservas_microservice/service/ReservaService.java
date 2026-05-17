package cl.duoc.reservas_microservice.service;

import cl.duoc.reservas_microservice.client.LibroClient;
import cl.duoc.reservas_microservice.client.UsuarioClient;
import cl.duoc.reservas_microservice.dto.CrearReservaRequest;
import cl.duoc.reservas_microservice.dto.LibroResponse;
import cl.duoc.reservas_microservice.dto.ReservaResponse;
import cl.duoc.reservas_microservice.dto.UsuarioResponse;
import cl.duoc.reservas_microservice.exception.UnauthorizedException;
import cl.duoc.reservas_microservice.mapper.ReservaMapper;
import cl.duoc.reservas_microservice.model.Reserva;
import cl.duoc.reservas_microservice.repository.ReservaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Clase que proporciona los servicios para la gestión de reservas.
 */
@Service
@Transactional
@Slf4j
public class ReservaService {
    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ReservaMapper reservaMapper;

    @Autowired
    private UsuarioClient usuarioClient;

    @Autowired
    private LibroClient libroClient;

    /**
     * Crea una nueva reserva verificando la existencia del usuario,
     * la disponibilidad del libro y garantizando que las fechas no se solapen.
     *
     * @param request Datos de la solicitud de reserva.
     * @param token Token JWT de autorización para llamadas remotas.
     * @return Los detalles de la reserva creada.
     */
    public ReservaResponse crearReserva(CrearReservaRequest request, String token) {
        log.info("Iniciando proceso de reserva para el Usuario ID: {} y Libro ID: {}",
                request.getIdUsuario(), request.getIdLibro());

        // =========================================================================
        // NUEVA VALIDACIÓN DE SEGURIDAD: Uso de UnauthorizedException (401)
        // =========================================================================
        if (token == null || token.trim().isEmpty() || !token.startsWith("Bearer ")) {
            log.error("Validación de seguridad fallida: Token ausente o mal estructurado.");
            throw new UnauthorizedException("El token de autorización es requerido o no tiene el formato Bearer válido.");
        }

        // 1. Consultar al microservicio de usuarios para verificar existencia
        UsuarioResponse usuarioResponse;
        try {
            usuarioResponse = usuarioClient.obtenerUsuarioPorId(request.getIdUsuario(), token);
            log.info("Usuario verificado de forma remota ID: {}", usuarioResponse.getIdUsuario());
        } catch (NoSuchElementException e) {
            log.warn("Validación remota fallida: El usuario ID {} no existe.", request.getIdUsuario());
            throw new NoSuchElementException("No se puede generar la reserva. El usuario no existe.");
        }

        // 2. Consultar al microservicio de libros para verificar existencia y disponibilidad
        LibroResponse libroResponse;
        try {
            libroResponse = libroClient.obtenerLibroPorId(request.getIdLibro(), token);
            log.info("Libro verificado de forma remota ID: {}", libroResponse.getIdLibro());
        } catch (NoSuchElementException e) {
            log.warn("Validación remota fallida: El libro ID {} no existe.", request.getIdLibro());
            throw new NoSuchElementException("No se puede generar la reserva. El libro seleccionado no existe.");
        }

        // 3. Regla de negocio: Verificar que el libro esté habilitado físicamente
        if (Boolean.FALSE.equals(libroResponse.getDisponible())) {
            log.warn("El libro ID {} existe pero está marcado como no disponible.", request.getIdLibro());
            throw new IllegalArgumentException("El libro seleccionado no se encuentra disponible para nuevas solicitudes.");
        }

        // 4. Regla de negocio: Evitar reservas duplicadas paralelas del mismo usuario y libro
        boolean yaTieneReserva = reservaRepository.existsByIdUsuarioAndIdLibroAndEstado(
                request.getIdUsuario(), request.getIdLibro(), "ACTIVA");
        if (yaTieneReserva) {
            log.warn("El usuario ID {} ya cuenta con una reserva activa para el libro ID {}",
                    request.getIdUsuario(), request.getIdLibro());
            throw new IllegalArgumentException("Ya posees una reserva activa para este mismo libro.");
        }

        // 5. Regla de negocio principal: Verificar solapamiento de fechas (garantía de tiempo)
        boolean hayCruce = reservaRepository.existeCruceDeFechas(
                request.getIdLibro(), request.getFechaInicio(), request.getFechaTermino());

        if (hayCruce) {
            log.warn("Conflicto de fechas detectado para el libro ID {} entre {} y {}",
                    request.getIdLibro(), request.getFechaInicio(), request.getFechaTermino());
            throw new IllegalArgumentException("El periodo de tiempo solicitado se encuentra reservado o protegido por garantía de otro usuario.");
        }

        // 6. Mapear y guardar la reserva en la base de datos
        Reserva reserva = reservaMapper.toEntity(request);
        Reserva savedReserva = reservaRepository.save(reserva);
        log.info("Reserva guardada con éxito en la base de datos. Asignado ID: {}", savedReserva.getIdReserva());

        // 7. Convertir el resultado a Response y devolverlo
        return reservaMapper.toResponse(savedReserva);
    }
    /**
     * Obtiene todas las reservas registradas en el sistema.
     */
    public List<ReservaResponse> obtenerTodasLasReservas() {
        log.info("Obteniendo listado global de todas las reservas");
        return reservaRepository.findAll().stream()
                .map(reservaMapper::toResponse)
                .toList();
    }

    /**
     * Obtiene todas las reservas asociadas a un usuario específico.
     */
    public List<ReservaResponse> obtenerReservasPorUsuarioId(Long idUsuario) {
        log.info("Obteniendo reservas para el usuario ID: {}", idUsuario);
        return reservaRepository.findAllByIdUsuario(idUsuario)
                .stream()
                .map(reservaMapper::toResponse)
                .toList();
    }

    /**
     * Elimina una reserva específica por su ID.
     */
    public void eliminarReserva(Long id) {
        log.info("Eliminando reserva con ID: {}", id);
        if (!reservaRepository.existsById(id)) {
            throw new NoSuchElementException("No se encontró la reserva con ID: " + id);
        }
        reservaRepository.deleteById(id);
    }

    /**
     * Elimina todas las reservas de un usuario por su ID.
     */
    public void eliminarReservasPorUsuarioId(Long idUsuario) {
        log.info("Eliminando todas las reservas del usuario ID: {}", idUsuario);
        reservaRepository.deleteAllByIdUsuario(idUsuario);
    }
}
