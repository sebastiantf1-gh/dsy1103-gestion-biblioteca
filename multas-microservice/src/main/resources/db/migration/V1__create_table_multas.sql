CREATE TABLE multas (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        monto INT,
                        fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        fecha_limite_pago TIMESTAMP NOT NULL,
                        id_usuario BIGINT NOT NULL,
                        id_prestamo BIGINT NOT NULL,

                        UNIQUE KEY uk_usuario_prestamo (id_usuario, id_prestamo)
);