package cl.duoc.autenticacion_microservice.security;

import cl.duoc.autenticacion_microservice.model.UsuarioPersonal;
import cl.duoc.autenticacion_microservice.repository.UsuarioPersonalRepository;
import cl.duoc.autenticacion_microservice.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioPersonalRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter)
            throws ServletException, IOException {

        // 1. Obtener la ruta de la solicitud
        String path = request.getServletPath();
        log.info("JwtAuthenticationFilter analizando la ruta: {}", path);

        // 2. ¡LA SOLUCIÓN! Si la ruta va al Auth (Login o Register), saltarse el filtro por completo
        if (path.contains("/v1/auth")) {
            filter.doFilter(request, response);
            return; // Detiene la ejecución del filtro aquí para que no siga bajando
        }

        String authHeader = request.getHeader("Authorization");

        // 3. Si no hay token en las demás rutas privadas, pasamos el control a Spring Security
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filter.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            if (jwtService.isTokenValid(token)) {
                Claims claims = jwtService.extractClaims(token);

                // 4. Control de nulos seguro para evitar el NullPointerException
                Object nameClaim = claims.get("name");
                if (nameClaim == null) {
                    log.warn("El token no contiene el campo 'name'. Access denied.");
                    filter.doFilter(request, response);
                    return;
                }

                String nombre = nameClaim.toString();
                if (!repository.existsByNombreUsuario(nombre)) {
                    log.warn("Token válido para un usuario que ya no existe en la BD: {}", nombre);
                    filter.doFilter(request, response);
                    return;
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(), null, List.of());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Token autenticado exitosamente para: {}", claims.getSubject());
            } else {
                log.warn("Token inválido recibido en: {}", request.getRequestURI());
            }
        } catch (Exception e) {
            log.error("Error crítico procesando el Token JWT: {}", e.getMessage());
        }

        // 5. Continuar la cadena si el token es impecable
        filter.doFilter(request, response);
    }
}
