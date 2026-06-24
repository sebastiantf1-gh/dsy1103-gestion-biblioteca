package cl.duoc.dsy1103.categorias_microservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "CategoriaRequest", description = "Modelo DTO de entrada para la creación de una nueva categoría literaria")
public class CategoriaRequest {

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 90, message = "El nombre debe tener como maximo 90 caracteres.")
    @Schema(
            description = "Nombre formal del género o categoría literaria a registrar",
            example = "Novela Histórica",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 90
    )
    private String nombre;

    @NotBlank(message = "La descripcion es obligatoria.")
    @Size(max = 250, message = "La descripcion debe tener como maximo 250 caracteres.")
    @Schema(
            description = "Breve descripción sobre el tipo de obras o temáticas que abarca la categoría",
            example = "Narrativas de ficción que sitúan su trama en un momento del pasado minuciosamente reconstruido.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 250
    )
    private String descripcion;
}