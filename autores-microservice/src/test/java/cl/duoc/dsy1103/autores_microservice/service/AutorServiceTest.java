package cl.duoc.dsy1103.autores_microservice.service;

import cl.duoc.dsy1103.autores_microservice.dto.AutorRequest;
import cl.duoc.dsy1103.autores_microservice.dto.AutorResponse;
import cl.duoc.dsy1103.autores_microservice.dto.AutorUpdateRequest;
import cl.duoc.dsy1103.autores_microservice.mapper.AutorMapper;
import cl.duoc.dsy1103.autores_microservice.model.Autor;
import cl.duoc.dsy1103.autores_microservice.repository.AutorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias - AutorService.")
@ActiveProfiles("test")
public class AutorServiceTest {
    @Mock
    private AutorRepository autorRepository;

    @Mock
    private AutorMapper autorMapper;

    @InjectMocks
    private AutorService autorService;

    private Autor sampleAutor;
    private AutorResponse sampleAutorResponse;
    private AutorRequest sampleAutorRequest;

    @BeforeEach
    public void setUp() {
        sampleAutor = Autor.builder()
                .id(1L)
                .nombreCompleto("Jack Marston")
                .biografia("Famoso por documentar las historias de su familia y la banda de forajidos de Van der Linde")
                .nacionalidad("Estadounidense")
                .fechaNacimiento(LocalDate.parse("1895-06-08"))
                .fechaRegistro(LocalDateTime.now())
                .build();

        sampleAutorResponse = AutorResponse.builder()
                .id(1L)
                .nombreCompleto("Jack Marston")
                .biografia("Famoso por documentar las historias de su familia y la banda de forajidos de Van der Linde")
                .nacionalidad("Estadounidense")
                .fechaNacimiento(LocalDate.parse("1895-06-08"))
                .fechaRegistro(LocalDateTime.now())
                .build();

        sampleAutorRequest = AutorRequest.builder()
                .nombreCompleto("Jack Marston")
                .biografia("Famoso por documentar las historias de su familia y la banda de forajidos de Van der Linde")
                .nacionalidad("Estadounidense")
                .fechaNacimiento(LocalDate.parse("1895-06-08"))
                .build();
    }

    //Listar autores
    @Test
    @DisplayName("TC-01 - Retorna todos los autores")
    public void testGetAllAutores() {
        //Given (dado que)
        when(autorRepository.findAll()).thenReturn(List.of(sampleAutor));
        when(autorMapper.toResponse(sampleAutor)).thenReturn(sampleAutorResponse);

        //When (cuando)
        List<AutorResponse> resultado = autorService.buscarAutores();

        //Then (entonces)
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Jack Marston", resultado.get(0).getNombreCompleto());
        verify(autorRepository).findAll();
    }

    //buscar autor por ID
    @Test
    @DisplayName("TC-02 - Retorna autor por su ID")
    public void testGetAutorPorID() {
        //Given
        when(autorRepository.findById(1L)).thenReturn(Optional.of(sampleAutor));
        when(autorMapper.toResponse(sampleAutor)).thenReturn(sampleAutorResponse);

        //When
        AutorResponse resultado = autorService.buscarAutorPorId(1L);

        //Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Famoso por documentar las historias de su familia y la banda de forajidos de Van der Linde", resultado.getBiografia());
    }

    //buscar: excepcion cuando no encuentra autor por su ID
    @Test
    @DisplayName("TC-03 - Lanza excepcion cuando no encuentra autor por su ID")
    public void testGetExceptionBuscarAutorPorID() {
        //Given
        when(autorRepository.findById(99L)).thenReturn(Optional.empty());

        //When & Then
        assertThrows(NoSuchElementException.class, () -> autorService.buscarAutorPorId(99L));
    }

    //Agregar autor
    @Test
    @DisplayName("TC-04 - Agrega autor")
    public void testAddAutor() {
        //Given
        when(autorMapper.toEntity(sampleAutorRequest)).thenReturn(sampleAutor);
        when(autorRepository.save(any(Autor.class))).thenReturn(sampleAutor);
        when(autorMapper.toResponse(sampleAutor)).thenReturn(sampleAutorResponse);

        //When
        AutorResponse resultado = autorService.crearAutor(sampleAutorRequest);

        //Then
        assertNotNull(resultado);
        assertEquals("Jack Marston", resultado.getNombreCompleto());
        verify(autorRepository).save(any(Autor.class));
    }
    //modificar autor
    @Test
    @DisplayName("TC-05 - Actualiza autor por su ID")
    public void testUpdateAutor() {
        //Given
        AutorUpdateRequest request = AutorUpdateRequest.builder()
                .nombreCompleto("Jack Marston Modificado")
                .build();

        when(autorRepository.findById(1L)).thenReturn(Optional.of(sampleAutor));
        when(autorRepository.save(any(Autor.class))).thenReturn(sampleAutor);
        when(autorMapper.toResponse(sampleAutor)).thenReturn(sampleAutorResponse);

        //When
        autorService.actualizarAutor(1L, request);

        //Then
        assertEquals("Jack Marston Modificado", sampleAutor.getNombreCompleto());
        assertEquals("Famoso por documentar las historias de su familia y la banda de forajidos de Van der Linde", sampleAutor.getBiografia()); //no cambia
        verify(autorRepository).save(sampleAutor);
    }
    //modificar: actualiza todos los campos de un autor
    @Test
    @DisplayName("TC-08 - Actualiza exitosamente todos los campos opcionales de un autor")
    public void testUpdateAllFieldsAutor() {
        //Given
        AutorUpdateRequest requestCompleto = AutorUpdateRequest.builder()
                .nombreCompleto("Jack Marston Modificado")
                .biografia("Nueva biografía técnica para el test.")
                .nacionalidad("Mexicana")
                .fechaNacimiento(LocalDate.parse("1900-01-01"))
                .build();

        when(autorRepository.findById(1L)).thenReturn(Optional.of(sampleAutor));
        when(autorRepository.save(any(Autor.class))).thenReturn(sampleAutor);
        when(autorMapper.toResponse(sampleAutor)).thenReturn(sampleAutorResponse);

        //When
        autorService.actualizarAutor(1L, requestCompleto);

        //Then
        assertEquals("Jack Marston Modificado", sampleAutor.getNombreCompleto());
        assertEquals("Nueva biografía técnica para el test.", sampleAutor.getBiografia());
        assertEquals("Mexicana", sampleAutor.getNacionalidad());
        assertEquals(LocalDate.parse("1900-01-01"), sampleAutor.getFechaNacimiento());
        verify(autorRepository).save(sampleAutor);
    }

    //modificar: excepcion cuando no encuentra autor por su ID
    @Test
    @DisplayName("TC-06 - Lanza excepcion cuando no encuentra autor por su ID para modificar")
    public void testGetExceptionUpdateAutor() {
        //Given
        AutorUpdateRequest request = AutorUpdateRequest.builder()
                .nombreCompleto("Cualquiera")
                .build();

        when(autorRepository.findById(99L)).thenReturn(Optional.empty());

        //When & Then
        assertThrows(NoSuchElementException.class, () -> autorService.actualizarAutor(99L, request));
    }

    //eliminar autor
    @Test
    @DisplayName("TC-07 - Elimina un autor por su ID")
    public void testDeleteAutor() {
        //Given
        when(autorRepository.existsById(1L)).thenReturn(true);

        //When
        autorService.eliminarAutor(1L);

        //Then
        verify(autorRepository).deleteById(1L);
    }

    //eliminar: excepcion cuando no encuentra autor por su ID
    @Test
    @DisplayName("TC-09 - Lanza excepcion cuando no encuentra autor por su ID para eliminar")
    public void  testGetExceptionDeleteAutor() {
        //Given
        when(autorRepository.existsById(99L)).thenReturn(false);

        //When & Then
        assertThrows(NoSuchElementException.class, () -> autorService.eliminarAutor(99L));
        verify(autorRepository, never()).deleteById(any());
    }
}
