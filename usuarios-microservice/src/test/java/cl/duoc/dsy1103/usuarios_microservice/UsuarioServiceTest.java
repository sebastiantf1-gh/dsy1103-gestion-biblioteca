package cl.duoc.dsy1103.usuarios_microservice;


import cl.duoc.dsy1103.usuarios_microservice.dto.UsuarioRequest;
import cl.duoc.dsy1103.usuarios_microservice.dto.UsuarioResponse;
import cl.duoc.dsy1103.usuarios_microservice.dto.UsuarioUpdateRequest;
import cl.duoc.dsy1103.usuarios_microservice.mapper.UsuarioMapper;
import cl.duoc.dsy1103.usuarios_microservice.model.Usuario;
import cl.duoc.dsy1103.usuarios_microservice.repository.UsuarioRepository;
import cl.duoc.dsy1103.usuarios_microservice.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioResponse usuarioResponse;
    private UsuarioRequest usuarioRequest;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .nombreCompleto("Benja Min")
                .email("Benjam@email.com")
                .telefono("948111544")
                .fechaRegistro(LocalDateTime.now())
                .build();

        usuarioResponse = UsuarioResponse.builder()
                .id(1L)
                .nombreCompleto("Benja Min")
                .email("Benjam@email.com")
                .telefono("948111544")
                .fechaRegistro(usuario.getFechaRegistro())
                .build();

        usuarioRequest = UsuarioRequest.builder()
                .nombreCompleto("Benja Min")
                .email("Benjam@email.com")
                .telefono("948111544")
                .build();
    }

    //ListarUsuarios
    @Test
    void listarUsuarios_retornaListaDeUsuarios() {
        //Given (dado que...)
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        when(usuarioMapper.toResponse(usuario)).thenReturn(usuarioResponse);

        //When (cuando...)
        List<UsuarioResponse> resultado = usuarioService.listarUsuarios();

        //Then (entonces...)
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Benja Min", resultado.get(0).getNombreCompleto());
        verify(usuarioRepository).findAll();
    }

    //buscarUsuarioPorId
    @Test
    void buscarUsuarioPorId_retornaUsuarioCuandoExiste() {
        //Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toResponse(usuario)).thenReturn(usuarioResponse);

        //When
        UsuarioResponse resultado = usuarioService.buscarUsuarioPorId(1L);

        //Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Benjam@email.com", resultado.getEmail());
    }
    @Test
    void buscarUsuarioPorId_lanzaExcepcionCunadoNoExiste() {
        //Given
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        //When / Then
        assertThrows(NoSuchElementException.class,
                ()-> usuarioService.buscarUsuarioPorId(99L));
    }

    //CrearUsuario
    @Test
    void crearUsuario_guardaYRetornaUsuario() {
        //Given
        when(usuarioMapper.fromRequest(usuarioRequest)).thenReturn(usuario);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toResponse(usuario)).thenReturn(usuarioResponse);

        //When
        UsuarioResponse resultado = usuarioService.crearUsuario(usuarioRequest);

        //Then
        assertNotNull(resultado);
        assertEquals("Benja Min", resultado.getNombreCompleto());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_asignaFechaRegistroAutomaticamente() {
        //Given
        Usuario usuarioSinFecha = Usuario.builder()
                .nombreCompleto("Benja Min")
                .email("Benjam@email.com")
                .build();

        when(usuarioMapper.fromRequest(usuarioRequest)).thenReturn(usuarioSinFecha);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSinFecha);
        when(usuarioMapper.toResponse(usuarioSinFecha)).thenReturn(usuarioResponse);

        //When
        usuarioService.crearUsuario(usuarioRequest);

        //Then
        assertNotNull(usuarioSinFecha.getFechaRegistro());

    }

    //modificarUsuario
    @Test
    void modificarUsuario_actualizaSoloCamposNoNUlos() {
        //Given
        UsuarioUpdateRequest request = UsuarioUpdateRequest.builder()
                .nombreCompleto("Benja Modificado")
                .build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toResponse(usuario)).thenReturn(usuarioResponse);

        //When
        usuarioService.modificarUsuario(1L, request);

        //Then
        assertEquals("Benja Modificado", usuario.getNombreCompleto());
        assertEquals("948111544", usuario.getTelefono()); // no cambia
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void modificarUsuario_lanzaExcepcionSiUsuarioNoExiste() {
        //Given
        UsuarioUpdateRequest request = UsuarioUpdateRequest.builder()
                .nombreCompleto("Cualquiera")
                .build();

        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        //When / Then
        assertThrows(NoSuchElementException.class,
                ()-> usuarioService.modificarUsuario(99L, request));
    }

    //eliminarUsuario
    @Test
    void eliminarUsuario_eliminarCorrectamenteCuandoExiste() {
        //Given
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        //When
        usuarioService.eliminarUsuario(1L);

        //Then
        verify(usuarioRepository).deleteById(1L);
    }
    @Test
    void eliminarUsuario_lanzaExcepcionSiNoExiste(){
        //Given
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        //When / Then
        assertThrows(NoSuchElementException.class,
                ()-> usuarioService.eliminarUsuario(99L));
        verify(usuarioRepository, never()).deleteById(any());
    }
}
