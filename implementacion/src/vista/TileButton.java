package vista;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.net.URL;

public class TileButton extends JToggleButton {
    private static final Color BG_NORMAL = new Color(245, 241, 248);
    private static final Color BG_HOVER  = new Color(232, 228, 240);
    private static final Color BG_ACTIVE = new Color(210, 203, 228);
    private static final Color FG_TEXT   = new Color(60, 56, 70);

    public TileButton(String texto, String iconPath) {
        super(texto);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFocusPainted(false);
        setContentAreaFilled(true);
        setOpaque(true);
        setBackground(BG_NORMAL);
        setForeground(FG_TEXT);
        setBorder(new LineBorder(new Color(205, 200, 220), 1, true));
        setFont(getFont().deriveFont(Font.PLAIN, 12f));
        setHorizontalAlignment(SwingConstants.CENTER);

        // icono arriba, texto abajo
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setIconTextGap(6);

        // tamaño cuadrado
        setPreferredSize(new Dimension(96, 96));
        setMinimumSize(new Dimension(96, 96));
        setMaximumSize(new Dimension(Short.MAX_VALUE, 96));

        // carga icono si existe (desde /resources/icons)
        setIcon(cargarIcono(iconPath, 28, 28));

        // hover
        addChangeListener(e -> {
            if (isSelected()) {
                setBackground(BG_ACTIVE);
            } else if (getModel().isRollover()) {
                setBackground(BG_HOVER);
            } else {
                setBackground(BG_NORMAL);
            }
        });
    }

    private Icon cargarIcono(String path, int w, int h) {
        if (path == null) return null;
        URL url = getClass().getResource("/icons/" + path);
        if (url == null) return null; // fallback: sin icono
        Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
