package cl.duoc.multas_microservice.client;

import cl.duoc.multas_microservice.dto.PrestamoResponse;
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
public class PrestamoClient {

    @Autowired
    @Qualifier("prestamosWebClient") // <-- Vincula con el WebClient del puerto 8089
    private WebClient webClientPrestamos;

    // Eliminamos el String token de la firma del metodo
    public PrestamoResponse obtenerPrestamoPorId(Long id){
        log.info("Verificando existencia del préstamo con ID: {}", id);
        try {
            return webClientPrestamos.get()
                    .uri("/{id}", id) // La URL base ya contiene la ruta completa
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