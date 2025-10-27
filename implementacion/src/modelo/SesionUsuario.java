package modelo;

public class SesionUsuario {
    private static Usuario actual;

    public static void set(Usuario u) { actual = u; }
    public static Usuario get() { return actual; }
    public static boolean haySesion() { return actual != null; }
    public static void cerrar() { actual = null; }
}
