CREATE TABLE personal_biblioteca (
                                     id_personal_biblioteca BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     nombre_usuario VARCHAR(100) NOT NULL UNIQUE,
                                     password VARCHAR(255) NOT NULL,
                                     email VARCHAR(100) NOT NULL UNIQUE,
                                     numero_telefono VARCHAR(20),
                                     fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);