package cl.duoc.dsy1103.categorias_microservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categoria")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 90)
    private String nombre;

    @Column(nullable = false, length = 250)
    private String descripcion;

    @Column(name = "id_libro", nullable = false)
    private Long idLibro;
}
