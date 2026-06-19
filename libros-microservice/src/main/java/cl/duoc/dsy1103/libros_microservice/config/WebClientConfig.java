package cl.duoc.dsy1103.libros_microservice.config;

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
    @Value("${services.autores.url}")
    private String autoresServiceUrl;

    @Value("${services.categorias.url}")
    private String categoriasServiceUrl;

    @Value("${services.generos.url}")
    private String generosServiceUrl;

    // Interceptor perimetral que clona de forma transparente el Token JWT
    // recibido desde Postman y lo inyecta en las peticiones internas.
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

    @Bean
    public WebClient autoresWebClient(){
        return WebClient.builder()
                .baseUrl(autoresServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .filter(bearerTokenInterceptor()) // <-- Inyectamos el propagador de seguridad
                .build();
    }

    @Bean
    public WebClient categoriasWebClient(){
        return WebClient.builder()
                .baseUrl(categoriasServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .filter(bearerTokenInterceptor()) // <-- Inyectamos el propagador de seguridad
                .build();
    }

    @Bean
    public WebClient generosWebClient(){
        return WebClient.builder()
                .baseUrl(generosServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .filter(bearerTokenInterceptor()) // <-- Inyectamos el propagador de seguridad
                .build();
    }
}