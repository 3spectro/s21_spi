package servicio;

import app.Config;
import modelo.Usuario;
import persistencia.UsuarioDao;

public class ServicioAuth {
    private final UsuarioDao usuarioDao = new UsuarioDao();

    public Usuario login(String username, String passwordPlano) throws Exception {
        // Modo demo: permite admin/admin sin DB para primer arranque
        boolean demo = Config.getBool("demo.mode", true);
        if (demo && "admin".equals(username) && "admin".equals(passwordPlano)) {
            Usuario u = new Usuario();
            u.setId(1L);
            u.setUsername("admin");
            u.setPasswordHash("admin");
            u.setRol("ADMIN");
            return u;
        }
        Usuario u = usuarioDao.buscarPorUsername(username);
        if (u == null) throw new IllegalArgumentException("Usuario inexistente");
        if (!verificar(passwordPlano, u.getPasswordHash()))
            throw new IllegalArgumentException("Contraseña inválida");
        return u;
    }
    private boolean verificar(String plano, String hash) {
        // MVP: comparación directa (luego se reemplaza por hash real)
        return hash != null && hash.equals(plano);
    }
}
