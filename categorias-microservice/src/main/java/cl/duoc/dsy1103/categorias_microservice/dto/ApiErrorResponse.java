package cl.duoc.dsy1103.categorias_microservice.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ApiErrorResponse {
    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
    private List<String> errors;
}
