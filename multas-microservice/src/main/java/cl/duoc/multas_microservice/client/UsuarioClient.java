package cl.duoc.multas_microservice.client;

import cl.duoc.multas_microservice.dto.UsuarioResponse;
import cl.duoc.multas_microservice.exception.ForbiddenException;
import cl.duoc.multas_microservice.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.NoSuchElementException;

@Component
@Slf4j
public class UsuarioClient {

    @Autowired
    @Qualifier("usuariosWebClient") // <-- Vincula con el WebClient del puerto 8081
    private WebClient webClient;

    // Eliminamos la variable local @Value que estaba de adorno
    // Eliminamos el String token de la firma del método
    public UsuarioResponse obtenerUsuarioPorId(Long id){
        log.info("Obteniendo información del usuario con ID: {}", id);
        try{
            return webClient.get()
                    .uri("/{id}", id) // La URL base ya contiene la ruta completa
                    .retrieve()
                    .bodyToMono(UsuarioResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error al obtener información del Usuario con ID: {}", id, ex);
            switch (ex.getStatusCode().value()){
                case 401 -> throw new UnauthorizedException("No autorizado para acceder al servicio de usuarios.");
                case 403 -> throw new ForbiddenException("Acceso prohibido al servicio de Usuarios.");
                case 404 -> throw new NoSuchElementException("Usuario no encontrado con ID: " + id);
                default -> throw new RuntimeException("Error interno del servicio de usuario.");
            }
        }
    }
}