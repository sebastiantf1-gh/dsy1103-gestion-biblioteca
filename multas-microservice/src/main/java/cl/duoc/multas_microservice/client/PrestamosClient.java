package cl.duoc.multas_microservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.NoSuchElementException;

@Component
@Slf4j
public class PrestamosClient {
    @Autowired
    private WebClient webClientPrestamos; // Asegúrate de tener este bean configurado

    public PrestamoResponse obtenerPrestamoPorId(Long id, String token){
        log.info("Verificando existencia del préstamo con ID: {}", id);
        try {
            return webClientPrestamos.get()
                    .uri("/{id}", id)
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(PrestamoResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error al obtener información del Préstamo con ID: {}", id);
            if (ex.getStatusCode().value() == 404) {
                throw new NoSuchElementException("Préstamo no encontrado con ID: " + id);
            }
            throw new RuntimeException("Error al conectar con servicio de préstamos");
        }
    }
}
