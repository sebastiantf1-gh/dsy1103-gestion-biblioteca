package cl.duoc.dsy1103.autores_microservice.exception;

import cl.duoc.dsy1103.autores_microservice.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice //Centraliza el manejo de errores interceptando todas las excepciones de los controladores.
                        // Combina la funcionalidad de interceptor de aspectos con @ResponseBody, asegurando el retorno directo en JSON.
@Slf4j //Inyecta de forma nativa la fachada de logs SLF4J para emitir trazas de nivel técnico (ERROR y WARN) ante excepciones operacionales del sistema.
public class GlobalExceptionHandler {

    //Violacion de integridad de datos
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,
            HttpServletRequest request){
        // Loguea el error en consola con nivel ERROR. Registra la causa técnica exacta enviada por Hibernate para facilitar
        // el diagnóstico del equipo de desarrollo.
        log.error("Violacion de integridad de datos: {}", ex.getMessage());

        // Mapeo explícito a HttpStatus.CONFLICT (409) debido a que la transacción colisionó con restricciones físicas reales guardadas en la persistencia de datos
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.name())
                .message("Violacion de integridad de datos")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    //Registro no encontrado
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchElementException(
            NoSuchElementException ex,
            HttpServletRequest request){
        log.error("Registro no encontrado: {}", ex.getMessage());
        // Mapeo semántico estricto REST. Traduce la ausencia lógica del registro a un HTTP 404 Not Found,
        // extrayendo el mensaje personalizado inyectado en la capa de negocio.
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.name())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    //Error de validacion
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        log.error("Error de validacion: {}", ex.getMessage());
        // Atrapa de forma directa las fallas del validador declaradas en AutorRequest/AutorUpdateRequest.
        // Retorna un estado HTTP 400 Bad Request que frena la solicitud antes de procesar lógica en el Service,
        // blindando la integridad de la API.
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.name())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    //Error interno del servidor
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request){
        log.error("Error interno del servidor: {}", ex.getMessage());
        // si ocurre un quiebre de código imprevisto, este método intercepta el hilo de ejecución para evitar desplegar
        // la pila interna del servidor, respondiendo de forma consistente con un código HTTP 500 estructurado que
        // protege el código fuente de exposición.
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    //Acceso prohibido
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiErrorResponse> handleForbiddenException(
            ForbiddenException ex,
            HttpServletRequest request){
        log.error("Acceso prohibido: {}", ex.getMessage());
        // Mapea la falta de privilegios del usuario a un código HTTP 403 Forbidden.
        // Permite notificar al cliente autenticado de forma limpia que carece de autorizaciones del dominio de negocio.
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.name())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    //No autorizado
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse>handleUnauthorizedException(
            UnauthorizedException ex,
            HttpServletRequest request){
        log.error("No autorizado: {}", ex.getMessage());
        // mapea la falla de identidad o token expirado detectado en los flujos de autenticación
        // a un código de red HTTP 401 Unauthorized, estandarizando las respuestas de seguridad con las del negocio.
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.name())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    //Conflicto por existencia
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleConflictException(
            ConflictException ex,
            HttpServletRequest request){
        // Registro de log específico parametrizado para auditorías de consistencia lógica del negocio.
        log.error("Autor ya existe: {} ", ex.getMessage());

        // Mapeo de reglas de negocio cruzadas a nivel lógico (ej. registros duplicados controlados)
        // devolviendo un código HTTP 409 Conflict estructurado de manera uniforme.
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.name())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
