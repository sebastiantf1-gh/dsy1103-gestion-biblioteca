package cl.duoc.dsy1103.libros_microservice.client;

import cl.duoc.dsy1103.libros_microservice.dto.CategoriaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CategoriaClient {

    @Autowired
    private WebClient categoriasWebClient;

    public CategoriaResponse buscarCategoriaPorId(Long idCategoria){
        log.info("Obteniendo categoria con ID: {}", idCategoria);
        try {
            return categoriasWebClient.get()
                    .uri("/categorias/{idCategoria}", idCategoria)
                    .retrieve()
                    .bodyToMono(CategoriaResponse.class)
                    .onErrorResume(exception -> {
                        log.warn("No se pudo obtener la categoria ID {}. Activando objeto de respaldo.", idCategoria);
                        return Mono.just(new CategoriaResponse(idCategoria, "Categoria no disponible (Eliminada)"));
                    })
                    .block();
        }catch (WebClientResponseException ex){
            log.error("Error al obtener categoria con ID: {}", idCategoria);
            switch (ex.getStatusCode().value()){
                case 404 -> throw new RuntimeException("No se encontro categoria con ID: " + idCategoria);
                default -> throw new RuntimeException("Error al intentar obtener categoria con ID "+ idCategoria, ex);
            }
        }

    }
}
