package modelo;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Venta extends EntidadBase {
    private String codigo;
    private LocalDate fecha;
    private String metodoPago;
    private List<LineaVenta> lineas = new ArrayList<>();

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public List<LineaVenta> getLineas() { return lineas; }
    public void setLineas(List<LineaVenta> lineas) { this.lineas = lineas; }

    public BigDecimal getTotal() {
        return lineas.stream()
                .map(LineaVenta::calcularTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
