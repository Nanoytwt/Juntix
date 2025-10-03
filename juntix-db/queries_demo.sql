-- --------------------------------------------------------------
-- JUNTIX
-- Axel Gaston Lopez
-- Seminario de practica de informatica
-- Universidad Siglo 21
-- Compatible con MySQL 8+
-- Ejecutar en MySQL Workbench o CLI después de: CREATE DATABASE juntix; USE juntix;
-- --------------------------------------------------------------

-- Buscar trabajadores por oficio y localidad
SELECT p.perfil_id, u.email, p.nombre_completo, o.nombre AS oficio, p.localidad, p.experiencia, p.telefono
FROM PerfilTrabajador p
JOIN Usuario u ON p.usuario_id = u.usuario_id
JOIN Oficio o ON p.oficio_id = o.oficio_id
WHERE p.visible = TRUE AND p.activo = TRUE
  AND o.nombre = 'Jardinería'
  AND p.localidad = 'Santa Rosa'
ORDER BY p.localidad, p.nombre_completo;

-- Listado de todos los trabajadores visibles
SELECT p.perfil_id, p.nombre_completo, o.nombre AS oficio, p.localidad, p.telefono
FROM PerfilTrabajador p
JOIN Oficio o ON p.oficio_id = o.oficio_id
WHERE p.visible = TRUE AND p.activo = TRUE
ORDER BY o.nombre, p.nombre_completo;

-- Conteo por oficio
SELECT o.nombre AS oficio, COUNT(*) AS cantidad
FROM PerfilTrabajador p
JOIN Oficio o ON p.oficio_id = o.oficio_id
WHERE p.visible = TRUE AND p.activo = TRUE
GROUP BY o.oficio_id, o.nombre
ORDER BY cantidad DESC;

-- Logs recientes
SELECT COALESCE(u.email,'(anon)') AS email, l.accion, l.detalle, l.fecha
FROM LogEvento l
LEFT JOIN Usuario u ON l.usuario_id = u.usuario_id
ORDER BY l.fecha DESC
LIMIT 20;

-- Obtener detalle de perfil (ver contacto)
SELECT p.perfil_id, u.email, p.nombre_completo, o.nombre AS oficio, p.experiencia, p.telefono, p.localidad
FROM PerfilTrabajador p
JOIN Usuario u ON p.usuario_id = u.usuario_id
JOIN Oficio o ON p.oficio_id = o.oficio_id
WHERE p.perfil_id = 1 AND p.visible = TRUE AND p.activo = TRUE;

-- Cantidad de trabajadores registrados por oficio
SELECT o.nombre AS oficio, COUNT(p.perfil_id) AS cantidad
FROM PerfilTrabajador p
JOIN Oficio o ON p.oficio_id = o.oficio_id
GROUP BY o.oficio_id
ORDER BY cantidad DESC;

-- Tiempo medio hasta primer contacto (si se registran eventos)
-- Suponiendo LogEvento registra 'PUBLICADO' y 'CONTACTO_REALIZADO'
SELECT AVG(TIMESTAMPDIFF(HOUR, pub.fecha, con.fecha)) AS horas_promedio
FROM LogEvento pub
JOIN LogEvento con ON pub.usuario_id = con.usuario_id
WHERE pub.accion = 'PUBLICAR_PERFIL'
  AND con.accion = 'PRIMER_CONTACTO';


-- ====== Borrado de registros (DELETE) ======
-- Soft delete
-- Agregar columna previamente:
ALTER TABLE PerfilTrabajador ADD COLUMN activo BOOLEAN DEFAULT TRUE;

-- Desactivar perfil (soft delete)
UPDATE PerfilTrabajador SET activo = FALSE, visible = FALSE WHERE perfil_id = ?;

-- Borrado físico con comprobación (hard delete) — con transacción
START TRANSACTION;

-- eliminar logs asociados opcionalmente
DELETE FROM LogEvento WHERE usuario_id = ?;

-- eliminar perfil
DELETE FROM PerfilTrabajador WHERE usuario_id = ?;

-- eliminar sesiones
DELETE FROM Session WHERE usuario_id = ?;

-- eliminar usuario
DELETE FROM Usuario WHERE usuario_id = ?;

COMMIT;

-- Borrado seguro por email
-- Obtener id y luego soft-delete
SELECT usuario_id FROM Usuario WHERE email = ?;

UPDATE Usuario SET telefono = NULL WHERE usuario_id = ?;   -- anonimizar si corresponde (RGPD/privacidad)
UPDATE PerfilTrabajador SET activo = FALSE, visible = FALSE WHERE usuario_id = ?;
