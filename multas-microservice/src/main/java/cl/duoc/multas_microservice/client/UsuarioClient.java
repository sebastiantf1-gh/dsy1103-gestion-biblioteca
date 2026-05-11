package cl.duoc.multas_microservice.client;

import cl.duoc.multas_microservice.exception.ForbiddenException;
import cl.duoc.multas_microservice.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.NoSuchElementException;

@Component
@Slf4j
public class UsuarioClient {

    @Autowired
    private WebClient webClient;

    /**
     * Obtiene la info de un usuario por medio de su id
     * Se realiza una llamada http GET al servicio de usuario para obtener sus datos.
     */
    @Value("${services.usuarios.baseUrl}")
    private String baseUrl;

    public UsuarioResponse obtenerUsuarioPorId(Long id, String token){
        log.info("Obteniendo información del paciente con ID: {}", id);
        try{
            return webClient.get()
                    .uri("/id/{id}",id).header("Authorization",token)
                    .retrieve()
                    .bodyToMono(UsuarioResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error al obtener información del Usuario con ID: {}", id, ex);
            switch (ex.getStatusCode().value()){
                case 401 -> throw new UnauthorizedException
                        ("No autorizado para acceder al servicio de usuarios al obtener usuario con ID: "+id);
                case 403 -> throw new ForbiddenException
                        ("Acceso prohibido al servicio de Usuarios al obtener usuario con ID: "+id);
                case 404 -> throw new NoSuchElementException
                        ("Usuario no encontrado con ID: " + id);
                default -> throw new RuntimeException("Error interno del servicio de usuario al obtener usuario con ID: " + id);
            }
        }
    }
}
