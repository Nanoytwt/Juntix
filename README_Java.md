# Juntix - Prototipo de aplicación de oficios (Java + JavaFX + MySQL)

**Descripción breve**  
Juntix es un prototipo de aplicación de escritorio desarrollado en Java y JavaFX, con persistencia en MySQL. El objetivo es conectar trabajadores de oficios con clientes locales. Este repositorio contiene el código fuente del prototipo, scripts SQL y utilidades para poblar la base de datos.

---

## Estructura del repositorio
- `src/` - código fuente Java (paquetes `com.juntix.*`)
- `resources/application.properties` - configuración de BD
- `sql/schema.sql` - script para crear tablas
- `sql/seed.sql` - script de datos iniciales
- `tools/DBSeeder.java` - clase Java para insertar usuarios de prueba con hashes válidos

---

## Requisitos
- Java JDK 11+ (se recomienda JDK 17)
- JavaFX SDK compatible con la versión de JDK
- MySQL 8+
- MySQL Connector/J (JAR)
- IDE recomendado: IntelliJ IDEA o Eclipse (con soporte JavaFX)

---

## Configuración de la base de datos
1. Crear la base y el usuario (ejemplo):
   ```sql
   CREATE DATABASE juntix CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   CREATE USER 'juntix_user'@'localhost' IDENTIFIED BY 'juntix_pass';
   GRANT ALL PRIVILEGES ON juntix.* TO 'juntix_user'@'localhost';
   FLUSH PRIVILEGES;
