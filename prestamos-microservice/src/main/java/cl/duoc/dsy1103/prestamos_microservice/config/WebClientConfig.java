package cl.duoc.dsy1103.prestamos_microservice.config;

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
    @Value("${services.usuarios.url}")
    private String usuariosServiceUrl;

    @Value("${services.libros.url}")
    private String librosServiceUrl;

    // Filtro interceptor que rescata el Token JWT de la solicitud original de Postman
    // y lo clona de manera transparente en la petición interna hacia el otro microservicio
    private ExchangeFilterFunction bearerTokenInterceptor() {
        return (request, next) -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest httpRequest = attributes.getRequest();
                String authHeader = httpRequest.getHeader("Authorization");

                // Si existe un token Bearer en la llamada de origen, lo inyectamos en la llamada saliente
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
    public WebClient usuariosWebClient(){
        return WebClient.builder()
                .baseUrl(usuariosServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .filter(bearerTokenInterceptor()) // <-- Agregamos el propagador de seguridad
                .build();
    }

    @Bean
    public WebClient librosWebClient(){
        return WebClient.builder()
                .baseUrl(librosServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .filter(bearerTokenInterceptor()) // <-- Agregamos el propagador de seguridad
                .build();
    }

}