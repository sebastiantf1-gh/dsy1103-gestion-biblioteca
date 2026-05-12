package cl.duoc.dsy1103.resennas_microservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${services.libros.url}")
    private String librosServiceUrl;

    @Value("${services.usuarios.url}")
    private String usuariosServiceUrl;

    @Bean
    public WebClient webClientLibros(){
        return WebClient.builder()
                .baseUrl(librosServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Bean
    public WebClient webClientUsuarios(){
        return WebClient.builder()
                .baseUrl(usuariosServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
