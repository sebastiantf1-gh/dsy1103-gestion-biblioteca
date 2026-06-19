package cl.duoc.dsy1103.resennas_microservice.config;

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

    @Value("${services.libros.baseUrl}")
    private String librosBaseUrl;

    // Interceptor perimetral que hereda el token JWT de Postman a las llamadas internas
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
    public WebClient usuariosWebClient() {
        return WebClient.builder()
                .baseUrl(usuariosBaseUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .filter(bearerTokenInterceptor())
                .build();
    }

    @Bean
    public WebClient librosWebClient() {
        return WebClient.builder()
                .baseUrl(librosBaseUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .filter(bearerTokenInterceptor())
                .build();
    }
}