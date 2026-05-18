package cl.duoc.dsy1103.categorias_microservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categorias")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     // Establece la estrategia IDENTITY. Delega por completo el control del autoincremento al motor SQL, impidiendo
    // colisiones de identificadores y garantizando la consistencia en inserciones concurrentes .
    private Long id;

    @Column(nullable = false, length = 90)
    private String nombre;

    @Column(nullable = false, length = 250)
    private String descripcion;
}