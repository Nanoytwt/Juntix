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

    // Instancia única de la clase (patrón Singleton)
    private static DBConnection instancia;

    // Constructor privado: evita instanciación directa
    private DBConnection() {
        try {
            // Registro explícito del driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: el controlador MySQL no se encontró en el classpath.");
            throw new RuntimeException("Driver JDBC MySQL no encontrado", e);
        }
    }

    /**
     * Retorna la instancia única de DBConnection.
     * Si aún no fue creada, la instancia se inicializa de forma segura (thread-safe).
     */
    public static synchronized DBConnection getInstancia() {
        if (instancia == null) {
            instancia = new DBConnection();
        }
        return instancia;
    }

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(
                DBConfig.getUrl(),
                DBConfig.getUser(),
                DBConfig.getPass()
        );
    }
}