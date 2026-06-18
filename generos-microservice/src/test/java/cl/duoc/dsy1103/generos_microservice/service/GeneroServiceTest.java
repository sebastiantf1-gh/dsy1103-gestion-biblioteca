package cl.duoc.dsy1103.generos_microservice.service;

import cl.duoc.dsy1103.generos_microservice.dto.GeneroRequest;
import cl.duoc.dsy1103.generos_microservice.dto.GeneroResponse;
import cl.duoc.dsy1103.generos_microservice.dto.GeneroUpdateRequest;
import cl.duoc.dsy1103.generos_microservice.mapper.GeneroMapper;
import cl.duoc.dsy1103.generos_microservice.model.Genero;
import cl.duoc.dsy1103.generos_microservice.repository.GeneroRepository;
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
public class GeneroServiceTest {

    @Mock
    private GeneroRepository generoRepository;

    @Mock
    private GeneroMapper generoMapper;

    @InjectMocks
    private GeneroService generoService;

    private Genero genero;
    private GeneroResponse generoResponse;
    private GeneroRequest generoRequest;

    @BeforeEach
    void setUp() {
        genero = Genero.builder()
                .id(1L)
                .nombreGenero("Didáctico")
                .descripcion("Su proposito es trasmitir conocimiento")
                .build();

        generoResponse = GeneroResponse.builder()
                .id(1L)
                .nombreGenero("Didáctico")
                .descripcion("Su proposito es trasmitir conocimiento")
                .build();

        generoRequest = GeneroRequest.builder()
                .nombreGenero("Didáctico")
                .descripcion("Su proposito es trasmitir conocimiento")
                .build();
    }

    //ListarGeneros
    @Test
    void listarGeneros_retornaListaDeGeneros() {
        //Given (dado que)
        when(generoRepository.findAll()).thenReturn(List.of(genero));
        when(generoMapper.toResponse(genero)).thenReturn(generoResponse);

        //When (cuando)
        List<GeneroResponse> resultado = generoService.listarGeneros();

        //Then (entonces)
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Didáctico", resultado.get(0).getNombreGenero());
        verify(generoRepository).findAll();
    }

    //buscarGeneroPorId
    @Test
    void buscarGeneroPorId_retornaGeneroCuandoExiste(){
        //Given
        when(generoRepository.findById(1L)).thenReturn(Optional.of(genero));
        when(generoMapper.toResponse(genero)).thenReturn(generoResponse);

        //When
        GeneroResponse resultado = generoService.buscarGeneroPorId(1L);

        //Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Su proposito es trasmitir conocimiento", resultado.getDescripcion());
    }

    @Test
    void buscarGeneroPorId_lanzaExcepcionCuandoNoExiste() {
        //Given
        when(generoRepository.findById(99L)).thenReturn(Optional.empty());

        //When / Then
        assertThrows(NoSuchElementException.class,
                ()-> generoService.buscarGeneroPorId(99L));
    }

    //agregarGenero
    @Test
    void agregarGenero_guardaYRetornaGenero(){
        //Given
        when(generoMapper.fromRequest(generoRequest)).thenReturn(genero);
        when(generoRepository.save(any(Genero.class))).thenReturn(genero);
        when(generoMapper.toResponse(genero)).thenReturn(generoResponse);

        //When
        GeneroResponse resultado = generoService.agregarGenero(generoRequest);

        //Then
        assertNotNull(resultado);
        assertEquals("Didáctico", resultado.getNombreGenero());
        verify(generoRepository).save(any(Genero.class));

    }

    //modificarGenero
    @Test
    void modificarGenero_actualizaSoloCamposNoNulos() {
        //Given
        GeneroUpdateRequest request = GeneroUpdateRequest.builder()
                .nombreGenero("Didáctico Modificado")
                .build();
        when(generoRepository.findById(1L)).thenReturn(Optional.of(genero));
        when(generoRepository.save(any(Genero.class))).thenReturn(genero);
        when(generoMapper.toResponse(genero)).thenReturn(generoResponse);

        //When
        generoService.modificarGenero(1L, request);

        //Then
        assertEquals("Didáctico Modificado", genero.getNombreGenero());
        assertEquals("Su proposito es trasmitir conocimiento", genero.getDescripcion()); // no cambia
        verify(generoRepository).save(genero);
    }

    @Test
    void modificarGenero_lanzaExcepcionSiGeneroNoExiste() {
        //Given
        GeneroUpdateRequest request = GeneroUpdateRequest.builder()
                .nombreGenero("Cualquiera")
                .build();

        when(generoRepository.findById(99L)).thenReturn(Optional.empty());

        //When /Then
        assertThrows(NoSuchElementException.class,
                ()-> generoService.modificarGenero(99L, request));
    }

    //eliminarGenero
    @Test
    void eliminarGenero_eliminarCorrectamenteCuandoExiste() {
        //Given
        when(generoRepository.existsById(1L)).thenReturn(true);

        //When
        generoService.eliminarGenero(1L);

        //Then
        verify(generoRepository).deleteById(1L);
    }
    @Test
    void eliminarGenero_lanzaExcepcionSiNoExiste(){
        //Given
        when(generoRepository.existsById(99L)).thenReturn(false);

        //When / Then
        assertThrows(NoSuchElementException.class,
                ()-> generoService.eliminarGenero(99L));
        verify(generoRepository, never()).deleteById(any());
    }
}
