package persistencia;

import controlador.ControladorVentasLista;
import modelo.LineaVenta;
import modelo.SesionUsuario;
import modelo.Venta;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentaDao {

    public void registrarVenta(Venta venta) throws Exception {
        String sqlSale = "INSERT INTO sale (sale_code, sale_date, sale_total, payment_method_id, created_by_user_id) VALUES (?, ?, ?, ?, ?)";
        String sqlDetail = "INSERT INTO sale_detail (sale_id, item_id, sale_quantity, sale_unit_price, sale_line_total) VALUES (?, ?, ?, ?, ?)";
        String sqlStock = "UPDATE item SET item_stock_qty = item_stock_qty - ? WHERE item_id = ? AND item_stock_qty >= ?";

        try (Connection cn = ConexionBD.obtener()) {
            cn.setAutoCommit(false);
            try (PreparedStatement psSale = cn.prepareStatement(sqlSale, Statement.RETURN_GENERATED_KEYS)) {
                psSale.setString(1, venta.getCodigo());
                psSale.setDate(2, Date.valueOf(venta.getFecha()));
                psSale.setBigDecimal(3, venta.getTotal());
                psSale.setLong(4, resolverPaymentMethodId(cn, venta.getMetodoPago()));
                psSale.setLong(5, SesionUsuario.get().getId());
                psSale.executeUpdate();

                long saleId;
                try (ResultSet gk = psSale.getGeneratedKeys()) {
                    gk.next();
                    saleId = gk.getLong(1);
                }

                try (PreparedStatement psDet = cn.prepareStatement(sqlDetail);
                     PreparedStatement psStk = cn.prepareStatement(sqlStock)) {
                    for (LineaVenta l : venta.getLineas()) {
                        psStk.setInt(1, l.getCantidad());
                        psStk.setLong(2, l.getItem().getId());
                        psStk.setInt(3, l.getCantidad());
                        int upd = psStk.executeUpdate();
                        if (upd == 0) throw new IllegalStateException("Stock insuficiente para " + l.getItem().getCodigo());

                        psDet.setLong(1, saleId);
                        psDet.setLong(2, l.getItem().getId());
                        psDet.setInt(3, l.getCantidad());
                        psDet.setBigDecimal(4, l.getItem().getPrecio());
                        psDet.setBigDecimal(5, l.calcularTotal());
                        psDet.addBatch();
                    }
                    psDet.executeBatch();
                }
                cn.commit();
            } catch (Exception ex) {
                cn.rollback();
                throw ex;
            } finally {
                cn.setAutoCommit(true);
            }
        }
    }

    private long resolverPaymentMethodId(Connection cn, String nombre) throws Exception {
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT payment_method_id FROM payment_method WHERE payment_method_name = ?")) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
                throw new IllegalArgumentException("Método de pago inexistente: " + nombre);
            }
        }
    }

    // ---- Listado de ventas (resumen) entre fechas ----
    public List<ControladorVentasLista.VentaResumen> listarResumen(LocalDate desde, LocalDate hasta) throws Exception {
        String sql = """
            SELECT s.sale_date,
                   s.sale_total,
                   COALESCE(SUM(sd.sale_quantity), 0) AS items
            FROM sale s
            LEFT JOIN sale_detail sd ON sd.sale_id = s.sale_id
            WHERE s.sale_date BETWEEN ? AND ?
            GROUP BY s.sale_id, s.sale_date, s.sale_total
            ORDER BY s.sale_date DESC
        """;
        ArrayList<ControladorVentasLista.VentaResumen> list = new ArrayList<>();
        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(desde));
            ps.setDate(2, Date.valueOf(hasta));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ControladorVentasLista.VentaResumen(
                            rs.getDate("sale_date").toLocalDate(),
                            rs.getBigDecimal("sale_total"),
                            rs.getInt("items")
                    ));
                }
            }
        }
        return list;
    }

    // ---- Métodos de pago (para el combo del Paso 1) ----
    public static List<String> listarMetodosPago() throws Exception {
        String sql = "SELECT payment_method_name FROM payment_method ORDER BY payment_method_name";
        ArrayList<String> list = new ArrayList<>();
        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(rs.getString(1));
        }
        return list;
    }
}
