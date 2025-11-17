package vista;

import controlador.ControladorInventario;
import util.ReadOnlyTableModel;

import javax.swing.*;
import java.awt.*;

public class PanelInventario extends JPanel {
    // Renombrado visualmente a "Actualizar" (dejamos alias getBtnCargar() para compatibilidad)
    private final JButton btnActualizar = new JButton("Actualizar");
    private final JButton btnExportar = new JButton("Exportar CSV");
    private final JTextField txtFiltro = new JTextField();

    private final JTable tabla = new JTable(new ReadOnlyTableModel(
            new Object[]{"Código","Nombre","Precio","Stock"}, 0));

    private final JLabel lblEstado = new JLabel(" ");

    public PanelInventario() {
        setLayout(new BorderLayout(6, 6));

        // --- TOP: Filtro + textbox + botones ---
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        top.add(new JLabel("Filtro:"));
        txtFiltro.setPreferredSize(new Dimension(260, 26));
        top.add(txtFiltro);

        btnActualizar.setFocusPainted(false);
        top.add(btnActualizar);

        btnExportar.setFocusPainted(false);
        top.add(btnExportar);

        add(top, BorderLayout.NORTH);

        // --- CENTRO: Tabla ---
        configurarTabla(tabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // --- BOTTOM: Estado a la derecha con padding ---
        JPanel bottom = new JPanel(new BorderLayout());
        lblEstado.setHorizontalAlignment(SwingConstants.RIGHT);
        bottom.add(lblEstado, BorderLayout.CENTER);
        bottom.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        add(bottom, BorderLayout.SOUTH);

        // Crear el controlador
        new ControladorInventario(this);
    }

    private void configurarTabla(JTable t) {
        t.setRowSelectionAllowed(true);
        t.setColumnSelectionAllowed(false);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setSelectionBackground(new Color(0, 120, 215)); // azul
        t.setSelectionForeground(Color.WHITE);
        t.setAutoCreateRowSorter(true); // ordenar columnas
        t.setShowGrid(false);
        t.setFillsViewportHeight(true);
    }

    // --- Getters para el controlador ---
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnExportar() { return btnExportar; }
    // Alias para compatibilidad con código previo (ControladorInventario esperaba getBtnCargar)
    public JButton getBtnCargar() { return btnActualizar; }

    public JTextField getTxtFiltro() { return txtFiltro; }
    public JTable getTabla() { return tabla; }
    public JLabel getLblEstado() { return lblEstado; }
}

