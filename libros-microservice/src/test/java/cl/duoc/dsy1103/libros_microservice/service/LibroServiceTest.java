package cl.duoc.dsy1103.libros_microservice.service;

import cl.duoc.dsy1103.libros_microservice.client.AutorClient;
import cl.duoc.dsy1103.libros_microservice.client.CategoriaClient;
import cl.duoc.dsy1103.libros_microservice.client.GeneroClient;
import cl.duoc.dsy1103.libros_microservice.dto.*;
import cl.duoc.dsy1103.libros_microservice.mapper.LibroMapper;
import cl.duoc.dsy1103.libros_microservice.model.Libro;
import cl.duoc.dsy1103.libros_microservice.repository.LibroRepository;
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
@DisplayName("Pruebas unitarias - LibroService.")
@ActiveProfiles("test")
public class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private LibroMapper libroMapper;

    @Mock
    private AutorClient autorClient;

    @Mock
    private CategoriaClient categoriaClient;

    @Mock
    private GeneroClient generoClient;

    @InjectMocks
    private LibroService libroService;

    private Libro sampleLibro;
    private LibroResponse sampleLibroResponse;
    private LibroRequest sampleLibroRequest;
    private AutorResponse sampleAutorResponse;
    private CategoriaResponse sampleCategoriaResponse;
    private GeneroResponse sampleGeneroResponse;

    @BeforeEach
    public void setUp() {
        sampleLibro = Libro.builder()
                .id(1L)
                .isbn("978-84-450-7372-8")
                .titulo("El Señor de los Anillos")
                .sinopsis("Una gran aventura épica.")
                .numeroPaginas((short) 423)
                .disponible(true)
                .idAutor(1L)
                .idCategoria(2L)
                .idGenero(3L)
                .fechaRegistro(LocalDateTime.now())
                .build();

        sampleAutorResponse = new AutorResponse(1L, "J.R.R. Tolkien");
        sampleCategoriaResponse = new CategoriaResponse(2L, "Fantasía");
        sampleGeneroResponse = new GeneroResponse(3L, "Épico");

        sampleLibroResponse = LibroResponse.builder()
                .id(1L)
                .isbn("978-84-450-7372-8")
                .titulo("El Señor de los Anillos")
                .sinopsis("Una gran aventura épica.")
                .numeroPaginas((short) 423)
                .disponible(true)
                .autor(sampleAutorResponse)
                .categoria(sampleCategoriaResponse)
                .genero(sampleGeneroResponse)
                .fechaRegistro(LocalDateTime.now())
                .build();

        sampleLibroRequest = LibroRequest.builder()
                .isbn("978-84-450-7372-8")
                .titulo("El Señor de los Anillos")
                .sinopsis("Una gran aventura épica.")
                .numeroPaginas((short) 423)
                .disponible(true)
                .idAutor(1L)
                .idCategoria(2L)
                .idGenero(3L)
                .build();
    }

    @Test
    @DisplayName("TC-01 - Retorna todos los libros con sus datos agregados")
    public void testGetAllLibros() {
        //Given
        when(libroRepository.findAll()).thenReturn(List.of(sampleLibro));
        when(autorClient.buscarAutorPorId(1L)).thenReturn(sampleAutorResponse);
        when(categoriaClient.buscarCategoriaPorId(2L)).thenReturn(sampleCategoriaResponse);
        when(generoClient.buscarGeneroPorId(3L)).thenReturn(sampleGeneroResponse);
        when(libroMapper.toResponse(sampleLibro, sampleAutorResponse, sampleCategoriaResponse, sampleGeneroResponse))
                .thenReturn(sampleLibroResponse);

        //When
        List<LibroResponse> resultado = libroService.buscarLibros();

        //Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("El Señor de los Anillos", resultado.get(0).getTitulo());
        verify(libroRepository).findAll();
    }

    @Test
    @DisplayName("TC-02 - Retorna libro por su ID")
    public void testGetLibroPorID() {
        //Given
        when(libroRepository.findById(1L)).thenReturn(Optional.of(sampleLibro));
        when(autorClient.buscarAutorPorId(1L)).thenReturn(sampleAutorResponse);
        when(categoriaClient.buscarCategoriaPorId(2L)).thenReturn(sampleCategoriaResponse);
        when(generoClient.buscarGeneroPorId(3L)).thenReturn(sampleGeneroResponse);
        when(libroMapper.toResponse(sampleLibro, sampleAutorResponse, sampleCategoriaResponse, sampleGeneroResponse))
                .thenReturn(sampleLibroResponse);

        //When
        LibroResponse resultado = libroService.buscarLibroPorId(1L);

        //Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("978-84-450-7372-8", resultado.getIsbn());
    }

    @Test
    @DisplayName("TC-03 - Lanza excepcion cuando no encuentra libro por su ID")
    public void testGetExceptionBuscarLibroPorID() {
        //Given
        when(libroRepository.findById(99L)).thenReturn(Optional.empty());

        //When & Then
        assertThrows(NoSuchElementException.class, () -> libroService.buscarLibroPorId(99L));
    }

    @Test
    @DisplayName("TC-04 - Retorna libros filtrados por Autor")
    public void testGetLibrosPorAutor() {
        //Given
        when(libroRepository.findByIdAutor(1L)).thenReturn(List.of(sampleLibro));
        when(autorClient.buscarAutorPorId(1L)).thenReturn(sampleAutorResponse);
        when(categoriaClient.buscarCategoriaPorId(2L)).thenReturn(sampleCategoriaResponse);
        when(generoClient.buscarGeneroPorId(3L)).thenReturn(sampleGeneroResponse);
        when(libroMapper.toResponse(sampleLibro, sampleAutorResponse, sampleCategoriaResponse, sampleGeneroResponse))
                .thenReturn(sampleLibroResponse);

        //When
        List<LibroResponse> resultado = libroService.buscarLibrosPorAutor(1L);

        //Then
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(libroRepository).findByIdAutor(1L);
    }

    @Test
    @DisplayName("TC-05 - Retorna libros filtrados por Categoria")
    public void testGetLibrosPorCategoria() {
        //Given
        when(libroRepository.findByIdCategoria(2L)).thenReturn(List.of(sampleLibro));
        when(autorClient.buscarAutorPorId(1L)).thenReturn(sampleAutorResponse);
        when(categoriaClient.buscarCategoriaPorId(2L)).thenReturn(sampleCategoriaResponse);
        when(generoClient.buscarGeneroPorId(3L)).thenReturn(sampleGeneroResponse);
        when(libroMapper.toResponse(sampleLibro, sampleAutorResponse, sampleCategoriaResponse, sampleGeneroResponse))
                .thenReturn(sampleLibroResponse);

        //When
        List<LibroResponse> resultado = libroService.buscarLibrosPorCategoria(2L);

        //Then
        assertNotNull(resultado);
        verify(libroRepository).findByIdCategoria(2L);
    }

    @Test
    @DisplayName("TC-06 - Retorna libros filtrados por Genero")
    public void testGetLibrosPorGenero() {
        //Given
        when(libroRepository.findByIdGenero(3L)).thenReturn(List.of(sampleLibro));
        when(autorClient.buscarAutorPorId(1L)).thenReturn(sampleAutorResponse);
        when(categoriaClient.buscarCategoriaPorId(2L)).thenReturn(sampleCategoriaResponse);
        when(generoClient.buscarGeneroPorId(3L)).thenReturn(sampleGeneroResponse);
        when(libroMapper.toResponse(sampleLibro, sampleAutorResponse, sampleCategoriaResponse, sampleGeneroResponse))
                .thenReturn(sampleLibroResponse);

        //When
        List<LibroResponse> resultado = libroService.buscarLibrosPorGenero(3L);

        //Then
        assertNotNull(resultado);
        verify(libroRepository).findByIdGenero(3L);
    }

    @Test
    @DisplayName("TC-07 - Agrega libro exitosamente si las relaciones externas existen")
    public void testAddLibroExitoso() {
        //Given
        when(libroMapper.toEntity(sampleLibroRequest)).thenReturn(sampleLibro);
        when(autorClient.buscarAutorPorId(1L)).thenReturn(sampleAutorResponse);
        when(categoriaClient.buscarCategoriaPorId(2L)).thenReturn(sampleCategoriaResponse);
        when(generoClient.buscarGeneroPorId(3L)).thenReturn(sampleGeneroResponse);
        when(libroRepository.save(any(Libro.class))).thenReturn(sampleLibro);
        when(libroMapper.toResponse(sampleLibro, sampleAutorResponse, sampleCategoriaResponse, sampleGeneroResponse))
                .thenReturn(sampleLibroResponse);

        //When
        LibroResponse resultado = libroService.crearLibro(sampleLibroRequest);

        //Then
        assertNotNull(resultado);
        assertEquals("El Señor de los Anillos", resultado.getTitulo());
        verify(libroRepository).save(any(Libro.class));
    }

    @Test
    @DisplayName("TC-08 - Lanza excepcion al agregar si alguna relacion externa no existe")
    public void testAddLibroExceptionRelacionNoEncontrada() {
        //Given
        when(libroMapper.toEntity(sampleLibroRequest)).thenReturn(sampleLibro);
        when(autorClient.buscarAutorPorId(1L)).thenReturn(null); // Simula que el autor no existe de forma remota

        //When & Then
        assertThrows(NoSuchElementException.class, () -> libroService.crearLibro(sampleLibroRequest));
        verify(libroRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC-09 - Actualiza parcialmente los campos primitivos de un libro")
    public void testUpdateLibroParcial() {
        //Given
        LibroUpdateRequest updateRequest = LibroUpdateRequest.builder()
                .titulo("El Señor de los Anillos: Las Dos Torres")
                .build();

        when(libroRepository.findById(1L)).thenReturn(Optional.of(sampleLibro));
        when(autorClient.buscarAutorPorId(1L)).thenReturn(sampleAutorResponse);
        when(categoriaClient.buscarCategoriaPorId(2L)).thenReturn(sampleCategoriaResponse);
        when(generoClient.buscarGeneroPorId(3L)).thenReturn(sampleGeneroResponse);
        when(libroRepository.save(any(Libro.class))).thenReturn(sampleLibro);
        when(libroMapper.toResponse(sampleLibro, sampleAutorResponse, sampleCategoriaResponse, sampleGeneroResponse))
                .thenReturn(sampleLibroResponse);

        //When
        libroService.actualizarLibro(1L, updateRequest);

        //Then
        assertEquals("El Señor de los Anillos: Las Dos Torres", sampleLibro.getTitulo());
        assertEquals((short) 423, sampleLibro.getNumeroPaginas()); // Se mantiene intacto
        verify(libroRepository).save(sampleLibro);
    }

    @Test
    @DisplayName("TC-10 - Actualiza todos los campos de un libro incluyendo llaves foraneas")
    public void testUpdateLibroCompleto() {
        //Given
        LibroUpdateRequest updateCompleto = LibroUpdateRequest.builder()
                .titulo("Nuevo Titulo")
                .sinopsis("Nueva Sinopsis")
                .numeroPaginas((short) 500)
                .disponible(false)
                .idAutor(10L)
                .idCategoria(20L)
                .idGenero(30L)
                .build();

        AutorResponse nuevoAutor = new AutorResponse(10L, "Nuevo Autor");
        CategoriaResponse nuevaCat = new CategoriaResponse(20L, "Nueva Cat");
        GeneroResponse nuevoGen = new GeneroResponse(30L, "Nuevo Gen");

        when(libroRepository.findById(1L)).thenReturn(Optional.of(sampleLibro));
        when(autorClient.buscarAutorPorId(10L)).thenReturn(nuevoAutor);
        when(categoriaClient.buscarCategoriaPorId(20L)).thenReturn(nuevaCat);
        when(generoClient.buscarGeneroPorId(30L)).thenReturn(nuevoGen);
        when(libroRepository.save(any(Libro.class))).thenReturn(sampleLibro);

        //When
        libroService.actualizarLibro(1L, updateCompleto);

        //Then
        assertEquals("Nuevo Titulo", sampleLibro.getTitulo());
        assertEquals("Nueva Sinopsis", sampleLibro.getSinopsis());
        assertEquals((short) 500, sampleLibro.getNumeroPaginas());
        assertFalse(sampleLibro.getDisponible());
        assertEquals(10L, sampleLibro.getIdAutor());
        assertEquals(20L, sampleLibro.getIdCategoria());
        assertEquals(30L, sampleLibro.getIdGenero());
        verify(libroRepository).save(sampleLibro);
    }

    @Test
    @DisplayName("TC-11 - Lanza excepcion cuando no encuentra libro para modificar")
    public void testUpdateLibroExceptionNoEncontrado() {
        //Given
        LibroUpdateRequest request = LibroUpdateRequest.builder().titulo("Test").build();
        when(libroRepository.findById(99L)).thenReturn(Optional.empty());

        //When & Then
        assertThrows(NoSuchElementException.class, () -> libroService.actualizarLibro(99L, request));
    }

    @Test
    @DisplayName("TC-12 - Elimina un libro por su ID")
    public void testDeleteLibro() {
        //Given
        when(libroRepository.existsById(1L)).thenReturn(true);

        //When
        libroService.eliminarLibro(1L);

        //Then
        verify(libroRepository).deleteById(1L);
    }

    @Test
    @DisplayName("TC-13 - Lanza excepcion al intentar eliminar un libro inexistente")
    public void testDeleteLibroExceptionNoEncontrado() {
        //Given
        when(libroRepository.existsById(99L)).thenReturn(false);

        //When & Then
        assertThrows(NoSuchElementException.class, () -> libroService.eliminarLibro(99L));
        verify(libroRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("TC-14 - Transiciona exitosamente un libro a estado PRESTADO (disponible = false)")
    public void testPrestarLibroExitoso() {
        //Given
        sampleLibro.setDisponible(true); // El libro inicia disponible
        when(libroRepository.findById(1L)).thenReturn(Optional.of(sampleLibro));
        when(libroRepository.save(any(Libro.class))).thenReturn(sampleLibro);
        when(autorClient.buscarAutorPorId(1L)).thenReturn(sampleAutorResponse);
        when(categoriaClient.buscarCategoriaPorId(2L)).thenReturn(sampleCategoriaResponse);
        when(generoClient.buscarGeneroPorId(3L)).thenReturn(sampleGeneroResponse);

        //When
        libroService.prestarLibro(1L);

        //Then
        assertFalse(sampleLibro.getDisponible());
        verify(libroRepository).save(sampleLibro);
    }

    @Test
    @DisplayName("TC-15 - Lanza excepcion al intentar prestar un libro que ya se encuentra arrendado")
    public void testPrestarLibroExceptionYaPrestado() {
        //Given
        sampleLibro.setDisponible(false); // Libro no disponible
        when(libroRepository.findById(1L)).thenReturn(Optional.of(sampleLibro));

        //When & Then
        assertThrows(IllegalStateException.class, () -> libroService.prestarLibro(1L));
        verify(libroRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC-16 - Transiciona exitosamente un libro a estado DISPONIBLE (disponible = true)")
    public void testDevolverLibroExitoso() {
        //Given
        sampleLibro.setDisponible(false); // El libro inicia PRESTADO (false) para poder ser devuelto
        when(libroRepository.findById(1L)).thenReturn(Optional.of(sampleLibro));
        when(libroRepository.save(any(Libro.class))).thenReturn(sampleLibro);
        when(autorClient.buscarAutorPorId(1L)).thenReturn(sampleAutorResponse);
        when(categoriaClient.buscarCategoriaPorId(2L)).thenReturn(sampleCategoriaResponse);
        when(generoClient.buscarGeneroPorId(3L)).thenReturn(sampleGeneroResponse);

        //When
        libroService.devolverLibro(1L);

        //Then
        assertTrue(sampleLibro.getDisponible()); // Al devolverse, pasa a true
        verify(libroRepository).save(sampleLibro);
    }

    @Test
    @DisplayName("TC-17 - Lanza excepcion al intentar devolver un libro que ya se encontraba disponible")
    public void testDevolverLibroExceptionEstado() {
        //Given
        sampleLibro.setDisponible(true); // El libro YA está disponible (true), gatilla la regla de excepción
        when(libroRepository.findById(1L)).thenReturn(Optional.of(sampleLibro));

        //When & Then
        assertThrows(IllegalStateException.class, () -> libroService.devolverLibro(1L));
        verify(libroRepository, never()).save(any()); // Jamás debe persistir si falla la regla
    }
}