package cl.duoc.dsy1103.resennas_microservice.service;

import cl.duoc.dsy1103.resennas_microservice.client.LibroClient;
import cl.duoc.dsy1103.resennas_microservice.client.UsuarioClient;
import cl.duoc.dsy1103.resennas_microservice.dto.*;
import cl.duoc.dsy1103.resennas_microservice.exception.ConflictException;
import cl.duoc.dsy1103.resennas_microservice.mapper.ResennaMapper;
import cl.duoc.dsy1103.resennas_microservice.model.Resenna;
import cl.duoc.dsy1103.resennas_microservice.repository.ResennaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResennaServiceTest {

    @Mock
    private ResennaRepository resennaRepository;

    @Mock
    private ResennaMapper resennaMapper;

    @Mock
    private LibroClient libroClient;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private ResennaService resennaService;

    private Resenna resenna;
    private ResennaResponse resennaResponse;
    private ResennaRequest resennaRequest;
    private LibroResponse libroResponse;
    private UsuarioResponse usuarioResponse;

    @BeforeEach
    void setUp() {
       resenna = Resenna.builder()
               .id(1L)
               .descripcion("Buenisimo")
               .calificacion(5)
               .idUsuario(1L)
               .idLibro(1L)
               .build();

       resennaResponse = ResennaResponse.builder()
               .id(1L)
               .tituloLibro("Cien años de soledad")
               .nombreDeUsuario("Camilo Vera")
               .descripcion("Buenisimo")
               .calificacion(5)
               .build();

       resennaRequest = ResennaRequest.builder()
               .descripcion("Buenisimo")
               .calificacion(5)
               .idUsuario(1L)
               .idLibro(1L)
               .build();

       libroResponse = LibroResponse.builder()
               .id(1L)
               .titulo("Cien años de soledad")
               .disponible(true)
               .build();

       usuarioResponse = UsuarioResponse.builder()
               .id(1L)
               .nombreCompleto("Camilo Vera")
               .build();
    }

    //crearResenna

    @Test
    void crearResenna_guardaYRetornaResennaCuandoTodoEsValido(){
        //Given
        when(libroClient.obtenerLibrosPorId(1L)).thenReturn(libroResponse);
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(usuarioResponse);
        when(resennaRepository.existsByIdUsuarioAndIdLibro(1L, 1L)).thenReturn(false);
        when(resennaMapper.fromRequest(resennaRequest)).thenReturn(resenna);
        when(resennaRepository.save(any(Resenna.class))).thenReturn(resenna);
        when(resennaMapper.toResponse(resenna, libroResponse, usuarioResponse)).thenReturn(resennaResponse);

        //When
        ResennaResponse resultado = resennaService.crearResenna(resennaRequest);

        //Then
        assertNotNull(resultado);
        assertEquals("Cien años de soledad", resultado.getTituloLibro());
        verify(resennaRepository).save(any(Resenna.class));
    }
    @Test
    void crearResenna_lanzaExcepcionSiLibroNoExiste() {
        //Given
        LibroResponse libroInexistente = LibroResponse.builder()
                .titulo("no está disponible")
                .build();

        when(libroClient.obtenerLibrosPorId(1L)).thenReturn(libroInexistente);
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(usuarioResponse);

        //When / Then
        assertThrows(NoSuchElementException.class,
                ()-> resennaService.crearResenna(resennaRequest));
        verify(resennaRepository, never()).save(any());
    }

    @Test
    void crearResenna_lanzaExcepcionSiUsuarioYaResennoElLibro() {
        //Given
        when(libroClient.obtenerLibrosPorId(1L)).thenReturn(libroResponse);
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(usuarioResponse);
        when(resennaRepository.existsByIdUsuarioAndIdLibro(1L, 1L)).thenReturn(true);

        //When / Then
        assertThrows(ConflictException.class,
                ()-> resennaService.crearResenna(resennaRequest));
        verify(resennaRepository, never()).save(any());
    }

    //buscarResennaPorId

    @Test
    void buscarResennaPorId_retornaResennaCuandoExiste() {
        //Given
        when(resennaRepository.findById(1L)).thenReturn(Optional.of(resenna));
        when(libroClient.obtenerLibrosPorId(1L)).thenReturn(libroResponse);
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(usuarioResponse);
        when(resennaMapper.toResponse(resenna, libroResponse , usuarioResponse)).thenReturn(resennaResponse);

        //When
        ResennaResponse resultado = resennaService.buscarResennaPorId(1L);

        //Then
        assertNotNull(resultado);
        assertEquals(5, resultado.getCalificacion());
    }

    @Test
    void buscarResennaPorId_lanzaExcepcionCuandoNoExiste() {
        //Given
        when(resennaRepository.findById(99L)).thenReturn(Optional.empty());

        //When / Then
        assertThrows(NoSuchElementException.class,
                ()-> resennaService.buscarResennaPorId(99L));
    }

    //listarResennas
    @Test
    void listarResennas_retornaListaDeResennas(){
        //Given
        when(resennaRepository.findAll()).thenReturn(List.of(resenna));
        when(libroClient.obtenerLibrosPorId(1L)).thenReturn(libroResponse);
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(usuarioResponse);
        when(resennaMapper.toResponse(resenna, libroResponse, usuarioResponse)).thenReturn(resennaResponse);

        // When
        List<ResennaResponse> resultado = resennaService.listarResennas();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Cien años de soledad", resultado.get(0).getTituloLibro());
    }

    //modificarResenna
    @Test
    void modificarResenna_actualizaSoloCamposNoNulos() {
        // Given
        ResennaUpdateRequest request = ResennaUpdateRequest.builder()
                .calificacion(3)
                .build();

        when(resennaRepository.findById(1L)).thenReturn(Optional.of(resenna));
        when(resennaRepository.save(any(Resenna.class))).thenReturn(resenna);
        when(libroClient.obtenerLibrosPorId(1L)).thenReturn(libroResponse);
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(usuarioResponse);
        when(resennaMapper.toResponse(resenna, libroResponse, usuarioResponse)).thenReturn(resennaResponse);

        // When
        resennaService.modificarResenna(1L, request);

        // Then
        assertEquals(3, resenna.getCalificacion());
        assertEquals("Buenisimo", resenna.getDescripcion()); // no cambia
        verify(resennaRepository).save(resenna);
    }

    @Test
    void modificarResenna_lanzaExcepcionSiNoExiste() {
        // Given
        ResennaUpdateRequest request = ResennaUpdateRequest.builder()
                .calificacion(3)
                .build();

        when(resennaRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(NoSuchElementException.class,
                () -> resennaService.modificarResenna(99L, request));
    }

    //eliminarResenna
    @Test
    void eliminarResenna_eliminaCorrectamenteCuandoExiste() {
        // Given
        when(resennaRepository.existsById(1L)).thenReturn(true);

        // When
        resennaService.eliminarResenna(1L);

        // Then
        verify(resennaRepository).deleteById(1L);
    }

    @Test
    void eliminarResenna_lanzaExcepcionSiNoExiste() {
        // Given
        when(resennaRepository.existsById(99L)).thenReturn(false);

        // When / Then
        assertThrows(NoSuchElementException.class,
                () -> resennaService.eliminarResenna(99L));
        verify(resennaRepository, never()).deleteById(any());
    }





}
