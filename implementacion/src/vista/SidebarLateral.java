package vista;

import javax.swing.*;
import java.awt.*;

public class SidebarLateral extends JPanel {
    private final TileButton btnVentas     = new TileButton("Ventas", "ventas.png");
    private final TileButton btnInventario = new TileButton("Inventario", "inventario.png");
    private final TileButton btnSalir      = new TileButton("Salir", "salir.png");

    public SidebarLateral() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(130, 0));
        setBackground(new Color(245, 241, 248));

        JPanel tiles = new JPanel();
        tiles.setLayout(new GridLayout(0, 1, 8, 8));
        tiles.setOpaque(false);
        tiles.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        tiles.add(btnVentas);
        tiles.add(btnInventario);

        // abajo queda "Salir"
        JPanel abajo = new JPanel(new BorderLayout());
        abajo.setOpaque(false);
        abajo.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));
        abajo.add(btnSalir, BorderLayout.SOUTH);

        add(tiles, BorderLayout.NORTH);
        add(abajo, BorderLayout.SOUTH);

        // grupo de selección (solo uno activo)
        ButtonGroup g = new ButtonGroup();
        g.add(btnVentas);
        g.add(btnInventario);
        g.add(btnSalir);
    }

    public TileButton getBtnVentas()     { return btnVentas; }
    public TileButton getBtnInventario() { return btnInventario; }
    public TileButton getBtnSalir()      { return btnSalir; }
}
