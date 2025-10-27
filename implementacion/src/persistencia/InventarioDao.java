package persistencia;

import modelo.Item;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class InventarioDao {
    public List<Item> listarTodos() throws Exception {
        String sql =
            "SELECT i.item_id, i.item_code, a.article_name, " +
            "i.item_list_price AS precio, " +
            "i.item_stock_qty " +
            "FROM item i " +
            "JOIN article a ON a.article_id = i.article_id";

        List<Item> items = new ArrayList<>();
        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Item it = new Item();
                it.setId(rs.getLong("item_id"));
                it.setCodigo(rs.getString("item_code"));
                it.setNombre(rs.getString("article_name"));
                BigDecimal p = rs.getBigDecimal("precio");
                it.setPrecio(p == null ? BigDecimal.ZERO : p);
                it.setStock(rs.getInt("item_stock_qty"));
                items.add(it);
            }
        }
        return items;
    }

    public Item buscarPorCodigo(String codigo) throws Exception {
        String sql =
            "SELECT i.item_id, i.item_code, a.article_name, " +
            "i.item_list_price AS precio, " +
            "i.item_stock_qty " +
            "FROM item i " +
            "JOIN article a ON a.article_id = i.article_id " +
            "WHERE i.item_code = ?";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Item it = new Item();
                    it.setId(rs.getLong("item_id"));
                    it.setCodigo(rs.getString("item_code"));
                    it.setNombre(rs.getString("article_name"));
                    it.setPrecio(rs.getBigDecimal("precio"));
                    it.setStock(rs.getInt("item_stock_qty"));
                    return it;
                }
                return null;
            }
        }
    }
}
