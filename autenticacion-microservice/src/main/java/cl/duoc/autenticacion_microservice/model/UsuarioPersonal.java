package cl.duoc.autenticacion_microservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "personal_biblioteca")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioPersonal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPersonalBiblioteca;

    @Column(name = "nombre_completo", unique = true, nullable = false, length = 100)
    private String nombreUsuario;

    @Column(nullable = false)
    private String password;

    @Column(name = "email", unique = true, nullable = false, length = 150)
    private String email;

    @Column(name = "telefono", length = 20)
    private String numeroTelefono;

    @Column(name = "fecha_registro",nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}
