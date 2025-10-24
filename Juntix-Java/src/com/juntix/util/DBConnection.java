package com.juntix.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection
 * Provee conexiones JDBC usando DriverManager y la configuración centralizada.
 * En prototipo se utiliza DriverManager; en producción podría reemplazarse por un pool.
 */
public class DBConnection {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC MySQL no encontrado: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DBConfig.getUrl(), DBConfig.getUser(), DBConfig.getPass());
    }
}
