package com.juntix.tools;

import com.juntix.util.DBConnection;
import com.juntix.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * DBSeeder
 * Herramienta para poblar la base de datos con datos de prueba.
 * Genera hashes seguros mediante PasswordUtil.generarHash(password).
 * Ejecutar localmente una sola vez.
 */
public class DBSeeder {
    public static void main(String[] args) {
        try (Connection c = DBConnection.getConexion()) {

            // Generar hashes seguros para los usuarios
            String hashCliente1 = PasswordUtil.generarHash("Cliente123!");
            String hashCliente2 = PasswordUtil.generarHash("Cliente456!");
            String hashTrab1 = PasswordUtil.generarHash("Trabajador123!");
            String hashTrab2 = PasswordUtil.generarHash("Trabajador456!");
            String hashTrab3 = PasswordUtil.generarHash("Trabajador789!");

            // Mostrar hashes generados en consola
            System.out.println("=== Hashes generados ===");
            System.out.println("cliente1@juntix.test -> " + hashCliente1);
            System.out.println("cliente2@juntix.test -> " + hashCliente2);
            System.out.println("trabajador1@juntix.test -> " + hashTrab1);
            System.out.println("trabajador2@juntix.test -> " + hashTrab2);
            System.out.println("trabajador3@juntix.test -> " + hashTrab3);
            System.out.println("========================");

            // Insertar usuarios de prueba
            String sqlUser = """
                INSERT INTO Usuario(email, password_hash, rol, telefono)
                VALUES (?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE telefono = VALUES(telefono)
            """;

            try (PreparedStatement ps = c.prepareStatement(sqlUser)) {
                // Cliente 1
                ps.setString(1, "cliente1@juntix.test");
                ps.setString(2, hashCliente1);
                ps.setString(3, "CLIENTE");
                ps.setString(4, "3511111111");
                ps.executeUpdate();

                // Cliente 2
                ps.setString(1, "cliente2@juntix.test");
                ps.setString(2, hashCliente2);
                ps.setString(3, "CLIENTE");
                ps.setString(4, "3512222222");
                ps.executeUpdate();

                // Trabajador 1
                ps.setString(1, "trabajador1@juntix.test");
                ps.setString(2, hashTrab1);
                ps.setString(3, "TRABAJADOR");
                ps.setString(4, "3513333333");
                ps.executeUpdate();

                // Trabajador 2
                ps.setString(1, "trabajador2@juntix.test");
                ps.setString(2, hashTrab2);
                ps.setString(3, "TRABAJADOR");
                ps.setString(4, "3514444444");
                ps.executeUpdate();

                // Trabajador 3
                ps.setString(1, "trabajador3@juntix.test");
                ps.setString(2, hashTrab3);
                ps.setString(3, "TRABAJADOR");
                ps.setString(4, "3515555555");
                ps.executeUpdate();
            }

            // === Insertar oficios ===
            String sqlOf = "INSERT IGNORE INTO Oficio(nombre) VALUES (?)";
            try (PreparedStatement ps2 = c.prepareStatement(sqlOf)) {
                String[] oficios = {"Jardinería", "Pintura", "Plomería", "Electricidad", "Limpieza"};
                for (String o : oficios) {
                    ps2.setString(1, o);
                    ps2.executeUpdate();
                }
            }

            System.out.println("Seed ejecutado correctamente. Los usuarios ya están listos para iniciar sesión.");

        } catch (Exception e) {
            System.err.println("Error al seedear BD: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

