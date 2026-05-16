package cl.duoc.dsy1103.resennas_microservice.client;

import cl.duoc.dsy1103.resennas_microservice.dto.LibroResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@Component
@Slf4j
public class LibroClient {

    @Autowired
    private WebClient webClientLibros;






    public LibroResponse obtenerLibrosPorId(Long id){
        log.info("Verificando existencia del libro con ID: {}", id);
        return webClientLibros.get()
                .uri("/{id}",id)
                .retrieve()
                .bodyToMono(LibroResponse.class)
                .onErrorResume(ex->{
                    log.error("Error al obtener el libro ID: {}", id);
                    LibroResponse fallback = new LibroResponse();
                    fallback.setTitulo("El libro no está disponible o fue eliminado");
                    return Mono.just(fallback);
                })
                .block();

    }
}
