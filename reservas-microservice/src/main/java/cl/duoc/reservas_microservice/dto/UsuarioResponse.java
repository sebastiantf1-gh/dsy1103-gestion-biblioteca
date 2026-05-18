package cl.duoc.reservas_microservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioResponse {

    @JsonProperty("id")
    private Long id;

    private String nombreCompleto;
    private String email;


    @JsonProperty("idUsuario")
    public void setIdUsuario(Long idUsuario) {
        this.id = idUsuario;
    }
}
