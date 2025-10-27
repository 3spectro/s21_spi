package controlador;

import modelo.Item;
import modelo.LineaVenta;
import modelo.Venta;
import servicio.ServicioInventario;
import servicio.ServicioVentas;
import vista.PanelVenta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;

public class ControladorVentas {
    private final PanelVenta vista;
    private final ServicioVentas servicioVentas = new ServicioVentas();
    private final ServicioInventario servicioInventario = new ServicioInventario();
    private final Venta venta = new Venta();

    public ControladorVentas(PanelVenta vista) {
        this.vista = vista;
        this.vista.getBtnAgregar().addActionListener(e -> agregarLinea());
        this.vista.getBtnConfirmar().addActionListener(e -> confirmarVenta());
        this.vista.getTxtFecha().setText(LocalDate.now().toString());
        this.vista.getCmbPago().addItem("Efectivo");
        this.vista.getCmbPago().addItem("Débito");
        this.vista.getCmbPago().addItem("Crédito");
        actualizarTotal();
    }

    private void agregarLinea() {
        String codigo = vista.getTxtCodigo().getText().trim();
        String cantidadStr = vista.getTxtCantidad().getText().trim();
        if (codigo.isEmpty() || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Código y cantidad son obligatorios.");
            return;
        }
        int cantidad;
        try { cantidad = Integer.parseInt(cantidadStr); }
        catch (NumberFormatException ex) { JOptionPane.showMessageDialog(vista, "Cantidad inválida."); return; }

        try {
            Item item = servicioInventario.porCodigo(codigo);
            if (item == null) { JOptionPane.showMessageDialog(vista, "Item no encontrado."); return; }
            if (item.getStock() < cantidad) { JOptionPane.showMessageDialog(vista, "Stock insuficiente."); return; }

            LineaVenta l = new LineaVenta();
            l.setItem(item);
            l.setCantidad(cantidad);
            venta.getLineas().add(l);

            DefaultTableModel model = (DefaultTableModel) vista.getTabla().getModel();
            model.addRow(new Object[]{item.getCodigo(), item.getNombre(), cantidad, item.getPrecio(), l.calcularTotal()});

            actualizarTotal();
            vista.getTxtCodigo().setText("");
            vista.getTxtCantidad().setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmarVenta() {
        try {
            venta.setFecha(LocalDate.parse(vista.getTxtFecha().getText().trim()));
            venta.setMetodoPago((String) vista.getCmbPago().getSelectedItem());
            servicioVentas.confirmar(venta);
            JOptionPane.showMessageDialog(vista, "Venta registrada con éxito.");
            ((DefaultTableModel) vista.getTabla().getModel()).setRowCount(0);
            venta.getLineas().clear();
            actualizarTotal();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage(), "No se pudo registrar la venta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTotal() {
        vista.getLblTotal().setText("Total: " + venta.getTotal());
    }
}
