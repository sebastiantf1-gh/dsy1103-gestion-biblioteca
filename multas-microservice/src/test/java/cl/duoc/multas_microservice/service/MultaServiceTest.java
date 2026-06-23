package cl.duoc.multas_microservice.service;

import cl.duoc.multas_microservice.client.PrestamoClient;
import cl.duoc.multas_microservice.client.UsuarioClient;
import cl.duoc.multas_microservice.dto.MultaRequest;
import cl.duoc.multas_microservice.dto.MultaResponse;
import cl.duoc.multas_microservice.dto.PrestamoResponse;
import cl.duoc.multas_microservice.dto.UsuarioResponse;
import cl.duoc.multas_microservice.mapper.MultaMapper;
import cl.duoc.multas_microservice.model.Multa;
import cl.duoc.multas_microservice.repository.MultaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MultaServiceTest {

    @Mock
    private MultaRepository multaRepository;

    @Mock
    private MultaMapper multaMapper;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private PrestamoClient prestamoClient;

    @InjectMocks
    private MultaService multaService;

    private Multa multa;
    private MultaResponse multaResponse;
    private MultaRequest multaRequest;
    private String token;

    @BeforeEach
    void setUp() {
        token = "Bearer token-secreto-123";

        multa = Multa.builder()
                .id(1L)
                .monto(5000)
                .idUsuario(1L)
                .idPrestamo(10L)
                .fechaRegistro(LocalDateTime.now())
                .fechaLimitePago(LocalDateTime.now().plusDays(10))
                .build();

        multaResponse = MultaResponse.builder()
                .id(1L)
                .monto(5000)
                .idUsuario(1L)
                .idPrestamo(10L)
                .fechaRegistro(multa.getFechaRegistro())
                .fechaLimitePago(multa.getFechaLimitePago())
                .build();

        multaRequest = MultaRequest.builder()
                .monto(5000)
                .idUsuario(1L)
                .idPrestamo(10L)
                .fechaLimitePago(multa.getFechaLimitePago())
                .build();
    }

    //crearMulta
    @Test
    void crearMulta_guardaYRetornaMulta() {
        //Given
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(new UsuarioResponse());
        when(prestamoClient.obtenerPrestamoPorId(10L)).thenReturn(new PrestamoResponse());
        when(multaMapper.toEntity(multaRequest)).thenReturn(multa);
        when(multaRepository.save(any(Multa.class))).thenReturn(multa);
        when(multaMapper.toResponse(multa)).thenReturn(multaResponse);

        //When
        MultaResponse resultado = multaService.crearMulta(multaRequest, token);

        //Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(5000, resultado.getMonto());
        verify(multaRepository).save(any(Multa.class));
    }

    //obtenerTodasLasMultas
    @Test
    void obtenerTodasLasMultas_retornaListaDeMultas() {
        //Given
        when(multaRepository.findAll()).thenReturn(List.of(multa));
        when(multaMapper.toResponse(multa)).thenReturn(multaResponse);

        //When
        List<MultaResponse> resultado = multaService.obtenerTodasLasMultas();

        //Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(5000, resultado.get(0).getMonto());
        verify(multaRepository).findAll();
    }

    //obtenerMultasPorUsuarioId
    @Test
    void obtenerMultasPorUsuarioId_retornaListaFiltradaCuandoExiste() {
        //Given
        when(multaRepository.findByIdUsuario(1L)).thenReturn(List.of(multa));
        when(multaMapper.toResponse(multa)).thenReturn(multaResponse);

        //When
        List<MultaResponse> resultado = multaService.obtenerMultasPorUsuarioId(1L);

        //Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getIdUsuario());
        verify(multaRepository).findByIdUsuario(1L);
    }

    //eliminarMulta
    @Test
    void eliminarMulta_eliminaCorrectamenteCuandoExiste() {
        //Given
        when(multaRepository.existsById(1L)).thenReturn(true);

        //When
        multaService.eliminarMulta(1L);

        //Then
        verify(multaRepository).deleteById(1L);
    }

    @Test
    void eliminarMulta_lanzaExcepcionSiNoExiste() {
        //Given
        when(multaRepository.existsById(99L)).thenReturn(false);

        //When / Then
        assertThrows(NoSuchElementException.class,
                () -> multaService.eliminarMulta(99L));
        verify(multaRepository, never()).deleteById(any());
    }
}