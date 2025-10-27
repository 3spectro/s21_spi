package vista;

import controlador.ControladorInventario;
import util.ReadOnlyTableModel;

import javax.swing.*;
import java.awt.*;

public class PanelInventario extends JPanel {
    // Renombrado visualmente a "Actualizar" (dejamos alias getBtnCargar() para compatibilidad)
    private final JButton btnActualizar = new JButton("Actualizar");
    private final JTextField txtFiltro = new JTextField();

    private final JTable tabla = new JTable(new ReadOnlyTableModel(
            new Object[]{"Código","Nombre","Precio","Stock"}, 0));

    private final JLabel lblEstado = new JLabel(" ");

    public PanelInventario() {
        setLayout(new BorderLayout(6,6));

        // --- TOP: Filtro + textbox + botón (en ese orden) ---
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        top.add(new JLabel("Filtro:"));
        txtFiltro.setPreferredSize(new Dimension(260, 26));
        top.add(txtFiltro);
        btnActualizar.setFocusPainted(false);
        top.add(btnActualizar);
        add(top, BorderLayout.NORTH);

        // --- CENTRO: Tabla ---
        configurarTabla(tabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // --- BOTTOM: Estado a la derecha con padding ---
        JPanel bottom = new JPanel(new BorderLayout());
        lblEstado.setHorizontalAlignment(SwingConstants.RIGHT);
        // padding top/bottom más generoso
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        bottom.add(lblEstado, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        // Controlador (se mantiene igual)
        new ControladorInventario(this);
    }

    private void configurarTabla(JTable t) {
        t.setRowSelectionAllowed(true);
        t.setColumnSelectionAllowed(false);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setSelectionBackground(new Color(0,120,215)); // azul
        t.setSelectionForeground(Color.WHITE);
        t.setAutoCreateRowSorter(true); // ordenar columnas
        t.setShowGrid(false);
        t.setFillsViewportHeight(true);
    }

    // --- Getters para el controlador ---
    public JButton getBtnActualizar(){ return btnActualizar; }
    // Alias para compatibilidad con código previo (ControladorInventario esperaba getBtnCargar)
    public JButton getBtnCargar(){ return btnActualizar; }

    public JTextField getTxtFiltro(){ return txtFiltro; }
    public JTable getTabla(){ return tabla; }
    public JLabel getLblEstado(){ return lblEstado; }
}
