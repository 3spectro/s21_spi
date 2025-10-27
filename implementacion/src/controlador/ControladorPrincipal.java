package controlador;

import vista.*;

import javax.swing.*;

public class ControladorPrincipal {

    private final VentanaPrincipal vista;
    private final PanelInventario panelInventario = new PanelInventario();
    private final PanelVentasLista panelVentasLista = new PanelVentasLista();

    public ControladorPrincipal(VentanaPrincipal vista) {
        this.vista = vista;

        // Controlador del listado de ventas
        new ControladorVentasLista(panelVentasLista);

        // Arranque: mostrar Ventas (listado) y marcar el tile
        this.vista.setContent(panelVentasLista);
        this.vista.getSidebar().getBtnVentas().setSelected(true);

        // Sidebar navigation
        this.vista.getSidebar().getBtnVentas().addActionListener(e -> {
            this.vista.setContent(panelVentasLista);
            this.vista.getSidebar().getBtnVentas().setSelected(true);
        });

        this.vista.getSidebar().getBtnInventario().addActionListener(e -> {
            this.vista.setContent(panelInventario);
            this.vista.getSidebar().getBtnInventario().setSelected(true);
        });

        this.vista.getSidebar().getBtnSalir().addActionListener(e -> salir());

        // Desde el listado: abrir wizard modal
        panelVentasLista.setOnNuevaVenta(() -> {
            java.awt.Window win = javax.swing.SwingUtilities.getWindowAncestor(vista);
            new DialogNuevaVenta(win, true).setVisible(true);
            // tras cerrar, podés refrescar el listado:
            // new ControladorVentasLista(panelVentasLista);  // si querés recargar al vuelo
        });
    }

    private void salir() {
        int r = JOptionPane.showConfirmDialog(vista, "¿Desea salir?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) System.exit(0);
    }
}
