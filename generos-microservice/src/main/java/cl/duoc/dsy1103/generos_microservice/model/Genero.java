package cl.duoc.dsy1103.generos_microservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "generos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Genero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_genero",unique = true, nullable = false)
    private String nombreGenero;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;
}
