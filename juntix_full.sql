-- --------------------------------------------------------------
-- JUNTIX: esquema, seed de datos y consultas de validación
-- Axel Gaston Lopez
-- Seminario de practica de informatica
-- Universidad Siglo 21
-- Archivo único: juntix_full.sql
-- Compatible con MySQL 8+
-- Ejecutar en MySQL Workbench o CLI después de: CREATE DATABASE juntix; USE juntix;
-- --------------------------------------------------------------

CREATE DATABASE juntix CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci; USE juntix;

-- ====== SCHEMA (crear tablas) ======
-- Oficio (catálogo)
CREATE TABLE IF NOT EXISTS Oficio (
  oficio_id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_spanish_ci;

-- Usuario
CREATE TABLE IF NOT EXISTS Usuario (
  usuario_id INT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  rol ENUM('CLIENTE','TRABAJADOR') NOT NULL,
  telefono VARCHAR(50),
  fecha_alta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_usuario_email (email)
) ENGINE=InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_spanish_ci;

-- PerfilTrabajador
CREATE TABLE IF NOT EXISTS PerfilTrabajador (
  perfil_id INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NOT NULL,
  oficio_id INT NOT NULL,
  nombre_completo VARCHAR(200) NOT NULL,
  experiencia TEXT,
  telefono VARCHAR(50) NOT NULL,
  localidad VARCHAR(150) NOT NULL,
  visible BOOLEAN NOT NULL DEFAULT FALSE,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_perfil_usuario FOREIGN KEY (usuario_id) REFERENCES Usuario(usuario_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_perfil_oficio FOREIGN KEY (oficio_id) REFERENCES Oficio(oficio_id) ON DELETE RESTRICT ON UPDATE CASCADE,
  INDEX idx_perfil_oficio_localidad (oficio_id, localidad),
  INDEX idx_perfil_visible (visible)
) ENGINE=InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_spanish_ci;

-- LogEvento (auditoría)
CREATE TABLE IF NOT EXISTS LogEvento (
  log_id INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NULL,
  accion VARCHAR(100) NOT NULL,
  detalle VARCHAR(500),
  fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_log_usuario FOREIGN KEY (usuario_id) REFERENCES Usuario(usuario_id) ON DELETE SET NULL ON UPDATE CASCADE,
  INDEX idx_log_fecha (fecha)
) ENGINE=InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_spanish_ci;

-- UserSession
CREATE TABLE IF NOT EXISTS UserSession (
  session_id VARCHAR(128) PRIMARY KEY,
  usuario_id INT NOT NULL,
  fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_expiracion TIMESTAMP NULL,
  CONSTRAINT fk_session_usuario FOREIGN KEY (usuario_id) REFERENCES Usuario(usuario_id) ON DELETE CASCADE ON UPDATE CASCADE,
  INDEX idx_session_usuario (usuario_id)
) ENGINE=InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_spanish_ci;

-- Vista para perfiles visibles
CREATE OR REPLACE VIEW Vw_PerfilVisible AS
SELECT p.perfil_id, u.usuario_id, u.email, p.nombre_completo, o.nombre AS oficio, p.localidad, p.telefono, p.experiencia, p.fecha_creacion
FROM PerfilTrabajador p
JOIN Usuario u ON p.usuario_id = u.usuario_id
JOIN Oficio o ON p.oficio_id = o.oficio_id
WHERE p.visible = TRUE AND p.activo = TRUE;

-- ====== SEED: carga de datos de prueba ======

-- Cargar catálogo de oficios
INSERT INTO Oficio (nombre) VALUES
('Jardinería'),
('Pintura'),
('Plomería'),
('Electricidad'),
('Limpieza')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

-- Insertar usuarios (hashes de ejemplo; en la app Java generar con BCrypt)
INSERT INTO Usuario (email, password_hash, rol, telefono) VALUES
('cliente1@example.com', '$2a$12$EjemploHashCliente1', 'CLIENTE', '3511111111'),
('cliente2@example.com', '$2a$12$EjemploHashCliente2', 'CLIENTE', '3512222222'),
('trabajador1@example.com', '$2a$12$EjemploHashTrab1', 'TRABAJADOR', '3513333333'),
('trabajador2@example.com', '$2a$12$EjemploHashTrab2', 'TRABAJADOR', '3514444444'),
('trabajador3@example.com', '$2a$12$EjemploHashTrab3', 'TRABAJADOR', '3515555555')
ON DUPLICATE KEY UPDATE telefono = VALUES(telefono);

-- Insertar perfiles de trabajadores usando subqueries
INSERT INTO PerfilTrabajador (usuario_id, oficio_id, nombre_completo, experiencia, telefono, localidad, visible)
VALUES
(
  (SELECT usuario_id FROM Usuario WHERE email = 'trabajador1@example.com'),
  (SELECT oficio_id FROM Oficio WHERE nombre = 'Jardinería'),
  'Juan Pérez',
  '5 años realizando mantenimiento de parques y jardines',
  '3513333333',
  'Santa Rosa',
  TRUE
),
(
  (SELECT usuario_id FROM Usuario WHERE email = 'trabajador2@example.com'),
  (SELECT oficio_id FROM Oficio WHERE nombre = 'Plomería'),
  'María López',
  '10 años como plomera; trabajos residenciales y comerciales',
  '3514444444',
  'Villa General Belgrano',
  TRUE
),
(
  (SELECT usuario_id FROM Usuario WHERE email = 'trabajador3@example.com'),
  (SELECT oficio_id FROM Oficio WHERE nombre = 'Pintura'),
  'Carlos Gómez',
  'Pintor independiente con 8 años de experiencia',
  '3515555555',
  'Santa Rosa',
  TRUE
)
ON DUPLICATE KEY UPDATE nombre_completo = VALUES(nombre_completo), telefono = VALUES(telefono);

-- Insertar logs de ejemplo (auditoría)
INSERT INTO LogEvento (usuario_id, accion, detalle) VALUES
((SELECT usuario_id FROM Usuario WHERE email='trabajador1@example.com'), 'CREAR_PERFIL', 'Perfil trabajador creado: Jardinería'),
((SELECT usuario_id FROM Usuario WHERE email='trabajador2@example.com'), 'CREAR_PERFIL', 'Perfil trabajador creado: Plomería'),
((SELECT usuario_id FROM Usuario WHERE email='trabajador3@example.com'), 'CREAR_PERFIL', 'Perfil trabajador creado: Pintura'),
((SELECT usuario_id FROM Usuario WHERE email='cliente1@example.com'), 'BUSCAR_TRABAJADOR', 'Búsqueda: Jardinería en Santa Rosa'),
((SELECT usuario_id FROM Usuario WHERE email='cliente2@example.com'), 'BUSCAR_TRABAJADOR', 'Búsqueda: Plomería en Villa General Belgrano');

-- Insertar sesión de ejemplo
INSERT INTO UserSession (session_id, usuario_id, fecha_expiracion) VALUES
('sess_abcd1234', (SELECT usuario_id FROM Usuario WHERE email='trabajador1@example.com'), DATE_ADD(NOW(), INTERVAL 2 HOUR))
ON DUPLICATE KEY UPDATE fecha_expiracion = VALUES(fecha_expiracion);

-- ====== CONSULTAS / VALIDACIÓN (ejemplos que ejecutar para verificar seed) ======

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
