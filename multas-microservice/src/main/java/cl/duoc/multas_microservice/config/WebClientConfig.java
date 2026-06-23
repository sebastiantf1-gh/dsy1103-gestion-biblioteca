package cl.duoc.multas_microservice.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {


    @Value("${services.usuarios.baseUrl}")
    private String usuariosBaseUrl;

    @Value("${services.prestamos.baseUrl}")
    private String prestamosBaseUrl;

    // Interceptor automatizado que clona el token de Postman de forma transparente
    private ExchangeFilterFunction bearerTokenInterceptor() {
        return (request, next) -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest httpRequest = attributes.getRequest();
                String authHeader = httpRequest.getHeader("Authorization");

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    ClientRequest filteredRequest = ClientRequest.from(request)
                            .header("Authorization", authHeader)
                            .build();
                    return next.exchange(filteredRequest);
                }
            }
            return next.exchange(request);
        };
    }

    // Bean específico para el microservicio de Usuarios (Port 8081)
    @Bean
    public WebClient usuariosWebClient() {
        return WebClient.builder()
                .baseUrl(usuariosBaseUrl) // Configura la URL base real
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .filter(bearerTokenInterceptor()) // Agrega seguridad automática
                .build();
    }

    // Bean específico para el microservicio de Préstamos (Port 8089)
    @Bean
    public WebClient prestamosWebClient() {
        return WebClient.builder()
                .baseUrl(prestamosBaseUrl) // Configura la URL base real
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .filter(bearerTokenInterceptor())
                .build();
    }
}