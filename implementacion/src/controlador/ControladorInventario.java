package controlador;

import servicio.ServicioInventario;
import vista.PanelInventario;
import modelo.Item;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;

public class ControladorInventario {
    private final PanelInventario vista;
    private final ServicioInventario servicio = new ServicioInventario();
    private TableRowSorter<DefaultTableModel> sorter;

    public ControladorInventario(PanelInventario vista) {
        this.vista = vista;
        this.vista.getBtnCargar().addActionListener(e -> cargarTodo());
        this.vista.getTxtFiltro().getDocument().addDocumentListener(new util.SimpleDocumentListener(this::filtrar));
        configurarTabla();
    }

    private void configurarTabla() {
        DefaultTableModel model = (DefaultTableModel) vista.getTabla().getModel();
        sorter = new TableRowSorter<>(model);
        vista.getTabla().setRowSorter(sorter);
    }

    private void cargarTodo() {
        try {
            List<Item> items = servicio.cargarInventarioCompleto();
            DefaultTableModel model = (DefaultTableModel) vista.getTabla().getModel();
            model.setRowCount(0);
            for (Item it : items) {
                model.addRow(new Object[]{it.getCodigo(), it.getNombre(), it.getPrecio(), it.getStock()});
            }
            vista.getLblEstado().setText("Items cargados: " + items.size());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage(), "Error al cargar inventario", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtrar() {
        String texto = vista.getTxtFiltro().getText().trim();
        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(texto)));
        }
    }
}
