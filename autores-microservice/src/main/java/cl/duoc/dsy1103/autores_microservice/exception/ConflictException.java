package cl.duoc.dsy1103.autores_microservice.exception;

public class ConflictException extends RuntimeException {
    // Al ser una excepción de tipo Unchecked (RuntimeException), no obliga a declarar bloques try-catch intermedios en
    // la capa de negocio.
    public ConflictException(String message) {
        super(message);
    }
}
