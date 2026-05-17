CREATE TABLE personal_biblioteca (
                                     id_personal_biblioteca BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     nombre_completo VARCHAR(100) NOT NULL UNIQUE,
                                     password VARCHAR(255) NOT NULL,
                                     email VARCHAR(150) NOT NULL UNIQUE,
                                     numero_telefono VARCHAR(20),
                                     fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);