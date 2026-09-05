-- ============================================================
--  COLOMBIANDO — Consultas SQL Avanzadas
--  Ejemplos útiles para reportes y análisis
-- ============================================================

USE colombiando_db;

-- ─────────────────────────────────────────────────────────────
-- 1. Tours activos con cupos disponibles
-- ─────────────────────────────────────────────────────────────
SELECT
    t.id_tour,
    t.nombre                                       AS tour,
    t.fecha_salida,
    t.precio,
    t.capacidad_maxima                             AS capacidad,
    COALESCE(SUM(r.numero_pasajeros), 0)           AS pasajeros_reservados,
    t.capacidad_maxima - COALESCE(SUM(r.numero_pasajeros), 0) AS cupos_disponibles
FROM tour t
LEFT JOIN reserva r
       ON t.id_tour = r.id_tour
      AND r.estado NOT IN ('CANCELADA')
WHERE t.estado = 'ACTIVO'
  AND t.fecha_salida >= CURDATE()
GROUP BY t.id_tour, t.nombre, t.fecha_salida, t.precio, t.capacidad_maxima
HAVING cupos_disponibles > 0
ORDER BY t.fecha_salida;


-- ─────────────────────────────────────────────────────────────
-- 2. Historial completo de un cliente con sus pagos
-- ─────────────────────────────────────────────────────────────
SELECT
    c.nombre                                   AS cliente,
    c.apellido,
    t.nombre                                   AS tour,
    r.id_reserva,
    r.numero_pasajeros,
    r.fecha_reserva,
    r.estado                                   AS estado_reserva,
    COALESCE(SUM(p.monto), 0)                  AS total_pagado,
    t.precio * r.numero_pasajeros              AS total_a_pagar,
    (t.precio * r.numero_pasajeros)
        - COALESCE(SUM(p.monto), 0)            AS saldo_pendiente
FROM cliente c
JOIN reserva r ON c.id_usuario = r.id_cliente
JOIN tour    t ON r.id_tour    = t.id_tour
LEFT JOIN pago p ON r.id_reserva = p.id_reserva AND p.estado = 'APROBADO'
WHERE c.id_usuario = 1          -- ← parámetro: id del cliente
GROUP BY c.nombre, c.apellido, t.nombre, r.id_reserva,
         r.numero_pasajeros, r.fecha_reserva, r.estado, t.precio
ORDER BY r.fecha_reserva DESC;


-- ─────────────────────────────────────────────────────────────
-- 3. Tours con sus destinos y empleados asignados
-- ─────────────────────────────────────────────────────────────
SELECT
    t.nombre                   AS tour,
    t.fecha_salida,
    GROUP_CONCAT(DISTINCT d.nombre ORDER BY d.nombre SEPARATOR ', ')
                               AS destinos,
    GROUP_CONCAT(DISTINCT CONCAT(e.nombre, ' ', e.apellido, ' (', te.rol_en_tour, ')')
                 ORDER BY e.apellido SEPARATOR ' | ')
                               AS equipo
FROM tour t
LEFT JOIN tour_destino  td ON t.id_tour   = td.id_tour
LEFT JOIN destino        d ON td.id_destino = d.id_destino
LEFT JOIN tour_empleado te ON t.id_tour   = te.id_tour
LEFT JOIN empleado       e ON te.id_empleado = e.id_usuario
GROUP BY t.id_tour, t.nombre, t.fecha_salida
ORDER BY t.fecha_salida;


-- ─────────────────────────────────────────────────────────────
-- 4. Empleados con sus idiomas
-- ─────────────────────────────────────────────────────────────
SELECT
    e.id_usuario,
    CONCAT(e.nombre, ' ', e.apellido) AS empleado,
    e.cargo,
    GROUP_CONCAT(CONCAT(i.nombre, ' (', h.nivel, ')')
                 ORDER BY i.nombre SEPARATOR ', ')
                                      AS idiomas
FROM empleado e
LEFT JOIN habla  h ON e.id_usuario = h.id_empleado
LEFT JOIN idioma i ON h.id_idioma  = i.id_idioma
GROUP BY e.id_usuario, e.nombre, e.apellido, e.cargo
ORDER BY e.apellido;


-- ─────────────────────────────────────────────────────────────
-- 5. Reporte de ingresos por mes
-- ─────────────────────────────────────────────────────────────
SELECT
    DATE_FORMAT(p.fecha_pago, '%Y-%m')     AS mes,
    COUNT(DISTINCT p.id_reserva)           AS numero_reservas,
    COUNT(p.id_pago)                       AS numero_pagos,
    SUM(p.monto)                           AS ingresos_totales,
    AVG(p.monto)                           AS ingreso_promedio
FROM pago p
WHERE p.estado = 'APROBADO'
GROUP BY DATE_FORMAT(p.fecha_pago, '%Y-%m')
ORDER BY mes DESC;


-- ─────────────────────────────────────────────────────────────
-- 6. Top 5 destinos más visitados
-- ─────────────────────────────────────────────────────────────
SELECT
    d.nombre                                    AS destino,
    d.departamento,
    COUNT(DISTINCT td.id_tour)                  AS tours_que_incluyen,
    COALESCE(SUM(r.numero_pasajeros), 0)        AS total_visitantes
FROM destino d
JOIN tour_destino td ON d.id_destino = td.id_destino
JOIN tour          t ON td.id_tour   = t.id_tour
LEFT JOIN reserva  r ON t.id_tour    = r.id_tour AND r.estado NOT IN ('CANCELADA')
GROUP BY d.id_destino, d.nombre, d.departamento
ORDER BY total_visitantes DESC
LIMIT 5;


-- ─────────────────────────────────────────────────────────────
-- 7. Guías disponibles para un idioma específico (ej: Inglés)
-- ─────────────────────────────────────────────────────────────
SELECT
    e.id_usuario,
    CONCAT(e.nombre, ' ', e.apellido) AS guia,
    h.nivel
FROM empleado e
JOIN habla  h ON e.id_usuario = h.id_empleado
JOIN idioma i ON h.id_idioma  = i.id_idioma
WHERE i.codigo_iso = 'en'            -- ← parámetro: código ISO del idioma
  AND h.nivel IN ('AVANZADO','NATIVO')
  AND e.activo = 1
ORDER BY h.nivel DESC, e.apellido;


-- ─────────────────────────────────────────────────────────────
-- 8. Reservas pendientes con saldo por cobrar
-- ─────────────────────────────────────────────────────────────
SELECT
    r.id_reserva,
    CONCAT(c.nombre, ' ', c.apellido)     AS cliente,
    c.telefono,
    t.nombre                               AS tour,
    t.precio * r.numero_pasajeros          AS valor_total,
    COALESCE(pagos.total_pagado, 0)        AS pagado,
    t.precio * r.numero_pasajeros
        - COALESCE(pagos.total_pagado, 0) AS saldo_pendiente,
    r.fecha_reserva
FROM reserva r
JOIN cliente c ON r.id_cliente = c.id_usuario
JOIN tour    t ON r.id_tour    = t.id_tour
LEFT JOIN (
    SELECT id_reserva, SUM(monto) AS total_pagado
    FROM   pago
    WHERE  estado = 'APROBADO'
    GROUP  BY id_reserva
) pagos ON r.id_reserva = pagos.id_reserva
WHERE r.estado IN ('PENDIENTE', 'CONFIRMADA')
  AND (t.precio * r.numero_pasajeros - COALESCE(pagos.total_pagado, 0)) > 0
ORDER BY r.fecha_reserva;


-- ─────────────────────────────────────────────────────────────
-- 9. Estadísticas generales del sistema
-- ─────────────────────────────────────────────────────────────
SELECT 'Clientes registrados'  AS metrica, COUNT(*) AS valor FROM cliente
UNION ALL
SELECT 'Empleados activos',     COUNT(*) FROM empleado WHERE activo = 1
UNION ALL
SELECT 'Tours activos',         COUNT(*) FROM tour WHERE estado = 'ACTIVO'
UNION ALL
SELECT 'Destinos disponibles',  COUNT(*) FROM destino
UNION ALL
SELECT 'Reservas confirmadas',  COUNT(*) FROM reserva WHERE estado = 'CONFIRMADA'
UNION ALL
SELECT 'Pagos aprobados',       COUNT(*) FROM pago    WHERE estado = 'APROBADO';
