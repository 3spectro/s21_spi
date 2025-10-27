package controlador;

import modelo.SesionUsuario;
import modelo.Usuario;
import servicio.ServicioAuth;
import vista.VentanaLogin;
import vista.VentanaPrincipal;

import javax.swing.*;

public class ControladorLogin {
    private final VentanaLogin vista;
    private final ServicioAuth auth = new ServicioAuth();

    public ControladorLogin(VentanaLogin vista) {
        this.vista = vista;
        this.vista.getBtnIngresar().addActionListener(e -> ingresar());
    }

    private void ingresar() {
        String user = vista.getTxtUsuario().getText().trim();
        String pass = new String(vista.getTxtPassword().getPassword());
        try {
            Usuario u = auth.login(user, pass);
            SesionUsuario.set(u);
            vista.dispose();
            new VentanaPrincipal().setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage(), "Error de autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }
}
