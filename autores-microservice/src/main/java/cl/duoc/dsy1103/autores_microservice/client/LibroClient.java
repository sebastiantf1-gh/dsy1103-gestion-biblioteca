package cl.duoc.dsy1103.autores_microservice.client;

import cl.duoc.dsy1103.autores_microservice.dto.LibroResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Component
@Slf4j
public class LibroClient {
    @Autowired
    private WebClient libroWebClient;

    // Cambiamos el nombre y el tipo de retorno a List<LibroResponse>
    public List<LibroResponse> buscarLibrosPorAutor(Long idAutor) {
        log.info("Obteniendo lista de libros para el autor con ID: {}", idAutor);
        try {
            return libroWebClient.get()
                    .uri("/libros/autor/{idAutor}", idAutor)
                    .retrieve()
                    .bodyToFlux(LibroResponse.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error al obtener libros del autor ID: {}. Estado: {}", idAutor, ex.getStatusCode());
            switch (ex.getStatusCode().value()) {
                case 404 -> {
                    log.warn("No se encontraron libros para el autor {}, devolviendo lista vacía", idAutor);
                    return java.util.Collections.emptyList();
                }
                default -> throw new RuntimeException("Error al intentar obtener libros del autor ID " + idAutor, ex);
            }
        } catch (Exception e) {
            log.error("Error inesperado en la comunicación con el microservicio de Libros", e);
            throw new RuntimeException("Error de comunicación remota");
        }
    }
}
