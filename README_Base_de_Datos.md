# Juntix - Base de Datos (Prototipo)

Este repositorio contiene los scripts SQL correspondientes al **prototipo de base de datos** para el sistema **Juntix**, desarrollado como parte del **Trabajo Práctico 2 de Seminario de Práctica de Informática**.  

El sistema Juntix busca conectar clientes con trabajadores independientes (ej. jardinería, plomería, pintura, electricidad, limpieza), gestionando usuarios, perfiles, oficios, sesiones y eventos de auditoría.

---

## 📂 Contenido del repositorio

- **schema.sql** → Definición de las tablas principales (DDL).
- **seed.sql** → Inserción de datos de prueba (usuarios, perfiles, oficios, logs).
- **queries.sql** → Consultas frecuentes, índices, vistas y ejemplos de borrado seguro.
- **juntix_full.sql** → Script completo que integra esquema, datos de prueba y consultas de validación (versión todo en uno).

---

## ⚙️ Requisitos

- **MySQL 8.0+**  
- Conector oficial JDBC (para integración con Java)  
- Motor de almacenamiento **InnoDB**  
- Charset: `utf8mb4`  
- Collation: `utf8mb4_spanish_ci`  

---

## ▶️ Uso

### 1. Crear la base de datos y ejecutar el esquema
```bash
mysql -u root -p
CREATE DATABASE juntix CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE juntix;
SOURCE schema.sql;
