package vista;

import servicio.ServicioAuth;
import modelo.Usuario;
import modelo.SesionUsuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class VentanaLogin extends JFrame {

    // ancho “medio” para inputs (mitad aprox. del anterior)
    private static final int FIELD_WIDTH = 260;

    private final JTextField txtUsuario = new JTextField();
    private final JPasswordField txtPassword = new JPasswordField();
    private final JButton btnIngresar = new JButton("Ingresar");
    private final JLabel lblMensaje = new JLabel(" "); // errores/estado

    private final ServicioAuth servicioAuth = new ServicioAuth();

    public VentanaLogin() {
        super("Talina Clothes Inventory");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // tamaño fijo (no redimensionable)
        setSize(900, 560);
        setMinimumSize(new Dimension(900, 560));
        setMaximumSize(new Dimension(900, 560));
        setResizable(false);

        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // --- contenedor central ---
        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setOpaque(true);
        centerWrap.setBackground(Color.WHITE);
        add(centerWrap, BorderLayout.CENTER);

        // Panel de login (columna centrada)
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Logo (opcional). Colocar en resources/icons/talina_logo.png
        JLabel logo = new JLabel();
        Icon ic = loadLogo("/icons/talina_logo.png", 520, 120);
        if (ic != null) logo.setIcon(ic);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(logo);
        card.add(Box.createVerticalStrut(10));

        // Título "Login" centrado
        JLabel titulo = new JLabel("Login", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.PLAIN, 14f));
        titulo.setForeground(new Color(110, 105, 120));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titulo);
        card.add(Box.createVerticalStrut(14));

        // Campo usuario (centrado, angosto)
        card.add(lineField("Usuario", txtUsuario));
        card.add(Box.createVerticalStrut(14));

        // Campo contraseña (centrado, angosto)
        card.add(lineField("Contraseña", txtPassword));
        card.add(Box.createVerticalStrut(22));

        // Botón ingresar (centrado)
        estilizarBotonPrimario(btnIngresar);
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(btnIngresar);
        card.add(Box.createVerticalStrut(10));

        // Mensaje/errores (centrado)
        lblMensaje.setForeground(new Color(160, 0, 0));
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblMensaje);

        // Colocar card centrado
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0;
        centerWrap.add(card, c);

        // Eventos
        btnIngresar.addActionListener(e -> intentarLogin());
        KeyAdapter enter = new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) intentarLogin();
            }
        };
        txtUsuario.addKeyListener(enter);
        txtPassword.addKeyListener(enter);
    }

    // --- helpers de UI ---

    private JPanel lineField(String label, JComponent input) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        // Título centrado
        JLabel l = new JLabel(label, SwingConstants.CENTER);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        l.setFont(l.getFont().deriveFont(Font.PLAIN, 13f));
        l.setForeground(new Color(85, 80, 95));

        // Input “angosto” y centrado
        input.setBorder(new EmptyBorder(6, 4, 6, 4));
        input.setOpaque(false);
        input.setBackground(Color.WHITE);
        input.setFont(input.getFont().deriveFont(Font.PLAIN, 14f));
        input.setMaximumSize(new Dimension(FIELD_WIDTH, 28));
        input.setPreferredSize(new Dimension(FIELD_WIDTH, 28));
        input.setMinimumSize(new Dimension(FIELD_WIDTH, 28));
        input.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Línea inferior (del ancho del input)
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(200, 190, 210));
        JPanel sepWrap = new JPanel();
        sepWrap.setOpaque(false);
        sepWrap.setMaximumSize(new Dimension(FIELD_WIDTH, 1));
        sepWrap.setPreferredSize(new Dimension(FIELD_WIDTH, 1));
        sepWrap.setMinimumSize(new Dimension(FIELD_WIDTH, 1));
        sepWrap.setAlignmentX(Component.CENTER_ALIGNMENT);
        sepWrap.setLayout(new BorderLayout());
        sepWrap.add(sep, BorderLayout.CENTER);

        p.add(l);
        p.add(input);
        p.add(sepWrap);
        p.setMaximumSize(new Dimension(600, 70));
        p.setAlignmentX(Component.CENTER_ALIGNMENT);
        return p;
    }

    private void estilizarBotonPrimario(JButton b) {
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setForeground(new Color(90, 60, 150));
        b.setBackground(new Color(246, 240, 252));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 210, 235)),
                new EmptyBorder(8, 18, 8, 18)
        ));
    }

    private Icon loadLogo(String path, int w, int h) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) return null;
            Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return null;
        }
    }

    // --- lógica de login ---
    private void intentarLogin() {
        String user = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            lblMensaje.setText("Ingrese usuario y contraseña.");
            return;
        }

        try {
            Usuario u = servicioAuth.login(user, pass); // retorna Usuario o null
            if (u != null) {
                try { SesionUsuario.set(u); } catch (Throwable ignored) {}
                SwingUtilities.invokeLater(() -> {
                    new VentanaPrincipal().setVisible(true);
                    dispose();
                });
            } else {
                lblMensaje.setText("Credenciales inválidas.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error de autenticación:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
