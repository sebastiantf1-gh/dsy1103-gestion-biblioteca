-- Inserción 1: Multa estándar para el Usuario 1 y el Préstamo 1 (Vigente)
INSERT INTO multas (monto, fecha_registro, fecha_limite_pago, id_usuario, id_prestamo)
VALUES (5000, '2026-05-10 10:00:00', '2026-06-10 23:59:59', 1, 1);

-- Inserción 2: Multa menor para el Usuario 2 y el Préstamo 2
INSERT INTO multas (monto, fecha_registro, fecha_limite_pago, id_usuario, id_prestamo)
VALUES (1500, '2026-05-12 14:30:00', '2026-06-12 23:59:59', 2, 2);

-- Inserción 3: Multa alta para el Usuario 3 y el Préstamo 3 (Próxima a vencer)
INSERT INTO multas (monto, fecha_registro, fecha_limite_pago, id_usuario, id_prestamo)
VALUES (12500, '2026-05-01 09:00:00', '2026-05-20 18:00:00', 3, 3);

-- Inserción 4: Multa para el Usuario 1 con otro préstamo diferente (Préstamo 4)
-- Esto es válido ya que la restricción UNIQUE es por la combinación (id_usuario, id_prestamo)
INSERT INTO multas (monto, fecha_registro, fecha_limite_pago, id_usuario, id_prestamo)
VALUES (3000, '2026-05-15 11:15:00', '2026-06-15 23:59:59', 1, 4);