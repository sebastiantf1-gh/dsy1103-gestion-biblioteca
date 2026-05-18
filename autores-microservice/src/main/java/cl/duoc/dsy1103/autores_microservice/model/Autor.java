package cl.duoc.dsy1103.autores_microservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)  // Habilita el oyente de auditoría de Spring Data JPA. Es indispensable
                                                // para interceptar la entidad antes de la persistencia y rellenar
                                                //campos automáticos como @CreatedDate.
@Table(name = "autores") // Define el nombre explícito de la tabla en la base de datos.
@AllArgsConstructor // Genera un constructor con todos los campos disponibles (requerido por el patrón Builder).
@NoArgsConstructor // Genera un constructor vacío obligatorio para que Hibernate pueda instanciar la entidad mediante reflexión.
@Builder // Implementa el patrón de diseño creacional Builder, facilitando las transformaciones de datos en la capa Mapper.
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
    private LocalDate fechaNacimiento;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    @CreatedDate //'@CreatedDate' automatiza la captura del timestamp de registro. Combinado con 'updatable = false',
                // se blinda el campo para que cambios posteriores (como actualizaciones) no alteren la fecha original.
    private LocalDateTime fechaRegistro;
}
