package cl.duoc.dsy1103.resennas_microservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "resennas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resenna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //que la descripcion sea opcional
    @Column(name = "descripcion", nullable = true,length = 255)
    private String descripcion;

    @Column(name = "calificacion",nullable = false)
    private Integer calificacion;

    @Column(name = "fecha_registro",nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "id_libro", nullable = false)
    private Long idLibro;

}
