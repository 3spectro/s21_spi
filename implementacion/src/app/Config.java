package app;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties props = new Properties();
    static {
        try (InputStream is = Config.class.getClassLoader().getResourceAsStream("app.properties")) {
            if (is != null) props.load(is);
            else System.err.println("No se encontró app.properties en resources/");
        } catch (Exception e) {
            System.err.println("Error cargando configuración: " + e.getMessage());
        }
    }
    public static String get(String key, String def) { return props.getProperty(key, def); }
    public static boolean getBool(String key, boolean def) {
        String v = props.getProperty(key);
        return v == null ? def : v.equalsIgnoreCase("true") || v.equals("1");
    }
}
