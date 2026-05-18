package cl.duoc.dsy1103.autores_microservice.security;

import cl.duoc.dsy1103.autores_microservice.service.JwtService;
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

@Component //Registra el filtro como un componente inyectable dentro del ecosistema de Spring, permitiendo su mapeo explícito
            // en la cadena de filtros configurada en SecurityConfig.
@Slf4j //Habilita el logging estructurado de SLF4J para auditar en tiempo real qué usuarios se autentican con éxito y qué peticiones se rechazan por credenciales corruptas (IE 2.3.2).
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter)
            throws ServletException, IOException {
        //Extrae el valor de la cabecera estándar 'Authorization' enviada por el cliente (ej: Postman) desde los metadatos HTTP.
        String authHeader = request.getHeader("Authorization");
        // Control de flujo defensivo inicial. Si la cabecera es nula o no sigue el estándar internacional "Bearer ",
        // significa que la petición es anónima o inválida. El filtro aborta su ejecución interna y delega la petición
        // al siguiente eslabón de la cadena mediante 'filter.doFilter()', permitiendo que Spring Security decida si rechaza
        // el acceso con un 401/403.
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filter.doFilter(request, response);
            return;
        }
        // Operación de extracción de string. Al aplicar '.substring(7)', removemos la palabra "Bearer "
        // junto con su espacio en blanco (7 caracteres en total), aislando puramente la cadena cifrada del token JWT que procesará el servicio.
        String token = authHeader.substring(7);

        if(jwtService.isTokenValid(token)){
            // Si el token es legítimo, se extraen de forma segura los reclamos de identidad (Claims).
            Claims claims = jwtService.extractClaims(token);
            // Se construye el token de autenticación oficial de Spring Security, pasando el nombre de usuario (Subject)
            // como credencial principal y una lista vacía de roles/autoridades (por el momento).
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    claims.getSubject(), null, List.of());
            // se registra el objeto de autenticación dentro del 'SecurityContextHolder'.
            // Esto le notifica oficialmente a Spring que la petición actual está plenamente autenticada, abriendo paso seguro hacia el Controller.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //Log de nivel INFO estructurado que asocia el éxito de la operación con la identidad del usuario para trazabilidad técnica.
            log.info("Token valido para usuario: {}", claims.getSubject());
        }else {
            // Log defensivo de nivel WARN. Advierte en consola un intento de acceso no autorizado,
            // guardando la URI exacta que fue atacada o consultada de forma fraudulenta.
            log.warn("Token invalido en solicitud a: {}", request.getRequestURI());
        }
        // independiente de si el token fue válido o no, se debe invocar 'filter.doFilter' al final para continuar el ciclo
        // de vida de la petición de red y evitar congelar la conexión del cliente.
        filter.doFilter(request, response);
    }
}
