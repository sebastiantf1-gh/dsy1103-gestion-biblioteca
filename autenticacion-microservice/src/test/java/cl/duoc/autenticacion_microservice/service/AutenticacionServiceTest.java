package cl.duoc.autenticacion_microservice.service;

import cl.duoc.autenticacion_microservice.dto.AutenticacionResponse;
import cl.duoc.autenticacion_microservice.dto.CrearUsuarioRequest;
import cl.duoc.autenticacion_microservice.dto.LoginRequest;
import cl.duoc.autenticacion_microservice.model.UsuarioPersonal;
import cl.duoc.autenticacion_microservice.repository.UsuarioPersonalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AutenticacionServiceTest {

    @Mock
    private UsuarioPersonalRepository usuarioRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AutenticacionService autenticacionService;

    private UsuarioPersonal usuario;
    private LoginRequest loginRequest;
    private CrearUsuarioRequest crearUsuarioRequest;

    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(autenticacionService, "jwtExpiration", 86400000L);

        usuario = UsuarioPersonal.builder()
                .idPersonalBiblioteca(1L)
                .nombreUsuario("camiloAdmin")
                .password("passwordCifrado123")
                .email("camilo@duocuc.cl")
                .build();

        loginRequest = new LoginRequest("camiloAdmin", "password123");

        crearUsuarioRequest = new CrearUsuarioRequest(
                "nuevoUsuario", "password123", "nuevo@duocuc.cl", "+56912345678"
        );
    }

    // TESTS PARA LOGIN


    @Test
    void login_exitoso_retornaTokenYDatos() {
        // Given
        when(usuarioRepository.existsByNombreUsuario("camiloAdmin")).thenReturn(true);
        when(usuarioRepository.findByNombreUsuario("camiloAdmin")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("password123", "passwordCifrado123")).thenReturn(true);
        when(jwtService.generateToken(usuario)).thenReturn("mocked-jwt-token");

        // When
        AutenticacionResponse resultado = autenticacionService.login(loginRequest);

        // Then
        assertNotNull(resultado);
        assertEquals("mocked-jwt-token", resultado.getToken());
        assertEquals("camiloAdmin", resultado.getNombreUsuario());
        assertEquals(86400000L, resultado.getExpiresIn());
        verify(usuarioRepository).findByNombreUsuario("camiloAdmin");
    }

    @Test
    void login_fallido_usuarioNoExiste_lanzaBadCredentials() {
        // Given
        when(usuarioRepository.existsByNombreUsuario("camiloAdmin")).thenReturn(false);

        // When / Then
        assertThrows(BadCredentialsException.class, () -> {
            autenticacionService.login(loginRequest);
        });
        verify(usuarioRepository, never()).findByNombreUsuario(anyString());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void login_fallido_contrasenaIncorrecta_lanzaBadCredentials() {
        // Given
        when(usuarioRepository.existsByNombreUsuario("camiloAdmin")).thenReturn(true);
        when(usuarioRepository.findByNombreUsuario("camiloAdmin")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("password123", "passwordCifrado123")).thenReturn(false);

        // When / Then
        assertThrows(BadCredentialsException.class, () -> {
            autenticacionService.login(loginRequest);
        });
        verify(jwtService, never()).generateToken(any());
    }

    // TESTS PARA REGISTRO

    @Test
    void registrarUsuario_exitoso_guardaYRetornaMensaje() {
        // Given
        when(usuarioRepository.existsByEmail("nuevo@duocuc.cl")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPasswordXYZ");
        when(usuarioRepository.save(any(UsuarioPersonal.class))).thenReturn(usuario);

        // When
        String resultado = autenticacionService.registrarUsuario(crearUsuarioRequest);

        // Then
        assertNotNull(resultado);
        assertEquals("Usuario registrado exitosamente", resultado);
        verify(usuarioRepository).save(any(UsuarioPersonal.class));
    }

    @Test
    void registrarUsuario_fallido_emailYaExiste_lanzaRuntimeException() {
        // Given
        when(usuarioRepository.existsByEmail("nuevo@duocuc.cl")).thenReturn(true);

        // When / Then
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            autenticacionService.registrarUsuario(crearUsuarioRequest);
        });
        assertEquals("El email ya está registrado", excepcion.getMessage());
        verify(usuarioRepository, never()).save(any(UsuarioPersonal.class));
    }
}