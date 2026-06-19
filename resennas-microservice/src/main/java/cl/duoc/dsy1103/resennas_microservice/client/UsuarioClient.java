package cl.duoc.dsy1103.resennas_microservice.client;


import cl.duoc.dsy1103.resennas_microservice.dto.LibroResponse;
import cl.duoc.dsy1103.resennas_microservice.dto.UsuarioResponse;

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
public class UsuarioClient {

    @Autowired
    @Qualifier("usuariosWebClient") // <-- Vincula al bean con puerto 8081 e interceptor
    private WebClient webClient;

    public UsuarioResponse obtenerUsuarioPorId(Long id) {
        log.info("Consultando datos del usuario ID: {}", id);
        try {
            return webClient.get()
                    .uri("/{id}", id) // La URL base ya contiene la ruta completa
                    .retrieve()
                    .bodyToMono(UsuarioResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error al obtener información del Usuario con ID: {}", id);
            if (ex.getStatusCode().value() == 404) {
                throw new NoSuchElementException("Usuario no encontrado con ID: " + id);
            }
            throw new RuntimeException("Error al conectar con servicio de usuarios");
        }
    }
}