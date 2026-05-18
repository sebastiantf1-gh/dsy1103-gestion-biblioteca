package cl.duoc.reservas_microservice.client;

import cl.duoc.reservas_microservice.dto.LibroResponse;
import cl.duoc.reservas_microservice.exception.ForbiddenException;
import cl.duoc.reservas_microservice.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.NoSuchElementException;

@Component
@Slf4j
public class LibroClient {
    @Autowired
    private WebClient webClient;

    @Value("${services.libros.baseUrl}")
    private String baseUrl;

    public LibroResponse obtenerLibroPorId(Long id, String token) {
        log.info("Enviando petición remota a Libros para ID: {}", id);
        try {
            return webClient.get()
                    .uri(baseUrl + "/{id}", id)
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(LibroResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error en comunicación remota con Libros para ID: {}", id, ex);
            switch (ex.getStatusCode().value()) {
                case 401 -> throw new UnauthorizedException("No autorizado para acceder al servicio de usuarios. Token inválido.");
                case 403 -> throw new ForbiddenException("Acceso prohibido al servicio de usuarios. Permisos insuficientes.");
                case 404 -> throw new NoSuchElementException("Usuario no encontrado con ID: " + id);
                default -> throw new RuntimeException("Error al conectar con el servicio de Usuarios");
            }
        }
    }
}
