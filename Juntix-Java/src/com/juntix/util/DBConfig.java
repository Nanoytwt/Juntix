package com.juntix.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * DBConfig
 * Lee parámetros de conexión desde resources/application.properties.
 * Facilita la configuración sin recompilar el código.
 */
public class DBConfig {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = DBConfig.class.getResourceAsStream("/application.properties")) {
            if (in != null) {
                PROPS.load(in);
            }
        } catch (IOException e) {
            System.err.println("No se pudo cargar application.properties: " + e.getMessage());
        }
    }

    public static String getUrl() {
        return PROPS.getProperty("db.url", "jdbc:mysql://localhost:3306/juntix?useSSL=false&serverTimezone=UTC");
    }

    public static String getUser() {
        return PROPS.getProperty("db.user", "juntix_user");
    }

    public static String getPass() {
        return PROPS.getProperty("db.pass", "juntix_pass");
    }
}
