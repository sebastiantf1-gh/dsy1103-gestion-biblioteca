package cl.duoc.dsy1103.autores_microservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class) //"escuchar" eventos de auditoria (@CreatedDate)
@Table(name = "autores")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_completo",nullable = false, length = 100)
    private String nombreCompleto;

    @Column(nullable = true, columnDefinition = "TEXT") // + de 255 caracteres.
    private String biografia;

    @Column(nullable = false, length = 50)
    private String nacionalidad;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDateTime fechaNacimiento;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    @CreatedDate //auditoria para automatizar fecha de registro
    private LocalDateTime fechaRegistro;
}
