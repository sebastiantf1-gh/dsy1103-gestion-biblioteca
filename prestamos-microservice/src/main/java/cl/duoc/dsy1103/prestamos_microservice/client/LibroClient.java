package cl.duoc.dsy1103.prestamos_microservice.client;

import cl.duoc.dsy1103.prestamos_microservice.dto.LibroResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
@Slf4j
public class LibroClient {
    @Autowired
    private WebClient libroWebClient;

    // 1. Obtener detalles del libro (Para validar existencia y mostrar título/autor)
    public LibroResponse buscarLibroPorId(Long idLibro) {
        log.info("Consultando datos del libro ID: {}", idLibro);
        return libroWebClient.get()
                .uri("/libros/{id}", idLibro)
                .retrieve()
                .bodyToMono(LibroResponse.class)
                .block();
    }

    // 2. Marcar como prestado (Cambia disponible a false)
    public LibroResponse marcarComoPrestado(Long idLibro) {
        log.info("Solicitando marcar libro ID: {} como prestado", idLibro);
        return libroWebClient.patch()
                .uri("/libros/{id}/prestamo", idLibro)
                .retrieve()
                .bodyToMono(LibroResponse.class)
                .block();
    }

    // 3. Marcar como devuelto (Cambia disponible a true)
    public LibroResponse marcarComoDevuelto(Long idLibro) {
        log.info("Solicitando marcar libro ID: {} como disponible", idLibro);
        return libroWebClient.patch()
                .uri("/libros/{id}/devolucion", idLibro)
                .retrieve()
                .bodyToMono(LibroResponse.class)
                .block();
    }
}
