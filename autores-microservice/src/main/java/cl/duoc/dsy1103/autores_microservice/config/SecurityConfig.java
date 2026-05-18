package cl.duoc.dsy1103.autores_microservice.config;

import cl.duoc.dsy1103.autores_microservice.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Indica a Spring que esta clase es una fuente de definiciones de Beans de configuración que serán
                // procesadas por el contenedor IoC al arrancar la aplicación.
@EnableWebSecurity// Habilita de forma explícita el soporte de seguridad web de Spring Security en este microservicio.
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean//Registra el filtro de seguridad en el contenedor de Spring.
        // El framework lo utilizará automáticamente para interceptar y evaluar todo el tráfico HTTP entrante al puerto del microservicio
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                // Se desactiva CSRF (Cross-Site Request Forgery)
                // porque la comunicación entre microservicios no utiliza cookies de sesión, sino tokens JWT en las cabeceras.
                // Esto blinda y optimiza el rendimiento de la API distribuida.
                .csrf(csrf -> csrf.disable())
                // Se configura la gestión de sesiones como 'STATELESS' (Sin Estado). El servidor jamás guardará información
                // de sesiones ni estados del cliente en memoria; cada petición debe ser autónoma y auto-contenida con su token JWT.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Postura de seguridad restrictiva estándar (Principio de Menor Privilegio). '.anyRequest().authenticated()'
                // establece que absolutamente todos los endpoints expuestos en este microservicio requieren autenticación
                // previa para denegar accesos anónimos por defecto.
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                //Integración del Filtro Personalizado. Inyecta nuestro 'jwtAuthFilter' justo ANTES del filtro estándar
                // de autenticación de Java ('UsernamePasswordAuthenticationFilter'). De esta forma, interceptamos el token,
                // validamos la firma y poblamos el contexto de seguridad antes de evaluar la petición.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}
