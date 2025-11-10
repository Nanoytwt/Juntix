package com.juntix.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class DBConfig {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = DBConfig.class.getResourceAsStream("/application.properties")) {
            if (in != null) PROPS.load(in);
            else System.err.println("⚠ No se encontró application.properties. Usando valores por defecto.");
        } catch (IOException e) {
            System.err.println("Error al cargar configuración: " + e.getMessage());
        }
    }

    private DBConfig() {}

    public static String getUrl() {
        return PROPS.getProperty("db.url", "jdbc:mysql://localhost:3306/juntix?useSSL=false&serverTimezone=UTC");
    }

    public static String getUser() {
        return PROPS.getProperty("db.user", "root");
    }

    public static String getPass() {
        return PROPS.getProperty("db.pass", "root");
    }
}


