package cl.duoc.dsy1103.libros_microservice.client;

import cl.duoc.dsy1103.libros_microservice.dto.AutorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@Slf4j
public class AutorClient {

    @Autowired
    @Qualifier("autoresWebClient")
    private WebClient autorWebClient;

    public AutorResponse buscarAutorPorId(Long idAutor){
        log.info("Obteniendo autor con ID: {}", idAutor);
        try{
            return autorWebClient.get()
                    .uri("/autores/{idAutor}")
                    .retrieve()
                    .bodyToMono(AutorResponse.class)
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
