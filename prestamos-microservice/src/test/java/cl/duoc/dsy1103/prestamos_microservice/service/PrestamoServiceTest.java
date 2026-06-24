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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias - PrestamoService.")
@ActiveProfiles("test")
public class PrestamoServiceTest {

    @Mock
    private PrestamoRepository prestamoRepository;

    @Mock
    private LibroClient libroClient;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private PrestamoMapper prestamoMapper;

    @InjectMocks
    private PrestamoService prestamoService;

    private Prestamo samplePrestamo;
    private PrestamoRequest samplePrestamoRequest;
    private PrestamoResponse samplePrestamoResponse;
    private UsuarioResponse sampleUsuarioResponse;
    private LibroResponse sampleLibroResponse;

    @BeforeEach
    public void setUp() {
        samplePrestamo = Prestamo.builder()
                .id(1L)
                .fechaPrestamo(LocalDateTime.now())
                .fechaDevolucion(LocalDateTime.now().plusDays(7))
                .idUsuario(10L)
                .idLibro(5L)
                .estado("activo")
                .build();

        sampleUsuarioResponse = new UsuarioResponse(10L, "Juan Carlos Pérez");
        sampleLibroResponse = new LibroResponse(5L, "El Señor de los Anillos");

        samplePrestamoResponse = PrestamoResponse.builder()
                .id(1L)
                .fechaPrestamo(samplePrestamo.getFechaPrestamo())
                .fechaDevolucion(samplePrestamo.getFechaDevolucion())
                .usuario(sampleUsuarioResponse)
                .libro(sampleLibroResponse)
                .estado("activo")
                .build();

        samplePrestamoRequest = PrestamoRequest.builder()
                .fechaDevolucion(LocalDateTime.now().plusDays(7))
                .idUsuario(10L)
                .idLibro(5L)
                .estado("activo")
                .build();
    }

    // --- MÉTODOS DE BÚSQUEDA ---

    @Test
    @DisplayName("TC-01 - Retorna un préstamo específico por su ID")
    public void testObtenerPrestamoPorIdExitoso() {
        //Given
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(samplePrestamo));
        when(usuarioClient.buscarUsuarioPorId(10L)).thenReturn(sampleUsuarioResponse);
        when(libroClient.buscarLibroPorId(5L)).thenReturn(sampleLibroResponse);
        when(prestamoMapper.toResponse(samplePrestamo, sampleUsuarioResponse, sampleLibroResponse)).thenReturn(samplePrestamoResponse);

        //When
        PrestamoResponse resultado = prestamoService.obtenerPrestamoPorId(1L);

        //Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("activo", resultado.getEstado());
        verify(prestamoRepository).findById(1L);
    }

    @Test
    @DisplayName("TC-02 - Lanza excepción al buscar un préstamo con ID inexistente")
    public void testObtenerPrestamoPorIdException() {
        //Given
        when(prestamoRepository.findById(99L)).thenReturn(Optional.empty());

        //When & Then
        assertThrows(NoSuchElementException.class, () -> prestamoService.obtenerPrestamoPorId(99L));
        verify(prestamoRepository).findById(99L);
    }

    @Test
    @DisplayName("TC-03 - Retorna la lista global de todos los préstamos")
    public void testListarTodosLosPrestamos() {
        //Given
        when(prestamoRepository.findAll()).thenReturn(List.of(samplePrestamo));
        when(usuarioClient.buscarUsuarioPorId(10L)).thenReturn(sampleUsuarioResponse);
        when(libroClient.buscarLibroPorId(5L)).thenReturn(sampleLibroResponse);
        when(prestamoMapper.toResponse(samplePrestamo, sampleUsuarioResponse, sampleLibroResponse)).thenReturn(samplePrestamoResponse);

        //When
        List<PrestamoResponse> resultado = prestamoService.listarTodosLosPrestamos();

        //Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(prestamoRepository).findAll();
    }

    // --- CREACIÓN DE PRÉSTAMOS ---

    @Test
    @DisplayName("TC-04 - Registra exitosamente un préstamo válido")
    public void testCrearPrestamoExitoso() {
        //Given
        when(usuarioClient.buscarUsuarioPorId(10L)).thenReturn(sampleUsuarioResponse);
        when(libroClient.buscarLibroPorId(5L)).thenReturn(sampleLibroResponse);
        when(prestamoMapper.toEntity(samplePrestamoRequest)).thenReturn(samplePrestamo);
        when(prestamoRepository.save(any(Prestamo.class))).thenReturn(samplePrestamo);
        when(prestamoMapper.toResponse(samplePrestamo, sampleUsuarioResponse, sampleLibroResponse)).thenReturn(samplePrestamoResponse);

        //When
        PrestamoResponse resultado = prestamoService.crearPrestamo(samplePrestamoRequest);

        //Then
        assertNotNull(resultado);
        verify(libroClient).marcarComoPrestado(5L);
        verify(prestamoRepository).save(any(Prestamo.class));
    }

    @Test
    @DisplayName("TC-05 - Lanza excepción al crear préstamo si el usuario no existe")
    public void testCrearPrestamoExceptionUsuarioNoExiste() {
        //Given
        when(usuarioClient.buscarUsuarioPorId(10L)).thenReturn(null);

        //When & Then
        assertThrows(NoSuchElementException.class, () -> prestamoService.crearPrestamo(samplePrestamoRequest));
        verify(libroClient, never()).marcarComoPrestado(anyLong());
        verify(prestamoRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC-06 - Lanza excepción al crear préstamo si el usuario está marcado como (Eliminado)")
    public void testCrearPrestamoExceptionUsuarioEliminado() {
        //Given
        UsuarioResponse usuarioEliminado = new UsuarioResponse(10L, "Juan Carlos Pérez (Eliminado)");
        when(usuarioClient.buscarUsuarioPorId(10L)).thenReturn(usuarioEliminado);

        //When & Then
        assertThrows(NoSuchElementException.class, () -> prestamoService.crearPrestamo(samplePrestamoRequest));
        verify(libroClient, never()).marcarComoPrestado(anyLong());
        verify(prestamoRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC-07 - Lanza excepción al crear préstamo si el libro no existe")
    public void testCrearPrestamoExceptionLibroNoExiste() {
        //Given
        when(usuarioClient.buscarUsuarioPorId(10L)).thenReturn(sampleUsuarioResponse);
        when(libroClient.buscarLibroPorId(5L)).thenReturn(null);

        //When & Then
        assertThrows(NoSuchElementException.class, () -> prestamoService.crearPrestamo(samplePrestamoRequest));
        verify(libroClient, never()).marcarComoPrestado(anyLong());
        verify(prestamoRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC-08 - Lanza excepción al crear préstamo si el libro está marcado como (Eliminado)")
    public void testCrearPrestamoExceptionLibroEliminado() {
        //Given
        LibroResponse libroEliminado = new LibroResponse(5L, "El Señor de los Anillos (Eliminado)");
        when(usuarioClient.buscarUsuarioPorId(10L)).thenReturn(sampleUsuarioResponse);
        when(libroClient.buscarLibroPorId(5L)).thenReturn(libroEliminado);

        //When & Then
        assertThrows(NoSuchElementException.class, () -> prestamoService.crearPrestamo(samplePrestamoRequest));
        verify(libroClient, never()).marcarComoPrestado(anyLong());
        verify(prestamoRepository, never()).save(any());
    }

    // --- ACCIONES DE NEGOCIO: DEVOLUCIÓN ---

    @Test
    @DisplayName("TC-09 - Procesa exitosamente la devolución física de un libro")
    public void testDevolverLibroExitoso() {
        //Given
        samplePrestamo.setEstado("activo"); // Inicia activo
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(samplePrestamo));
        when(prestamoRepository.save(any(Prestamo.class))).thenReturn(samplePrestamo);
        when(usuarioClient.buscarUsuarioPorId(10L)).thenReturn(sampleUsuarioResponse);
        when(libroClient.buscarLibroPorId(5L)).thenReturn(sampleLibroResponse);

        samplePrestamoResponse.setEstado("devuelto");
        when(prestamoMapper.toResponse(samplePrestamo, sampleUsuarioResponse, sampleLibroResponse)).thenReturn(samplePrestamoResponse);

        //When
        PrestamoResponse resultado = prestamoService.devolverLibro(1L);

        //Then
        assertNotNull(resultado);
        assertEquals("devuelto", samplePrestamo.getEstado());
        assertNotNull(samplePrestamo.getFechaDevolucion());
        verify(libroClient).marcarComoDevuelto(5L);
        verify(prestamoRepository).save(samplePrestamo);
    }

    @Test
    @DisplayName("TC-10 - Lanza excepción en devolución si la transacción de préstamo no existe")
    public void testDevolverLibroExceptionNoEncontrado() {
        //Given
        when(prestamoRepository.findById(99L)).thenReturn(Optional.empty());

        //When & Then
        assertThrows(NoSuchElementException.class, () -> prestamoService.devolverLibro(99L));
        verify(libroClient, never()).marcarComoDevuelto(anyLong());
    }

    @Test
    @DisplayName("TC-11 - Lanza excepción si se intenta devolver un préstamo que ya fue cerrado")
    public void testDevolverLibroExceptionYaCerrado() {
        //Given
        samplePrestamo.setEstado("devuelto"); // Transacción ya cerrada
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(samplePrestamo));

        //When & Then
        assertThrows(IllegalStateException.class, () -> prestamoService.devolverLibro(1L));
        verify(libroClient, never()).marcarComoDevuelto(anyLong());
        verify(prestamoRepository, never()).save(any());
    }

    // --- HISTORIALES Y FILTROS ---

    @Test
    @DisplayName("TC-12 - Retorna el historial de préstamos de un Usuario válido")
    public void testHistorialPorUsuarioExitoso() {
        //Given
        when(usuarioClient.buscarUsuarioPorId(10L)).thenReturn(sampleUsuarioResponse);
        when(prestamoRepository.findByIdUsuario(10L)).thenReturn(List.of(samplePrestamo));
        when(libroClient.buscarLibroPorId(5L)).thenReturn(sampleLibroResponse);
        when(prestamoMapper.toResponse(samplePrestamo, sampleUsuarioResponse, sampleLibroResponse)).thenReturn(samplePrestamoResponse);

        //When
        List<PrestamoResponse> resultado = prestamoService.historialPorUsuario(10L);

        //Then
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(prestamoRepository).findByIdUsuario(10L);
    }

    @Test
    @DisplayName("TC-13 - Lanza excepción en historial si el usuario consultado no existe")
    public void testHistorialPorUsuarioException() {
        //Given
        when(usuarioClient.buscarUsuarioPorId(99L)).thenReturn(null);

        //When & Then
        assertThrows(NoSuchElementException.class, () -> prestamoService.historialPorUsuario(99L));
        verify(prestamoRepository, never()).findByIdUsuario(anyLong());
    }

    @Test
    @DisplayName("TC-14 - Retorna el historial de trazabilidad de un Libro válido")
    public void testObtenerHistorialPorLibroExitoso() {
        //Given
        when(libroClient.buscarLibroPorId(5L)).thenReturn(sampleLibroResponse);
        when(prestamoRepository.findByIdLibro(5L)).thenReturn(List.of(samplePrestamo));
        when(usuarioClient.buscarUsuarioPorId(10L)).thenReturn(sampleUsuarioResponse);
        when(prestamoMapper.toResponse(samplePrestamo, sampleUsuarioResponse, sampleLibroResponse)).thenReturn(samplePrestamoResponse);

        //When
        List<PrestamoResponse> resultado = prestamoService.obtenerHistorialPorLibro(5L);

        //Then
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(prestamoRepository).findByIdLibro(5L);
    }

    @Test
    @DisplayName("TC-15 - Lanza excepción en historial si el libro consultado no existe")
    public void testObtenerHistorialPorLibroException() {
        //Given
        when(libroClient.buscarLibroPorId(99L)).thenReturn(null);

        //When & Then
        assertThrows(NoSuchElementException.class, () -> prestamoService.obtenerHistorialPorLibro(99L));
        verify(prestamoRepository, never()).findByIdLibro(anyLong());
    }

    @Test
    @DisplayName("TC-16 - Retorna los préstamos filtrados por su estado operativo")
    public void testListarPrestamosPorEstado() {
        //Given
        when(prestamoRepository.findByEstado("activo")).thenReturn(List.of(samplePrestamo));
        when(usuarioClient.buscarUsuarioPorId(10L)).thenReturn(sampleUsuarioResponse);
        when(libroClient.buscarLibroPorId(5L)).thenReturn(sampleLibroResponse);
        when(prestamoMapper.toResponse(samplePrestamo, sampleUsuarioResponse, sampleLibroResponse)).thenReturn(samplePrestamoResponse);

        //When
        List<PrestamoResponse> resultado = prestamoService.listarPrestamosPorEstado("ACTIVO"); // Testea el .toLowerCase() interno

        //Then
        assertNotNull(resultado);
        verify(prestamoRepository).findByEstado("activo");
    }
}