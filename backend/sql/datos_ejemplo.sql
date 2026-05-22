-- ============================================================
--  COLOMBIANDO — Datos de ejemplo / semilla inicial
-- ============================================================

USE colombiando_db;

-- ── Idiomas ───────────────────────────────────────────────────
INSERT INTO idioma (nombre, codigo_iso, nivel_requerido) VALUES
  ('Español',   'es', 'NATIVO'),
  ('Inglés',    'en', 'AVANZADO'),
  ('Francés',   'fr', 'INTERMEDIO'),
  ('Portugués', 'pt', 'INTERMEDIO'),
  ('Alemán',    'de', 'BASICO');

-- ── Destinos ──────────────────────────────────────────────────
INSERT INTO destino (nombre, departamento, municipio, descripcion, clima, imagen_url) VALUES
  ('Ciudad Amurallada',   'Bolívar',         'Cartagena',       'Centro histórico Patrimonio de la Humanidad', 'Cálido',   'https://colombiando.co/img/cartagena.jpg'),
  ('Eje Cafetero',        'Quindío',         'Armenia',         'Paisaje Cultural Cafetero, reserva UNESCO',   'Templado', 'https://colombiando.co/img/eje-cafetero.jpg'),
  ('Parque Tayrona',      'Magdalena',       'Santa Marta',     'Playas paradisíacas y selva tropical',        'Cálido',   'https://colombiando.co/img/tayrona.jpg'),
  ('Caño Cristales',      'Meta',            'La Macarena',     'El río más hermoso del mundo',                'Cálido',   'https://colombiando.co/img/caño-cristales.jpg'),
  ('Ciudad Perdida',      'Magdalena',       'Santa Marta',     'Ruinas indígenas Tayrona en la Sierra Nevada','Cálido',   'https://colombiando.co/img/ciudad-perdida.jpg'),
  ('Desierto La Tatacoa', 'Huila',           'Villavieja',      'Desierto rojo y gris con cielo estrellado',   'Cálido',   'https://colombiando.co/img/tatacoa.jpg'),
  ('Jardín',              'Antioquia',       'Jardín',          'Pueblo patrimonio con naturaleza exuberante', 'Templado', 'https://colombiando.co/img/jardin.jpg'),
  ('Amazonas',            'Amazonas',        'Leticia',         'Selva amazónica y etnias indígenas',          'Cálido',   'https://colombiando.co/img/amazonas.jpg');

-- ── Empleados ─────────────────────────────────────────────────
INSERT INTO empleado (nombre, apellido, correo, telefono, contrasena, cargo, salario, fecha_contratacion) VALUES
  ('Carlos',   'Mendoza',  'carlos.mendoza@colombiando.co',  '3001234567', 'hashed_pass_1', 'GUIA',         2500000.00, '2022-03-01'),
  ('Diana',    'Ospina',   'diana.ospina@colombiando.co',    '3109876543', 'hashed_pass_2', 'GUIA',         2500000.00, '2021-07-15'),
  ('Miguel',   'Torres',   'miguel.torres@colombiando.co',   '3204445566', 'hashed_pass_3', 'COORDINADOR',  3800000.00, '2020-01-20'),
  ('Lucia',    'Vargas',   'lucia.vargas@colombiando.co',    '3152223344', 'hashed_pass_4', 'ASESOR',       2200000.00, '2023-06-10'),
  ('Andrés',   'Herrera',  'andres.herrera@colombiando.co',  '3007778899', 'hashed_pass_5', 'GUIA',         2500000.00, '2022-11-01');

-- ── Idiomas de empleados (HABLA) ──────────────────────────────
INSERT INTO habla (id_empleado, id_idioma, nivel) VALUES
  (1, 1, 'NATIVO'),   -- Carlos: Español nativo
  (1, 2, 'AVANZADO'), -- Carlos: Inglés avanzado
  (2, 1, 'NATIVO'),
  (2, 3, 'INTERMEDIO'), -- Diana: Francés
  (3, 1, 'NATIVO'),
  (3, 2, 'AVANZADO'),
  (3, 4, 'BASICO'),   -- Miguel: Portugués básico
  (4, 1, 'NATIVO'),
  (4, 2, 'INTERMEDIO'),
  (5, 1, 'NATIVO'),
  (5, 2, 'AVANZADO'),
  (5, 5, 'BASICO');   -- Andrés: Alemán básico

-- ── Clientes ──────────────────────────────────────────────────
INSERT INTO cliente (nombre, apellido, correo, telefono, contrasena,
                     numero_documento, tipo_documento, fecha_nacimiento, nacionalidad) VALUES
  ('María',   'González', 'maria.gonzalez@mail.com',   '3011112222', 'hashed_c1', '1020304050', 'CC', '1990-05-14', 'Colombiana'),
  ('James',   'Anderson', 'james.a@outlook.com',        '3122223333', 'hashed_c2', 'A12345678',  'PASAPORTE', '1985-08-22', 'Estadounidense'),
  ('Laura',   'Restrepo', 'lrestrepo@gmail.com',         '3054445555', 'hashed_c3', '1098765432', 'CC', '1995-12-30', 'Colombiana'),
  ('Sophie',  'Leclerc',  'sophie.l@orange.fr',          '3006667777', 'hashed_c4', 'FR98765432', 'PASAPORTE', '1993-03-18', 'Francesa'),
  ('Carlos',  'Pedraza',  'cpedraza@empresa.co',         '3148889999', 'hashed_c5', '80654321',   'CC', '1978-11-05', 'Colombiana');

-- ── Tours ─────────────────────────────────────────────────────
INSERT INTO tour (nombre, descripcion, precio, duracion_dias, capacidad_maxima,
                  fecha_salida, fecha_regreso, estado) VALUES
  ('Cartagena Mágica',      'Recorre la ciudad amurallada, Islas del Rosario y Bocachico', 1800000.00,  4, 20, '2026-06-15', '2026-06-18', 'ACTIVO'),
  ('Ruta del Café',         'Experiencia cafetera en el Quindío y Caldas',                 2200000.00,  5, 16, '2026-07-01', '2026-07-05', 'ACTIVO'),
  ('Tayrona y Ciudad Perdida','Senderismo entre mar y selva en la Sierra Nevada',          3500000.00,  7, 12, '2026-07-20', '2026-07-26', 'ACTIVO'),
  ('Amazonas Salvaje',      'Expedición a la selva amazónica con comunidades indígenas',   4800000.00,  6, 10, '2026-08-10', '2026-08-15', 'ACTIVO'),
  ('Caño Cristales',        'Visita al río de los cinco colores en La Macarena',           2900000.00,  4, 14, '2026-09-05', '2026-09-08', 'ACTIVO');

-- ── Asignar destinos a tours ───────────────────────────────────
INSERT INTO tour_destino (id_tour, id_destino) VALUES
  (1, 1),  -- Cartagena Mágica → Ciudad Amurallada
  (2, 2),  -- Ruta del Café    → Eje Cafetero
  (2, 7),  -- Ruta del Café    → Jardín
  (3, 3),  -- Tayrona          → Parque Tayrona
  (3, 5),  -- + Ciudad Perdida
  (4, 8),  -- Amazonas         → Amazonas
  (5, 4),  -- Caño Cristales   → Caño Cristales
  (5, 6);  -- + La Tatacoa

-- ── Asignar empleados a tours ──────────────────────────────────
INSERT INTO tour_empleado (id_tour, id_empleado, rol_en_tour) VALUES
  (1, 1, 'GUIA'),
  (1, 3, 'COORDINADOR'),
  (2, 2, 'GUIA'),
  (3, 5, 'GUIA'),
  (3, 3, 'COORDINADOR'),
  (4, 1, 'GUIA'),
  (5, 2, 'GUIA');

-- ── Reservas ──────────────────────────────────────────────────
INSERT INTO reserva (id_cliente, id_tour, numero_pasajeros, fecha_reserva, estado, observaciones) VALUES
  (1, 1, 2, '2026-06-15', 'CONFIRMADA',  'Solicitan habitación doble'),
  (2, 3, 1, '2026-07-20', 'CONFIRMADA',  'Pasaporte vence en 2027'),
  (3, 2, 3, '2026-07-01', 'PENDIENTE',   'Grupo familiar'),
  (4, 1, 1, '2026-06-15', 'PENDIENTE',   'Prefiere guía en francés'),
  (5, 4, 2, '2026-08-10', 'CONFIRMADA',  'Interesados en fotografía');

-- ── Pagos ─────────────────────────────────────────────────────
INSERT INTO pago (id_reserva, monto, metodo_pago, estado, referencia, observaciones) VALUES
  (1, 3600000.00, 'TRANSFERENCIA', 'APROBADO', 'TXN-COL-001', 'Pago total (2 personas × $1,800,000)'),
  (2, 3500000.00, 'TARJETA',       'APROBADO', 'TXN-COL-002', 'Pago completo tarjeta Visa'),
  (5, 4800000.00, 'PSE',           'APROBADO', 'TXN-COL-003', 'Pago total tour Amazonas'),
  (3, 2200000.00, 'EFECTIVO',      'APROBADO', 'TXN-COL-004', 'Abono inicial de $2,200,000'),
  (4, 1800000.00, 'NEQUI',         'PENDIENTE','TXN-COL-005', 'En proceso de validación');
