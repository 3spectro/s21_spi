package controlador;

import servicio.ServicioInventario;
import vista.PanelInventario;
import modelo.Item;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.util.List;

public class ControladorInventario {

    private final PanelInventario vista;
    private final ServicioInventario servicio = new ServicioInventario();
    private TableRowSorter<DefaultTableModel> sorter;

    public ControladorInventario(PanelInventario vista) {
        this.vista = vista;

        // Botón actualizar (alias getBtnCargar se mantiene por compatibilidad)
        this.vista.getBtnCargar().addActionListener(e -> cargarTodo());

        // Nuevo: botón exportar CSV
        this.vista.getBtnExportar().addActionListener(e -> exportarCsv());

        // Filtro en tiempo real
        this.vista.getTxtFiltro()
                .getDocument()
                .addDocumentListener(new util.SimpleDocumentListener(this::filtrar));

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
                model.addRow(new Object[]{
                        it.getCodigo(),
                        it.getNombre(),
                        it.getPrecio(),
                        it.getStock()
                });
            }
            vista.getLblEstado().setText("Items cargados: " + items.size());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    vista,
                    ex.getMessage(),
                    "Error al cargar inventario",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void exportarCsv() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar inventario (CSV compatible con Excel)");
        chooser.setSelectedFile(new java.io.File("inventario.csv"));

        int res = chooser.showSaveDialog(vista);

        if (res == JFileChooser.APPROVE_OPTION) {
            java.io.File file = chooser.getSelectedFile();

            // Forzar extensión .csv
            String path = file.getAbsolutePath();
            if (!path.toLowerCase().endsWith(".csv")) {
                path = path + ".csv";
                file = new java.io.File(path);
            }

            try {
                servicio.exportarInventarioCsv(path);
                JOptionPane.showMessageDialog(
                        vista,
                        "Inventario exportado correctamente a:\n" + file.getAbsolutePath(),
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        vista,
                        "Error al exportar inventario: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void filtrar() {
        String texto = vista.getTxtFiltro().getText().trim();
        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(
                    RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(texto))
            );
        }
    }
}
