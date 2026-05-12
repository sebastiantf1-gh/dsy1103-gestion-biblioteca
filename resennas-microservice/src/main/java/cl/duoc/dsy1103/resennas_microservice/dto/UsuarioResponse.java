package cl.duoc.dsy1103.resennas_microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioResponse {
    private Long id;
    private String nombreCompleto;
    private String email;
    private String telefono;
}
