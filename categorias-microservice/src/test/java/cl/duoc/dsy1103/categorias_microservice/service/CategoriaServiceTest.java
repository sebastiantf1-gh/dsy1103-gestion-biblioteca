package cl.duoc.dsy1103.categorias_microservice.service;

import cl.duoc.dsy1103.categorias_microservice.dto.CategoriaRequest;
import cl.duoc.dsy1103.categorias_microservice.dto.CategoriaResponse;
import cl.duoc.dsy1103.categorias_microservice.dto.CategoriaUpdateRequest;
import cl.duoc.dsy1103.categorias_microservice.mapper.CategoriaMapper;
import cl.duoc.dsy1103.categorias_microservice.model.Categoria;
import cl.duoc.dsy1103.categorias_microservice.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias - CategoriaService.")
@ActiveProfiles("test")
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private CategoriaMapper categoriaMapper;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria sampleCategoria;
    private CategoriaResponse sampleCategoriaResponse;
    private CategoriaRequest sampleCategoriaRequest;

    @BeforeEach
    public void setUp() {
        sampleCategoria = Categoria.builder()
                .id(1L)
                .nombre("Ciencia Ficción")
                .descripcion("Libros basados en desarrollos científicos especulativos y futuros alternativos.")
                .build();

        sampleCategoriaResponse = CategoriaResponse.builder()
                .id(1L)
                .nombre("Ciencia Ficción")
                .descripcion("Libros basados en desarrollos científicos especulativos y futuros alternativos.")
                .build();

        sampleCategoriaRequest = CategoriaRequest.builder()
                .nombre("Ciencia Ficción")
                .descripcion("Libros basados en desarrollos científicos especulativos y futuros alternativos.")
                .build();
    }

    //Listar categorias
    @Test
    @DisplayName("TC-01 - Retorna todas las categorias")
    public void testGetAllCategorias() {
        //Given (dado que)
        when(categoriaRepository.findAll()).thenReturn(List.of(sampleCategoria));
        when(categoriaMapper.toResponse(sampleCategoria)).thenReturn(sampleCategoriaResponse);

        //When (cuando)
        List<CategoriaResponse> resultado = categoriaService.buscarCategorias();

        //Then (entonces)
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Ciencia Ficción", resultado.get(0).getNombre());
        verify(categoriaRepository).findAll();
    }

    //buscar categoria por ID
    @Test
    @DisplayName("TC-02 - Retorna categoria por su ID")
    public void testGetCategoriaPorID() {
        //Given
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(sampleCategoria));
        when(categoriaMapper.toResponse(sampleCategoria)).thenReturn(sampleCategoriaResponse);

        //When
        CategoriaResponse resultado = categoriaService.buscarCategoriaPorId(1L);

        //Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Libros basados en desarrollos científicos especulativos y futuros alternativos.", resultado.getDescripcion());
    }

    //buscar: excepcion cuando no encuentra categoria por su ID
    @Test
    @DisplayName("TC-03 - Lanza excepcion cuando no encuentra categoria por su ID")
    public void testGetExceptionBuscarCategoriaPorID() {
        //Given
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        //When & Then
        assertThrows(NoSuchElementException.class, () -> categoriaService.buscarCategoriaPorId(99L));
    }

    //Agregar categoria
    @Test
    @DisplayName("TC-04 - Agrega categoria")
    public void testAddCategoria() {
        //Given
        when(categoriaMapper.toEntity(sampleCategoriaRequest)).thenReturn(sampleCategoria);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(sampleCategoria);
        when(categoriaMapper.toResponse(sampleCategoria)).thenReturn(sampleCategoriaResponse);

        //When
        CategoriaResponse resultado = categoriaService.crearCategoria(sampleCategoriaRequest);

        //Then
        assertNotNull(resultado);
        assertEquals("Ciencia Ficción", resultado.getNombre());
        verify(categoriaRepository).save(any(Categoria.class));
    }

    //modificar categoria (Parcial)
    @Test
    @DisplayName("TC-05 - Actualiza categoria por su ID")
    public void testUpdateCategoria() {
        //Given
        CategoriaUpdateRequest request = CategoriaUpdateRequest.builder()
                .nombre("Ciencia Ficción Modificada")
                .build();

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(sampleCategoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(sampleCategoria);
        when(categoriaMapper.toResponse(sampleCategoria)).thenReturn(sampleCategoriaResponse);

        //When
        categoriaService.actualizarCategoria(1L, request);

        //Then
        assertEquals("Ciencia Ficción Modificada", sampleCategoria.getNombre());
        assertEquals("Libros basados en desarrollos científicos especulativos y futuros alternativos.", sampleCategoria.getDescripcion()); //no cambia
        verify(categoriaRepository).save(sampleCategoria);
    }

    //modificar: actualiza todos los campos de una categoria
    @Test
    @DisplayName("TC-08 - Actualiza exitosamente todos los campos opcionales de una categoria")
    public void testUpdateAllFieldsCategoria() {
        //Given
        CategoriaUpdateRequest requestCompleto = CategoriaUpdateRequest.builder()
                .nombre("Ciencia Ficción Modificada")
                .descripcion("Nueva descripción técnica para el test de categorías.")
                .build();

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(sampleCategoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(sampleCategoria);
        when(categoriaMapper.toResponse(sampleCategoria)).thenReturn(sampleCategoriaResponse);

        //When
        categoriaService.actualizarCategoria(1L, requestCompleto);

        //Then
        assertEquals("Ciencia Ficción Modificada", sampleCategoria.getNombre());
        assertEquals("Nueva descripción técnica para el test de categorías.", sampleCategoria.getDescripcion());
        verify(categoriaRepository).save(sampleCategoria);
    }

    //modificar: excepcion cuando no encuentra categoria por su ID
    @Test
    @DisplayName("TC-06 - Lanza excepcion cuando no encuentra categoria por su ID para modificar")
    public void testGetExceptionUpdateCategoria() {
        //Given
        CategoriaUpdateRequest request = CategoriaUpdateRequest.builder()
                .nombre("Cualquiera")
                .build();

        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        //When & Then
        assertThrows(NoSuchElementException.class, () -> categoriaService.actualizarCategoria(99L, request));
    }

    //eliminar categoria
    @Test
    @DisplayName("TC-07 - Elimina una categoria por su ID")
    public void testDeleteCategoria() {
        //Given
        when(categoriaRepository.existsById(1L)).thenReturn(true);

        //When
        categoriaService.eliminarCategoria(1L);

        //Then
        verify(categoriaRepository).deleteById(1L);
    }

    //eliminar: excepcion cuando no encuentra categoria por su ID
    @Test
    @DisplayName("TC-09 - Lanza excepcion cuando no encuentra categoria por su ID para eliminar")
    public void testGetExceptionDeleteCategoria() {
        //Given
        when(categoriaRepository.existsById(99L)).thenReturn(false);

        //When & Then
        assertThrows(NoSuchElementException.class, () -> categoriaService.eliminarCategoria(99L));
        verify(categoriaRepository, never()).deleteById(any());
    }
}