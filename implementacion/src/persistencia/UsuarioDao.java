package persistencia;

import modelo.Usuario;
import java.sql.*;

public class UsuarioDao {
    public Usuario buscarPorUsername(String username) throws Exception {
        // Ajustado a columnas reales: user_username y user_password
        String sql = "SELECT user_id, user_username, user_password FROM app_user WHERE user_username = ?";
        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getLong("user_id"));
                    u.setUsername(rs.getString("user_username"));
                    // usamos el campo existente user_password (no hay hash)
                    u.setPasswordHash(rs.getString("user_password"));
                    // si no existe columna de rol, por ahora default ADMIN; se ajustará cuando tengamos roles en DB
                    u.setRol("ADMIN");
                    return u;
                }
                return null;
            }
        }
    }
}
