package vista;

import controlador.ControladorVentas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelVenta extends JPanel {
    private final JTextField txtFecha = new JTextField(10);
    private final JComboBox<String> cmbPago = new JComboBox<>();
    private final JTextField txtCodigo = new JTextField(12);
    private final JTextField txtCantidad = new JTextField(5);
    private final JButton btnAgregar = new JButton("Agregar");
    private final JTable tabla = new JTable(new DefaultTableModel(new Object[]{"Código","Nombre","Cant.","Precio","Subtotal"}, 0));
    private final JButton btnConfirmar = new JButton("Confirmar venta");
    private final JLabel lblTotal = new JLabel("Total: 0");

    public PanelVenta() {
        setLayout(new BorderLayout(8,8));

        JPanel datos = new JPanel();
        datos.add(new JLabel("Fecha:"));
        datos.add(txtFecha);
        datos.add(new JLabel("Pago:"));
        datos.add(cmbPago);

        JPanel ingreso = new JPanel();
        ingreso.add(new JLabel("Código:"));
        ingreso.add(txtCodigo);
        ingreso.add(new JLabel("Cantidad:"));
        ingreso.add(txtCantidad);
        ingreso.add(btnAgregar);

        add(datos, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel pie = new JPanel(new BorderLayout());
        pie.add(lblTotal, BorderLayout.WEST);
        pie.add(btnConfirmar, BorderLayout.EAST);
        add(pie, BorderLayout.SOUTH);

        new ControladorVentas(this);
    }

    public JTextField getTxtFecha(){ return txtFecha; }
    public JComboBox<String> getCmbPago(){ return cmbPago; }
    public JTextField getTxtCodigo(){ return txtCodigo; }
    public JTextField getTxtCantidad(){ return txtCantidad; }
    public JButton getBtnAgregar(){ return btnAgregar; }
    public JTable getTabla(){ return tabla; }
    public JButton getBtnConfirmar(){ return btnConfirmar; }
    public JLabel getLblTotal(){ return lblTotal; }
}
