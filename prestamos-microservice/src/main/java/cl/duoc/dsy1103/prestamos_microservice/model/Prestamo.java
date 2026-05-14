package cl.duoc.dsy1103.prestamos_microservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "prestamos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_prestamo",nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime fechaPrestamo;

    @Column(name = "fecha_devolucion")
    private LocalDateTime fechaDevolucion;

    @Column (name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column (name = "id_libro", nullable = false)
    private Long idLibro;

    @Column (nullable = false, length = 8)
    private String estado;
}
