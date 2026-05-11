CREATE TABLE autores(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre_completo  VARCHAR(100) NOT NULL,
    biografia        TEXT,
    nacionalidad     VARCHAR(50) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    fecha_registro   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);