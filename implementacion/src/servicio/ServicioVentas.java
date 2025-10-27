package servicio;

import modelo.LineaVenta;
import modelo.Venta;
import persistencia.VentaDao;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class ServicioVentas {
    private final VentaDao dao = new VentaDao();
    private final AtomicInteger correlativo = new AtomicInteger(1);

    public void confirmar(Venta v) throws Exception {
        if (v.getFecha() == null) v.setFecha(LocalDate.now());
        if (v.getCodigo() == null) v.setCodigo(generarCodigo());
        if (v.getLineas().isEmpty()) throw new IllegalArgumentException("La venta no tiene líneas");
        dao.registrarVenta(v);
    }

    private String generarCodigo() {
        String fecha = LocalDate.now().toString().replace("-", "");
        return fecha + "-" + String.format("%04d", correlativo.getAndIncrement());
    }

    public void agregarLinea(Venta venta, LineaVenta linea) {
        venta.getLineas().add(linea);
    }
}
