-- Inserción 1: Reserva activa para el Usuario 1 y el Libro 1
INSERT INTO reservas (fecha_inicio, fecha_termino, id_libro, id_usuario, estado)
VALUES ('2026-06-01', '2026-06-07', 1, 1, 'ACTIVA');

-- Inserción 2: Reserva activa para el Usuario 2 y el Libro 2
INSERT INTO reservas (fecha_inicio, fecha_termino, id_libro, id_usuario, estado)
VALUES ('2026-06-05', '2026-06-12', 2, 2, 'ACTIVA');

-- Inserción 3: Reserva para el Usuario 3 y el Libro 3 (Fechas futuras en el año)
INSERT INTO reservas (fecha_inicio, fecha_termino, id_libro, id_usuario, estado)
VALUES ('2026-07-10', '2026-07-17', 3, 3, 'ACTIVA');

-- Inserción 4: Historial de reserva ya finalizada/completada para el Usuario 1 y el Libro 5
-- Útil para mostrarle al profesor que el sistema maneja diferentes estados
INSERT INTO reservas (fecha_inicio, fecha_termino, id_libro, id_usuario, estado)
VALUES ('2026-04-01', '2026-04-07', 5, 1, 'FINALIZADA');