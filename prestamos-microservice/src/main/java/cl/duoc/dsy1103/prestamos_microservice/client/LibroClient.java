package cl.duoc.dsy1103.prestamos_microservice.client;

import cl.duoc.dsy1103.prestamos_microservice.dto.LibroResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class LibroClient {
    @Autowired
    private WebClient librosWebClient;

    //obtener detalles del libro (para validar existencia y mostrar título)
    public LibroResponse buscarLibroPorId(Long idLibro) {
        log.info("Consultando datos del libro ID: {}", idLibro);
        return librosWebClient.get()
                .uri("/libros/{id}", idLibro)
                .retrieve()
                .bodyToMono(LibroResponse.class)
                .onErrorResume(exception -> {
                    log.warn("El libro ID {} no existe o fue eliminado del catálogo. Aplicando objeto de respaldo.", idLibro);
                    // Devolvemos un objeto ficticio con el título aclaratorio para que postman no tire 500
                    return Mono.just(new LibroResponse(idLibro, "Libro no disponible (Eliminado)"));
                })
                .block();
    }

    //marcar como prestado (Cambia disponible a false)
    public LibroResponse marcarComoPrestado(Long idLibro) {
        log.info("Solicitando marcar libro ID: {} como prestado", idLibro);
        return librosWebClient.patch()
                .uri("/libros/{id}/prestamo", idLibro)
                .retrieve()
                .bodyToMono(LibroResponse.class)
                .onErrorResume(exception -> {
                    log.error("Error critico: No se pudo actualizar el estado del libro ID {} a prestado.", idLibro);
                    return Mono.error(new IllegalStateException("No se pudo cambiar el estado del libro. Transacción cancelada."));
                })
                .block();
    }

    //marcar como devuelto (Cambia disponible a true)
    public LibroResponse marcarComoDevuelto(Long idLibro) {
        log.info("Solicitando marcar libro ID: {} como disponible", idLibro);
        return librosWebClient.patch()
                .uri("/libros/{id}/devolucion", idLibro)
                .retrieve()
                .bodyToMono(LibroResponse.class)
                .onErrorResume(exception -> {
                    log.error("Error critico: No se pudo actualizar el estado del libro ID {} a disponible.", idLibro);
                    return Mono.error(new IllegalStateException("No se pudo revertir el estado del libro. Transacción cancelada."));
                })
                .block();
    }
}
