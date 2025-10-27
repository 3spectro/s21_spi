package vista;

import modelo.Item;
import modelo.LineaVenta;
import modelo.Venta;
import servicio.ServicioInventario;
import servicio.ServicioVentas;
import util.ReadOnlyTableModel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class DialogNuevaVenta extends JDialog {

    private final ServicioInventario servicioInventario = new ServicioInventario();
    private final ServicioVentas servicioVentas = new ServicioVentas();

    private final Venta venta = new Venta();

    private final CardLayout cards = new CardLayout();
    private final JPanel cardPanel = new JPanel(cards);

    // Paso 1
    private final JSpinner spFecha = new JSpinner(new SpinnerDateModel());
    private final JComboBox<String> cmbPago = new JComboBox<>();
    private final JButton btnSiguiente = new JButton("Siguiente");
    private final JButton btnAnterior = new JButton("Anterior");
    private final JButton btnCerrar1 = new JButton("Cerrar");

    // Paso 2
    private final JTextField txtCodigo = new JTextField(14);
    private final JButton btnBuscar = new JButton("Buscar");
    private final JPanel panelFicha = new JPanel(new GridLayout(2, 3, 12, 6));
    private final JButton btnAgregar = new JButton("Agregar");

    // Ahora con columna "Eliminar"
    private final JTable tabla = new JTable(new ReadOnlyTableModel(
            new Object[]{"Código","Artículo","Precio","Cant.","Subtotal","Eliminar"}, 0));

    private final JLabel lblCant = new JLabel("Cant. ítems: 0");
    private final JLabel lblTotal = new JLabel("Valor total: $0,00");
    private final JButton btnConfirmar = new JButton("Confirmar");
    private final JButton btnCerrar2 = new JButton("Cerrar");

    private Item ultimoItemBuscado;

    public DialogNuevaVenta(Window owner, boolean modal) {
        super(owner, "Nueva venta", ModalityType.APPLICATION_MODAL);
        setModal(modal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(720, 520);
        setLocationRelativeTo(owner);
        getRootPane().setBorder(new LineBorder(new Color(200, 200, 210), 1, true));

        // ---------- Paso 1 ----------
        JPanel paso1 = new JPanel(new BorderLayout(12, 12));
        paso1.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        JLabel titulo1 = new JLabel("Nueva venta");
        titulo1.setFont(titulo1.getFont().deriveFont(Font.BOLD, 18f));
        paso1.add(titulo1, BorderLayout.NORTH);

        JPanel form1 = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.WEST;

        spFecha.setEditor(new JSpinner.DateEditor(spFecha, "dd/MM/yyyy"));

        c.gridx=0; c.gridy=0; form1.add(new JLabel("Fecha:"), c);
        c.gridx=1; c.gridy=0; form1.add(spFecha, c);
        c.gridx=0; c.gridy=1; form1.add(new JLabel("Método de pago:"), c);
        c.gridx=1; c.gridy=1; cmbPago.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXX");
        form1.add(cmbPago, c);
        paso1.add(form1, BorderLayout.CENTER);

        JPanel nav1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nav1.add(btnAnterior); btnAnterior.setEnabled(false);
        nav1.add(btnSiguiente);
        nav1.add(btnCerrar1);
        paso1.add(nav1, BorderLayout.SOUTH);

        // ---------- Paso 2 ----------
        JPanel paso2 = new JPanel(new BorderLayout(12, 12));
        paso2.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        JLabel titulo2 = new JLabel("Nueva venta");
        titulo2.setFont(titulo2.getFont().deriveFont(Font.BOLD, 18f));
        paso2.add(titulo2, BorderLayout.NORTH);

        JPanel buscar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        buscar.add(new JLabel("Código:"));
        buscar.add(txtCodigo);
        buscar.add(btnBuscar);
        paso2.add(buscar, BorderLayout.BEFORE_FIRST_LINE);

        panelFicha.setBorder(new LineBorder(new Color(160, 180, 220), 2, true));
        rebuildFicha(null);
        JPanel fichaWrap = new JPanel(new BorderLayout());
        fichaWrap.add(panelFicha, BorderLayout.CENTER);
        JPanel fichaActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        fichaActions.add(btnAgregar);
        btnAgregar.setEnabled(false);
        fichaWrap.add(fichaActions, BorderLayout.SOUTH);

        configurarTabla(tabla);
        JPanel centro = new JPanel(new BorderLayout(8, 8));
        centro.add(fichaWrap, BorderLayout.NORTH);
        centro.add(new JScrollPane(tabla), BorderLayout.CENTER);
        paso2.add(centro, BorderLayout.CENTER);

        // Resumen + navegación
        JPanel pie2 = new JPanel(new BorderLayout());
        JPanel pieLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pieLeft.add(lblCant);
        JPanel pieRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pieRight.add(lblTotal);
        pie2.add(pieLeft, BorderLayout.WEST);
        pie2.add(pieRight, BorderLayout.EAST);

        JPanel nav2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nav2.add(btnAnterior);
        nav2.add(btnConfirmar);
        nav2.add(btnCerrar2);

        JPanel south2 = new JPanel(new BorderLayout());
        south2.add(pie2, BorderLayout.NORTH);
        south2.add(nav2, BorderLayout.SOUTH);

        paso2.add(south2, BorderLayout.SOUTH);

        // Cards
        cardPanel.add(paso1, "P1");
        cardPanel.add(paso2, "P2");
        add(cardPanel);

        // Cargar métodos de pago
        cargarMetodosPago();

        // Eventos
        btnSiguiente.addActionListener(e -> avanzarAPaso2());
        btnAnterior.addActionListener(e -> cards.show(cardPanel, "P1"));
        btnCerrar1.addActionListener(e -> dispose());
        btnCerrar2.addActionListener(e -> dispose());
        btnBuscar.addActionListener(e -> buscarItem());
        btnAgregar.addActionListener(e -> agregarLinea());
        btnConfirmar.addActionListener(e -> confirmarVenta());

        // Click en columna "Eliminar"
        tabla.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int viewCol = tabla.columnAtPoint(e.getPoint());
                int viewRow = tabla.rowAtPoint(e.getPoint());
                if (viewRow < 0 || viewCol < 0) return;
                int modelCol = tabla.convertColumnIndexToModel(viewCol);
                int modelRow = tabla.convertRowIndexToModel(viewRow);
                if (modelCol == 5) { // columna "Eliminar"
                    eliminarFila(modelRow);
                }
            }
        });

        actualizarResumen(); // inicia en 0/0
    }

    private void configurarTabla(JTable t) {
        t.setRowSelectionAllowed(true);
        t.setColumnSelectionAllowed(false);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setSelectionBackground(new Color(0,120,215));
        t.setSelectionForeground(Color.WHITE);
        t.setShowGrid(false);
        t.setFillsViewportHeight(true);

        // Renderer estilo "link" para columna Eliminar
        DefaultTableCellRenderer link = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                setForeground(new Color(80, 60, 160));
                setText("<html><u>Eliminar</u></html>");
                if (isSelected) setForeground(Color.WHITE);
                return c;
            }
        };
        t.getColumnModel().getColumn(5).setCellRenderer(link);
        t.getColumnModel().getColumn(5).setPreferredWidth(70);
    }

    private void cargarMetodosPago() {
        try {
            List<String> metodos = persistencia.VentaDao.listarMetodosPago();
            cmbPago.removeAllItems();
            cmbPago.addItem("Seleccione un elemento");
            for (String m : metodos) cmbPago.addItem(m);
            cmbPago.setSelectedIndex(0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar métodos de pago.\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void avanzarAPaso2() {
        if (cmbPago.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un método de pago.");
            return;
        }
        venta.setFecha(utilFechas.toLocalDate((java.util.Date) spFecha.getValue()));
        venta.setMetodoPago((String) cmbPago.getSelectedItem());
        cards.show(cardPanel, "P2");
    }

    private void buscarItem() {
        String codigo = txtCodigo.getText().trim();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un código.");
            return;
        }
        try {
            Item it = servicioInventario.porCodigo(codigo);
            if (it == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el ítem.");
                rebuildFicha(null);
                btnAgregar.setEnabled(false);
                return;
            }
            ultimoItemBuscado = it;
            rebuildFicha(it);
            btnAgregar.setEnabled(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarLinea() {
        if (ultimoItemBuscado == null) return;
        LineaVenta l = new LineaVenta();
        l.setItem(ultimoItemBuscado);
        l.setCantidad(1);

        venta.getLineas().add(l);

        DefaultTableModel m = (DefaultTableModel) tabla.getModel();
        m.addRow(new Object[]{
                ultimoItemBuscado.getCodigo(),
                ultimoItemBuscado.getNombre(),
                ultimoItemBuscado.getPrecio(),
                l.getCantidad(),
                l.calcularTotal(),
                "Eliminar"
        });

        ultimoItemBuscado = null;
        rebuildFicha(null);
        btnAgregar.setEnabled(false);
        txtCodigo.setText("");

        actualizarResumen();
    }

    private void eliminarFila(int modelRow) {
        if (modelRow < 0) return;
        // Eliminar de memoria y de la tabla
        if (modelRow < venta.getLineas().size()) {
            venta.getLineas().remove(modelRow);
        }
        ((DefaultTableModel) tabla.getModel()).removeRow(modelRow);
        actualizarResumen();
    }

    /** Recalcula cantidad de ítems (suma de cantidades) y el total. */
    private void actualizarResumen() {
        // total
        BigDecimal total = venta.getTotal();
        lblTotal.setText("Valor total: $" + total);

        // cantidad de ítems = suma de cantidades
        int cant = 0;
        for (LineaVenta lv : venta.getLineas()) {
            cant += lv.getCantidad();
        }
        lblCant.setText("Cant. ítems: " + cant);
    }

    private void confirmarVenta() {
        if (venta.getLineas().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un ítem.");
            return;
        }
        try {
            servicioVentas.confirmar(venta);
            JOptionPane.showMessageDialog(this, "Venta registrada.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo registrar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rebuildFicha(Item it) {
        panelFicha.removeAll();
        panelFicha.setOpaque(true);
        panelFicha.setBackground(new Color(230, 240, 255));
        if (it == null) {
            panelFicha.add(new JLabel("No hay ítem seleccionado."));
            panelFicha.revalidate(); panelFicha.repaint();
            return;
        }
        panelFicha.add(negritaValor("Artículo:", it.getNombre()));
        panelFicha.add(negritaValor("Precio:", "$" + it.getPrecio()));
        panelFicha.add(negritaValor("Stock:", String.valueOf(it.getStock())));
        panelFicha.revalidate(); panelFicha.repaint();
    }

    private JPanel negritaValor(String k, String v) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        JLabel lk = new JLabel(k); lk.setFont(lk.getFont().deriveFont(Font.BOLD));
        JLabel lv = new JLabel(v);
        p.add(lk); p.add(lv);
        p.setOpaque(false);
        return p;
    }

    private static class utilFechas {
        static LocalDate toLocalDate(java.util.Date d) {
            return d.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        }
    }
}
