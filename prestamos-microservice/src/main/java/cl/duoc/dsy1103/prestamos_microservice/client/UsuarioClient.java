package cl.duoc.dsy1103.prestamos_microservice.client;

import cl.duoc.dsy1103.prestamos_microservice.dto.UsuarioResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class UsuarioClient {

    @Autowired
    private WebClient usuariosWebClient;

    /**
     * Busca un usuario por su ID en el microservicio de Usuarios,
     * para validar que el alumno existe antes de realizar un préstamo.
     */
    public UsuarioResponse buscarUsuarioPorId(Long idUsuario) {
        log.info("Consultando datos del usuario con ID: {}", idUsuario);
        try {
            return usuariosWebClient.get()
                    .uri("/usuarios/{id}", idUsuario)
                    .retrieve()
                    .bodyToMono(UsuarioResponse.class)
                    .onErrorResume(exception -> {
                        log.warn("El usuario ID {} no existe o fue eliminado. Aplicando objeto de respaldo.", idUsuario);
                        // Retornamos el DTO con el formato correcto (camelCase) y el mensaje de aviso
                        return Mono.just(new UsuarioResponse(idUsuario, "Usuario no disponible (Eliminado)"));
                    })
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error al obtener usuario con ID: {}. Estado: {}", idUsuario, ex.getStatusCode());
            switch (ex.getStatusCode().value()) {
                case 404 -> {
                    log.warn("No se encontro usuario con ID: {}.", idUsuario);
                    return null;
                }
                default -> throw new RuntimeException("Error al intentar obtener usuario ID " + idUsuario, ex);
            }
        } catch (Exception e) {
            log.error("Error inesperado en la comunicación con microservicio de Usuarios", e);
            throw new RuntimeException("Error de comunicacion remota");
        }
    }
}