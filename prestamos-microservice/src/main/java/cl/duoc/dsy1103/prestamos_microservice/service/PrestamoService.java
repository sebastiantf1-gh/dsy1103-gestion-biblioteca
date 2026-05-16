package cl.duoc.dsy1103.prestamos_microservice.service;

import cl.duoc.dsy1103.prestamos_microservice.client.LibroClient;
import cl.duoc.dsy1103.prestamos_microservice.client.UsuarioClient;
import cl.duoc.dsy1103.prestamos_microservice.dto.LibroResponse;
import cl.duoc.dsy1103.prestamos_microservice.dto.PrestamoRequest;
import cl.duoc.dsy1103.prestamos_microservice.dto.PrestamoResponse;
import cl.duoc.dsy1103.prestamos_microservice.dto.UsuarioResponse;
import cl.duoc.dsy1103.prestamos_microservice.mapper.PrestamoMapper;
import cl.duoc.dsy1103.prestamos_microservice.model.Prestamo;
import cl.duoc.dsy1103.prestamos_microservice.repository.PrestamoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private LibroClient libroClient;

    @Autowired
    private UsuarioClient usuarioClient;

    @Autowired
    private PrestamoMapper prestamoMapper;

    public PrestamoResponse obtenerPrestamoPorId(Long id) {
        log.info("Buscando préstamo por ID: {}", id);

        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Préstamo no encontrado con el ID: " + id));

        UsuarioResponse usuario = usuarioClient.buscarUsuarioPorId(prestamo.getIdUsuario());
        LibroResponse libro = libroClient.buscarLibroPorId(prestamo.getIdLibro());

        return prestamoMapper.toResponse(prestamo, usuario, libro);
    }

    //listar todos los préstamos
    public List<PrestamoResponse> listarTodosLosPrestamos() {
        log.info("Obteniendo listado completo de préstamos...");

        return prestamoRepository.findAll().stream()
                .map(prestamo -> {
                    UsuarioResponse usuario = usuarioClient.buscarUsuarioPorId(prestamo.getIdUsuario());
                    LibroResponse libro = libroClient.buscarLibroPorId(prestamo.getIdLibro());
                    return prestamoMapper.toResponse(prestamo, usuario, libro);
                })
                .toList();
    }

    //crear préstamo
    public PrestamoResponse crearPrestamo(PrestamoRequest request) {
        log.info("Iniciando creación de prestamo para Usuario ID: {} y Libro ID: {}",
                request.getIdUsuario(), request.getIdLibro());

        //validar usuario
        UsuarioResponse usuario = usuarioClient.buscarUsuarioPorId(request.getIdUsuario());
        if (usuario == null || usuario.getNombreCompleto().contains("(Eliminado)")) {
            throw new NoSuchElementException("No se puede realizar el prestamo: El usuario no existe.");
        }
        //validar libro
        LibroResponse libro = libroClient.buscarLibroPorId(request.getIdLibro());
        if (libro == null|| libro.getTitulo().contains("(Eliminado)")) {
            throw new NoSuchElementException("No se puede realizar el prestamo: El libro no existe.");
        }
        // marcar libro como prestado (patch)
        libroClient.marcarComoPrestado(request.getIdLibro());
        //mapear
        Prestamo prestamo = prestamoMapper.toEntity(request);
        prestamo.setEstado(request.getEstado().toLowerCase());
        //guardar en bd
        Prestamo prestamoGuardado = prestamoRepository.save(prestamo);
        log.info("Prestamo registrado exitosamente con ID: {}", prestamoGuardado.getId());

        return prestamoMapper.toResponse(prestamoGuardado, usuario, libro);//**
    }

    //devolver libro
    public PrestamoResponse devolverLibro(Long idPrestamo) {
        log.info("Procesando devolución para el prestamo ID: {}", idPrestamo);

        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new NoSuchElementException("Prestamo no encontrado."));

        if ("devuelto".equals(prestamo.getEstado())) {
            throw new IllegalStateException("El prestamo ya fue cerrado anteriormente.");
        }

        //marcar libro como devuelto
        libroClient.marcarComoDevuelto(prestamo.getIdLibro());

        //actualizar estado a devuelto
        prestamo.setEstado("devuelto");
        prestamo.setFechaDevolucion(LocalDateTime.now());
        //guardar en bd
        Prestamo prestamoCerrado = prestamoRepository.save(prestamo);

        //obtener datos necesarios para el response
        UsuarioResponse usuario = usuarioClient.buscarUsuarioPorId(prestamoCerrado.getIdUsuario());
        LibroResponse libro = libroClient.buscarLibroPorId(prestamoCerrado.getIdLibro());

        return prestamoMapper.toResponse(prestamoCerrado, usuario, libro);
    }

    //historial de prestamos por usuario
    public List<PrestamoResponse> historialPorUsuario(Long idUsuario) {
        log.info("Consultando historial de préstamos para el usuario ID: {}", idUsuario);

        UsuarioResponse usuario = usuarioClient.buscarUsuarioPorId(idUsuario);
        if (usuario == null) {
            throw new NoSuchElementException("Usuario no encontrado.");
        }

        return prestamoRepository.findByIdUsuario(idUsuario).stream()
                .map(prestamo -> {
                    LibroResponse libro = libroClient.buscarLibroPorId(prestamo.getIdLibro());
                    return prestamoMapper.toResponse(prestamo, usuario, libro);
                })
                .toList();
    }
    //historial de un libro específico
    public List<PrestamoResponse> obtenerHistorialPorLibro(Long idLibro) {
        log.info("Consultando historial de préstamos para el libro ID: {}", idLibro);

        //datos del libro
        LibroResponse libro = libroClient.buscarLibroPorId(idLibro);
        if (libro == null) {
            throw new NoSuchElementException("Libro no encontrado.");
        }

        return prestamoRepository.findByIdLibro(idLibro).stream()
                .map(prestamo -> {
                    // Por cada préstamo, se consulta quien fue el usuario
                    UsuarioResponse usuario = usuarioClient.buscarUsuarioPorId(prestamo.getIdUsuario());
                    return prestamoMapper.toResponse(prestamo, usuario, libro);
                })
                .toList();
    }

    //préstamos por estado (ej: ver todos los libros actualmente prestados)
    public List<PrestamoResponse> listarPrestamosPorEstado(String estado) {
        log.info("Listando todos los préstamos con estado: {}", estado);

        return prestamoRepository.findByEstado(estado.toLowerCase()).stream()
                .map(prestamo -> {
                    //datos para el response
                    UsuarioResponse usuario = usuarioClient.buscarUsuarioPorId(prestamo.getIdUsuario());
                    LibroResponse libro = libroClient.buscarLibroPorId(prestamo.getIdLibro());
                    return prestamoMapper.toResponse(prestamo, usuario, libro);
                })
                .toList();
    }
    //en este microservicio se descarta eliminar y actualizar, porque es un servicio de negocio (transaccional), no de mantenimiento (CRUD).
}