-- --------------------------------------------------------------
-- JUNTIX: esquema
-- Axel Gaston Lopez
-- Seminario de practica de informatica
-- Universidad Siglo 21
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