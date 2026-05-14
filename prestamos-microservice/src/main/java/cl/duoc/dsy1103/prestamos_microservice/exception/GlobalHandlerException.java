package cl.duoc.dsy1103.prestamos_microservice.exception;

import cl.duoc.dsy1103.prestamos_microservice.dto.ApiErrorResponse;
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

@RestControllerAdvice
@Slf4j
public class GlobalHandlerException {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,
            HttpServletRequest request){
        log.error("Violacion de integridad de datos: {}", ex.getMessage());
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.name())
                .message("Violacion de integridad de datos")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchElementException(
            NoSuchElementException ex,
            HttpServletRequest request){
        log.error("Registro no encontrado: {}", ex.getMessage());
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.name())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        log.error("Error de validacion: {}", ex.getMessage());
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.name())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request){
        log.error("Error interno del servidor: {}", ex.getMessage());
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiErrorResponse> handleForbiddenException(
            ForbiddenException ex,
            HttpServletRequest request){
        log.error("Acceso prohibido: {}", ex.getMessage());
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.name())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse>handleUnauthorizedException(
            UnauthorizedException ex,
            HttpServletRequest request){
        log.error("No autorizado: {}", ex.getMessage());
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.name())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleConflictException(
            ConflictException ex,
            HttpServletRequest request){
        log.error("Libro ya existe: {} ", ex.getMessage());
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
