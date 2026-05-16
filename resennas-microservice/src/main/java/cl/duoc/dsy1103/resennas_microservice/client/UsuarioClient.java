package cl.duoc.dsy1103.resennas_microservice.client;


import cl.duoc.dsy1103.resennas_microservice.dto.LibroResponse;
import cl.duoc.dsy1103.resennas_microservice.dto.UsuarioResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@Component
@Slf4j
public class UsuarioClient {

    @Autowired
    private WebClient webClientUsuarios;





    public UsuarioResponse obtenerUsuarioPorId(Long id){
        log.info("Verificando existencia del usuarios con ID: {}", id);

        return  webClientUsuarios.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(UsuarioResponse.class)
                .onErrorResume(ex -> {
                    log.error("Error al obtener usuario ID: {}", id);
                    UsuarioResponse fallback = new UsuarioResponse();
                    fallback.setNombreCompleto("El usuario no está disponible o fue eliminado");
                    return Mono.just(fallback);
                })
                .block();
    }
}
