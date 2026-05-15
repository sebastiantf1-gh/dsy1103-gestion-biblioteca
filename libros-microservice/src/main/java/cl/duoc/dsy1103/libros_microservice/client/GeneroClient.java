package cl.duoc.dsy1103.libros_microservice.client;

import cl.duoc.dsy1103.libros_microservice.dto.GeneroResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@Slf4j
public class GeneroClient {

    @Autowired
    @Qualifier("generosWebClient")
    private WebClient generoWebClient;

    public GeneroResponse buscarGeneroPorId(Long idGenero){
        log.info("Obteniendo genero con ID: {}", idGenero);
        try {
            return generoWebClient.get()
                    .uri("/generos/{idGenero}")
                    .retrieve()
                    .bodyToMono(GeneroResponse.class)
                    .block();
        }catch (WebClientResponseException ex){
            log.error("Error al obtener genero con ID: {}", idGenero);
            switch (ex.getStatusCode().value()){
                case 404 -> throw new RuntimeException("No se encontro genero con ID: " + idGenero);
                default -> throw new RuntimeException("Error al intentar obtener genero con ID "+ idGenero, ex);
            }
        }

    }
}
