CREATE TABLE reservas (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          fecha_inicio DATE NOT NULL,
                          fecha_termino DATE NOT NULL,
                          id_libro BIGINT NOT NULL,
                          id_usuario BIGINT NOT NULL,
                          estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVA'
);