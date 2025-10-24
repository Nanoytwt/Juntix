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
        try (Connection c = DBConnection.getConnection()) {
            String hashCliente = PasswordUtil.generarHash("Cliente123!");
            String hashTrab = PasswordUtil.generarHash("Trabajador123!");

            String sqlUser = "INSERT INTO Usuario(email, password_hash, rol, telefono) VALUES(?, ?, ?, ?)";
            try (PreparedStatement ps = c.prepareStatement(sqlUser)) {
                ps.setString(1, "cliente1@juntix.test");
                ps.setString(2, hashCliente);
                ps.setString(3, "CLIENTE");
                ps.setString(4, "3511111111");
                ps.executeUpdate();

                ps.setString(1, "trabajador1@juntix.test");
                ps.setString(2, hashTrab);
                ps.setString(3, "TRABAJADOR");
                ps.setString(4, "3512222222");
                ps.executeUpdate();
            }

            String sqlOf = "INSERT IGNORE INTO Oficio(nombre) VALUES (?)";
            try (PreparedStatement ps2 = c.prepareStatement(sqlOf)) {
                String[] oficios = {"Jardinería","Pintura","Plomería","Electricidad","Limpieza"};
                for (String o : oficios) {
                    ps2.setString(1, o);
                    ps2.executeUpdate();
                }
            }

            System.out.println("Seed ejecutado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al seedear BD: " + e.getMessage());
        }
    }
}
