package servicio;

import modelo.Item;
import persistencia.InventarioDao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ServicioInventario {

    private final InventarioDao dao = new InventarioDao();

    public List<Item> cargarInventarioCompleto() throws Exception {
        return dao.listarTodos();
    }

    public Item porCodigo(String codigo) throws Exception {
        return dao.buscarPorCodigo(codigo);
    }

    /**
     * Exporta el inventario actual a un archivo CSV.
     * Columnas: id,code,name,price,stock
     */
    public void exportarInventarioCsv(String filePath) throws Exception {
        List<Item> items = cargarInventarioCompleto();

        try (PrintWriter out = new PrintWriter(new FileWriter(filePath))) {
            out.println("id,code,name,price,stock");
            for (Item it : items) {
                out.printf(
                        "%d,%s,%s,%s,%d%n",
                        it.getId(),
                        safe(it.getCodigo()),
                        safe(it.getNombre()),
                        it.getPrecio() != null ? it.getPrecio().toPlainString() : "0",
                        it.getStock()
                );
            }
        } catch (IOException e) {
            // Reenvolvemos como Exception genérica para no cambiar firmas en los controladores
            throw new Exception("Error al escribir el archivo CSV: " + e.getMessage(), e);
        }
    }

    private String safe(String value) {
        return value == null ? "" : value.replace(",", " ");
    }
}
