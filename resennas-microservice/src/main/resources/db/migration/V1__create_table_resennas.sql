CREATE TABLE resennas(
    id              BIGINT NOT NULL AUTO_INCREMENT,
    descripcion     VARCHAR(255),
    calificacion    INT NOT NULL,
    fecha_registro  DATETIME NOT NULL,
    id_usuario      BIGINT NOT NULL,
    id_libro        BIGINT NOT NULL,
    CONSTRAINT pk_resennas PRIMARY KEY (id)
);