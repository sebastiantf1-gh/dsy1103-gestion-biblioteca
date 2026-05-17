package cl.duoc.autenticacion_microservice.service;

import cl.duoc.autenticacion_microservice.dto.AutenticacionResponse;
import cl.duoc.autenticacion_microservice.dto.LoginRequest;
import cl.duoc.autenticacion_microservice.model.UsuarioPersonal;
import cl.duoc.autenticacion_microservice.repository.UsuarioPersonalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static reactor.netty.http.HttpConnectionLiveness.log;

@Service
public class AutenticacionService {
    @Autowired
    private UsuarioPersonalRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public AutenticacionResponse login(LoginRequest request) {
        //Valida que el usuario exista
        if (!usuarioRepository.existsByNombreUsuario(request.getNombreUsuario())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }
        //Buscamos al usuario por nombre
        UsuarioPersonal usuario = usuarioRepository.findByNombreUsuario(request.getNombreUsuario()).get();
        //Validamos la contraseña
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            log.warn("Contraseña incorrecta para el usuario: {}", usuario.getNombreUsuario());
            throw new BadCredentialsException("Credenciales inválidas");
        }
        //Generamos el token para el usuario
        String token = jwtService.generateToken(usuario);
        //Retornamos el token
        return AutenticacionResponse.builder()
                .token(token)
                .nombreUsuario(usuario.getNombreUsuario())
                .expiresIn(jwtExpiration)
                .build();
    }
}
