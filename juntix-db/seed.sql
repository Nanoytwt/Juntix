-- --------------------------------------------------------------
-- JUNTIX: seed de datos
-- Axel Gaston Lopez
-- Seminario de practica de informatica
-- Universidad Siglo 21
-- Compatible con MySQL 8+
-- Ejecutar en MySQL Workbench o CLI después de: CREATE DATABASE juntix; USE juntix;
-- --------------------------------------------------------------

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