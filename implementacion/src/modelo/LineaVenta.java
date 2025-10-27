package modelo;
import java.math.BigDecimal;

public class LineaVenta {
    private Item item;
    private int cantidad;

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public BigDecimal calcularTotal() {
        if (item == null) return BigDecimal.ZERO;
        return item.getPrecio().multiply(BigDecimal.valueOf(cantidad));
    }
}
