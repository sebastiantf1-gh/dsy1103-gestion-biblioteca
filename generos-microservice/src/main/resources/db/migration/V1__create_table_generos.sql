CREATE TABLE generos(
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre_genero VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255) NOT NULL,
    CONSTRAINT  pk_usuarios PRIMARY KEY (id)
);