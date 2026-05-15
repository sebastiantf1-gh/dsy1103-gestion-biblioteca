package cl.duoc.dsy1103.usuarios_microservice.dto;



import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioUpdateRequest {

    @Size(min =1 ,max = 100, message = "El nombre completo tiene un limite de 100 caracteres")
    private String nombreCompleto;

    @Size(min= 1, max = 100, message = "El email tiene un limite de 100 caracteres")
    private String email;

    @Size(max = 20, message = "Limites de caracteres 20")
    private String telefono;
}
