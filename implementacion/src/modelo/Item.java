package modelo;
import java.math.BigDecimal;

public class Item extends EntidadBase {
    private String codigo;
    private String nombre;
    private BigDecimal precio = BigDecimal.ZERO;
    private int stock;

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio == null ? BigDecimal.ZERO : precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}
