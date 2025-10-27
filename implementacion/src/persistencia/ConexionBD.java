package persistencia;

import app.Config;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionBD {
    public static Connection obtener() throws Exception {
        String url  = Config.get("db.url", "jdbc:mysql://localhost:3306/talina?useSSL=false&serverTimezone=UTC");
        String user = Config.get("db.user", "root");
        String pass = Config.get("db.password", "");
        return DriverManager.getConnection(url, user, pass);
    }
}
