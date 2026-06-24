package cl.duoc.reservas_microservice.service;

import cl.duoc.reservas_microservice.client.LibroClient;
import cl.duoc.reservas_microservice.client.UsuarioClient;
import cl.duoc.reservas_microservice.dto.CrearReservaRequest;
import cl.duoc.reservas_microservice.dto.LibroResponse;
import cl.duoc.reservas_microservice.dto.ReservaResponse;
import cl.duoc.reservas_microservice.dto.UsuarioResponse;
import cl.duoc.reservas_microservice.mapper.ReservaMapper;
import cl.duoc.reservas_microservice.model.Reserva;
import cl.duoc.reservas_microservice.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private LibroClient libroClient;

    @Spy // Usamos Spy para que ejecute el mapeo real sin necesidad de mockearlo por completo
    private ReservaMapper reservaMapper;

    @InjectMocks
    private ReservaService reservaService;

    private CrearReservaRequest request;
    private UsuarioResponse usuarioMock;
    private LibroResponse libroMock;
    private Reserva reservaGuardada;
    private final String mockToken = "Bearer token-valido-xyz";

    @BeforeEach
    void setUp() {
        request = new CrearReservaRequest();
        request.setIdUsuario(1L);
        request.setIdLibro(10L);
        request.setFechaInicio(LocalDate.now().plusDays(1));
        request.setFechaTermino(LocalDate.now().plusDays(5));

        usuarioMock = UsuarioResponse.builder()
                .id(1L)
                .nombreCompleto("Juan Pérez")
                .email("juan@duocuc.cl")
                .build();

        libroMock = LibroResponse.builder()
                .id(10L)
                .disponible(true)
                .build();

        reservaGuardada = Reserva.builder()
                .id(100L)
                .idUsuario(1L)
                .idLibro(10L)
                .fechaInicio(request.getFechaInicio())
                .fechaTermino(request.getFechaTermino())
                .estado("ACTIVA")
                .build();
    }

    @Test
    void crearReserva_exitoso() {
        // Given
        when(usuarioClient.obtenerUsuarioPorId(1L, mockToken)).thenReturn(usuarioMock);
        when(libroClient.obtenerLibroPorId(10L, mockToken)).thenReturn(libroMock);
        when(reservaRepository.existsByIdUsuarioAndIdLibroAndEstado(1L, 10L, "ACTIVA")).thenReturn(false);
        when(reservaRepository.existeCruceDeFechas(eq(10L), any(LocalDate.class), any(LocalDate.class))).thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaGuardada);

        // When
        ReservaResponse respuesta = reservaService.crearReserva(request, mockToken);

        // Then
        assertNotNull(respuesta);
        assertEquals(100L, respuesta.getId());
        assertEquals("ACTIVA", respuesta.getEstado());
        assertEquals(1L, respuesta.getIdUsuario());
        assertEquals(10L, respuesta.getIdLibro());
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    void crearReserva_fallo_libroNoDisponible() {
        // Given
        libroMock.setDisponible(false); // Forzamos a que no esté habilitado físicamente
        when(usuarioClient.obtenerUsuarioPorId(1L, mockToken)).thenReturn(usuarioMock);
        when(libroClient.obtenerLibroPorId(10L, mockToken)).thenReturn(libroMock);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.crearReserva(request, mockToken);
        });

        assertEquals("El libro seleccionado no se encuentra disponible para nuevas solicitudes.", exception.getMessage());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    void crearReserva_fallo_usuarioYaTieneReservaActiva() {
        // Given
        when(usuarioClient.obtenerUsuarioPorId(1L, mockToken)).thenReturn(usuarioMock);
        when(libroClient.obtenerLibroPorId(10L, mockToken)).thenReturn(libroMock);
        when(reservaRepository.existsByIdUsuarioAndIdLibroAndEstado(1L, 10L, "ACTIVA")).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.crearReserva(request, mockToken);
        });

        assertEquals("Ya posees una reserva activa para este mismo libro.", exception.getMessage());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    void crearReserva_fallo_cruceDeFechasDetectado() {
        // Given
        when(usuarioClient.obtenerUsuarioPorId(1L, mockToken)).thenReturn(usuarioMock);
        when(libroClient.obtenerLibroPorId(10L, mockToken)).thenReturn(libroMock);
        when(reservaRepository.existsByIdUsuarioAndIdLibroAndEstado(1L, 10L, "ACTIVA")).thenReturn(false);
        // Simulamos que las fechas se solapan con otra reserva existente
        when(reservaRepository.existeCruceDeFechas(eq(10L), any(LocalDate.class), any(LocalDate.class))).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.crearReserva(request, mockToken);
        });

        assertEquals("El periodo de tiempo solicitado se encuentra reservado o protegido por garantía de otro usuario.", exception.getMessage());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }
}