package cl.duoc.dsy1103.categorias_microservice.client;

import cl.duoc.dsy1103.categorias_microservice.dto.LibroResponse;
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
    public List<LibroResponse> buscarLibrosPorCategoria(Long idCategoria) {
        log.info("Obteniendo lista de libros para la categoria con ID: {}", idCategoria);
        try {
            return libroWebClient.get()
                    .uri("/libros/categoria/{idCategoria}", idCategoria)
                    .retrieve()
                    .bodyToFlux(LibroResponse.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error al obtener libros de categoria con ID: {}. Estado: {}", idCategoria, ex.getStatusCode());
            switch (ex.getStatusCode().value()) {
                case 404 -> {
                    log.warn("No se encontraron libros para la categoria {}, devolviendo lista vacía.", idCategoria);
                    return java.util.Collections.emptyList();
                }
                default -> throw new RuntimeException("Error al intentar obtener libros de categoria con ID " + idCategoria, ex);
            }
        } catch (Exception e) {
            log.error("Error inesperado en la comunicación con el microservicio de Libros", e);
            throw new RuntimeException("Error de comunicación remota");
        }
    }
}
