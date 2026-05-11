package cl.duoc.dsy1103.usuarios_microservice.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioRequest {

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(max = 100, message = "El nombre completo tiene un limite de 100 caracteres")
    private String nombreCompleto;

    @NotBlank(message = "El email es obligatorio")
    @Size(max = 100, message = "El email tiene un limite de 100 caracteres")
    private String email;

    @Size(max = 20, message = "Limites de caracteres 20")
    private String telefono;


}
