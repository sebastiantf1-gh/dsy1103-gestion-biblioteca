package cl.duoc.dsy1103.libros_microservice.client;

import cl.duoc.dsy1103.libros_microservice.dto.AutorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AutorClient {

    @Autowired
    private WebClient autoresWebClient;

    public AutorResponse buscarAutorPorId(Long idAutor){
        log.info("Obteniendo autor con ID: {}", idAutor);
        try{
            return autoresWebClient.get()
                    .uri("/autores/{idAutor}", idAutor)
                    .retrieve()
                    .bodyToMono(AutorResponse.class)
                    .onErrorResume(exception -> {
                        log.warn("No se pudo obtener el autor ID {}. Activando objeto de respaldo.", idAutor);
                        return Mono.just(new AutorResponse(idAutor, "Autor no disponible (Eliminado)"));
                    })
                    .block();

        }catch (WebClientResponseException ex) {
            log.error("Error al obtener autor con ID: {}", idAutor);
            switch (ex.getStatusCode().value()){
                case 404 -> throw new RuntimeException("No se encontró autor con ID " + idAutor);
                default -> throw new RuntimeException("Error al intentar obtener autor con ID "+ idAutor, ex);
            }
        }
    }


}
