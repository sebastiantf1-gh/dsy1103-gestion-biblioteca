CREATE TABLE libros(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    isbn            VARCHAR(20) NOT NULL UNIQUE,
    titulo          VARCHAR(100) NOT NULL,
    sinopsis        TEXT,
    numero_paginas  SMALLINT NOT NULL,
    disponible      BOOLEAN NOT NULL,
    fecha_registro  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_autor        BIGINT NOT NULL,
    id_categoria    BIGINT NOT NULL,
    id_genero       BIGINT NOT NULL
);