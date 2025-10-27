package vista;

import controlador.ControladorPrincipal;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private final SidebarLateral sidebar = new SidebarLateral();
    private final JPanel content = new JPanel(new BorderLayout());

    public VentanaPrincipal() {
        super("Sistema de Gestión - Ejemplo MVC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);

        new ControladorPrincipal(this);
    }

    public void setContent(JComponent comp) {
        content.removeAll();
        content.add(comp, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }

    public SidebarLateral getSidebar() { return sidebar; }
}
