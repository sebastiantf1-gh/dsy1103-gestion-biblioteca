package cl.duoc.dsy1103.resennas_microservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResennaResponse {
    private Long id;
    private String descripcion;
    private Integer calificacion;
    private LocalDateTime fechaRegistro;
}
