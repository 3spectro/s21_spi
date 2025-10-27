package controlador;

import vista.PanelVentasLista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.List;

public class ControladorVentasLista {
    private final PanelVentasLista vista;
    private final persistencia.VentaDao dao = new persistencia.VentaDao();

    public ControladorVentasLista(PanelVentasLista vista) {
        this.vista = vista;
        this.vista.getBtnFiltrar().addActionListener(e -> cargar());
        cargar(); // primera carga
    }

    private void cargar() {
        try {
            LocalDate d = vista.getDesde();
            LocalDate h = vista.getHasta();
            List<VentaResumen> data = dao.listarResumen(d, h);

            DefaultTableModel m = (DefaultTableModel) vista.getTabla().getModel();
            m.setRowCount(0);
            double total = 0;
            for (VentaResumen r : data) {
                m.addRow(new Object[]{ r.fecha(), r.total(), r.cantidadItems() });
                total += r.total().doubleValue();
            }
            vista.getLblTotal().setText("Total: " + total);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // record para transportar datos desde el DAO
    public static record VentaResumen(java.time.LocalDate fecha, java.math.BigDecimal total, int cantidadItems) {}
}
