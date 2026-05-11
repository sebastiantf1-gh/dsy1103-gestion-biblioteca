package cl.duoc.dsy1103.libros_microservice.model;

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
@EntityListeners(AuditingEntityListener.class)
@Table(name = "libros") //test
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 20, nullable = false)
    private String isbn;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String sinopsis;

    @Column(name = "numero_paginas", nullable = false)
    private Short numeroPaginas;

    @Column(nullable = false)
    private Boolean disponible;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime fechaRegistro;

    @Column(name = "id_autor", nullable = false)
    private Long idAutor;

    @Column(name = "id_categoria", nullable = false)
    private Long idCategoria;

    @Column(name = "id_genero", nullable = false)
    private Long idGenero;

}
