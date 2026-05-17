package cl.duoc.dsy1103.resennas_microservice.security;

import cl.duoc.dsy1103.resennas_microservice.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter)
        throws ServletException, IOException{

        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filter.doFilter(request, response);
            return;
        }

        // Quita la palabra 'Bearer ' para quedarse  con el string del token JWT
        String token = authHeader.substring(7);

        //Si es que el JwtService valida el token, saca los datos y da el permiso
        if(jwtService.isTokenValid(token)){
            Claims claims = jwtService.extractClaims(token);
            //Le asigne el "ROLE_USER" para pasar la seguridad de Spring
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    claims.getSubject(), null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
            //Guarda el usuario autenticado
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Token valido para resennas: {}", claims.getSubject());
        }else {
            log.warn("Token invalido en solicitud a: {}", request.getRequestURI());
        }
        filter.doFilter(request, response);

    }
}
