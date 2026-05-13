package cl.duoc.dsy1103.resennas_microservice.client;


import cl.duoc.dsy1103.resennas_microservice.dto.UsuarioResponse;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.NoSuchElementException;

@Component
@Slf4j
public class UsuarioClient {

    @Autowired
    private WebClient webClientUsuarios;

    @Value("${services.usuarios.url}")


    public UsuarioResponse obtenerUsuarioPorId(Long id){
        log.info("Verificando existencia del usuarios con ID: {}", id);
        try{
            return  webClientUsuarios.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .bodyToMono(UsuarioResponse.class)
                    .block();
        }catch (WebClientResponseException ex){
            log.error("Error al obtener información del usuario con ID: {}", id);
            if(ex.getStatusCode().value() == 404){
                throw new NoSuchElementException("Usuario no encontrado con ID: " +id);
            }
            throw new RuntimeException("Error al conectar con servicio de usuarios");
        }
    }
}
