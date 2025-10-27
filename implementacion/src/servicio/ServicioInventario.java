package servicio;

import modelo.Item;
import persistencia.InventarioDao;
import java.util.List;

public class ServicioInventario {
    private final InventarioDao dao = new InventarioDao();
    public List<Item> cargarInventarioCompleto() throws Exception {
        return dao.listarTodos();
    }
    public Item porCodigo(String codigo) throws Exception {
        return dao.buscarPorCodigo(codigo);
    }
}
