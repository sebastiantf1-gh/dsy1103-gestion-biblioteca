package cl.duoc.dsy1103.resennas_microservice.client;

import cl.duoc.dsy1103.resennas_microservice.dto.LibroResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@Component
@Slf4j
public class LibroClient {

    @Autowired
    @Qualifier("librosWebClient") // <-- Vincula al bean con puerto 8085 e interceptor
    private WebClient webClientLibros;

    public LibroResponse obtenerLibrosPorId(Long id) {
        log.info("Consultando datos del libro ID: {}", id);
        try {
            return webClientLibros.get()
                    .uri("/{id}", id) // La URL base ya contiene la ruta completa
                    .retrieve()
                    .bodyToMono(LibroResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error al obtener información del Libro con ID: {}", id);
            if (ex.getStatusCode().value() == 404) {
                throw new NoSuchElementException("Libro no encontrado con ID: " + id);
            }
            throw new RuntimeException("Error al conectar con servicio de libros");
        }
    }
}