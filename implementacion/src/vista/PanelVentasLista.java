package vista;

import util.ReadOnlyTableModel;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PanelVentasLista extends JPanel {
    private final JSpinner spDesde = new JSpinner(new SpinnerDateModel());
    private final JSpinner spHasta = new JSpinner(new SpinnerDateModel());

    private final JButton btnSemana = new JButton("Semana actual");
    private final JButton btnMes = new JButton("Mes actual");
    private final JButton btnFiltrar = new JButton("Filtrar");

    private final JButton btnNueva = new JButton("Nueva venta");  // ahora va arriba a la derecha

    private final JTable tabla = new JTable(new ReadOnlyTableModel(
            new Object[]{"Fecha venta","Total venta","Cant. ítems"}, 0));
    private final JLabel lblTotal = new JLabel("Total: 0");

    private Runnable onNuevaVenta;

    public PanelVentasLista() {
        setLayout(new BorderLayout(8,8));

        spDesde.setEditor(new JSpinner.DateEditor(spDesde, "dd/MM/yyyy"));
        spHasta.setEditor(new JSpinner.DateEditor(spHasta, "dd/MM/yyyy"));

        spDesde.setValue(Date.from(LocalDate.now().minusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        spHasta.setValue(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        // ----- TOP: Filtros a la izquierda + "Nueva venta" a la derecha -----
        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.setBorder(BorderFactory.createEmptyBorder(8,8,0,8));

        // Izquierda: filtros
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        filtros.add(new JLabel("Desde:")); filtros.add(spDesde);
        filtros.add(new JLabel("Hasta:")); filtros.add(spHasta);
        filtros.add(btnSemana); filtros.add(btnMes);
        filtros.add(btnFiltrar);
        top.add(filtros, BorderLayout.WEST);

        // Derecha: botón Nueva venta alineado a la derecha
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        estilizarBotonNueva(btnNueva);
        Icon icoNueva = cargarIcono("nueva.png", 18, 18);
        if (icoNueva != null) btnNueva.setIcon(icoNueva); // icono a la izquierda del texto
        acciones.add(btnNueva);
        top.add(acciones, BorderLayout.EAST);

        // Separador debajo del top
        JPanel topWrap = new JPanel(new BorderLayout());
        topWrap.add(top, BorderLayout.CENTER);
        topWrap.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.SOUTH);

        add(topWrap, BorderLayout.NORTH);

        // ----- CENTRO: tabla -----
        configurarTabla(tabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // ----- FOOTER: total a la derecha -----
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        right.add(lblTotal);
        JPanel footer = new JPanel(new BorderLayout());
        footer.add(right, BorderLayout.EAST);
        add(footer, BorderLayout.SOUTH);

        // Acciones rápidas fechas
        btnSemana.addActionListener(e -> {
            LocalDate hoy = LocalDate.now();
            LocalDate lunes = hoy.with(java.time.DayOfWeek.MONDAY);
            spDesde.setValue(Date.from(lunes.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            spHasta.setValue(Date.from(hoy.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        });
        btnMes.addActionListener(e -> {
            LocalDate hoy = LocalDate.now();
            LocalDate primero = hoy.withDayOfMonth(1);
            spDesde.setValue(Date.from(primero.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            spHasta.setValue(Date.from(hoy.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        });

        // Abrir modal de nueva venta
        btnNueva.addActionListener(e -> { if (onNuevaVenta != null) onNuevaVenta.run(); });
    }

    private void configurarTabla(JTable t) {
        t.setRowSelectionAllowed(true);
        t.setColumnSelectionAllowed(false);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setSelectionBackground(new Color(0,120,215));
        t.setSelectionForeground(Color.WHITE);
        t.setShowGrid(false);
        t.setFillsViewportHeight(true);
        t.setAutoCreateRowSorter(true);
    }

    private void estilizarBotonNueva(JButton b) {
        b.setFocusPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT); // icono a la izquierda
        b.setPreferredSize(new Dimension(180, 36));     // ancho generoso tipo “primario”
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 210, 235)),
                BorderFactory.createEmptyBorder(6,12,6,12)
        ));
        b.setBackground(new Color(246, 240, 252));
        b.setForeground(new Color(90, 60, 150));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
    }

    private Icon cargarIcono(String name, int w, int h) {
        try {
            URL url = getClass().getResource("/icons/" + name);
            if (url == null) return null;
            Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception ex) {
            return null;
        }
    }

    private static LocalDate toLocalDate(Object spinnerValue) {
        if (!(spinnerValue instanceof Date)) return LocalDate.now();
        Instant inst = ((Date) spinnerValue).toInstant();
        return inst.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // ----- Getters / callback -----
    public LocalDate getDesde() { return toLocalDate(spDesde.getValue()); }
    public LocalDate getHasta() { return toLocalDate(spHasta.getValue()); }
    public JButton getBtnFiltrar() { return btnFiltrar; }
    public JTable getTabla() { return tabla; }
    public JLabel getLblTotal() { return lblTotal; }
    public void setOnNuevaVenta(Runnable r) { this.onNuevaVenta = r; }
}
