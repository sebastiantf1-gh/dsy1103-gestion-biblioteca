package cl.duoc.dsy1103.autores_microservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${services.libros.url}")
    private String librosServiceUrl;

    @Bean
    public WebClient librosWebClient(){
        return WebClient.builder()
                .baseUrl(librosServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
