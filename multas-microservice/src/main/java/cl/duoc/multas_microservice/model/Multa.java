package cl.duoc.multas_microservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "multas", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_usuario","id_prestamo"})
})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Multa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "monto", nullable = true)
    private Integer monto;

    @Column(name = "fecha_registro", nullable = false)
    @Builder.Default
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "fecha_limite_pago", nullable = false)
    private LocalDateTime fechaLimitePago;

    //FK a otros microservicios

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "id_prestamo", nullable = false)
    private Long idPrestamo;
}
