package main.java.sistemapanelessolares.view;

import sistemapanelessolares.dominio.Casa;
import sistemapanelessolares.dominio.PanelSolar;
import sistemapanelessolares.dominio.Usuario;
import sistemapanelessolares.validadores.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Registro extends JFrame {

    // =========================
    // COLORES DEL SISTEMA
    // =========================
    private final Color crema = new Color(252, 248, 240);
    private final Color cremaClaro = new Color(255, 253, 248);
    private final Color azulPrincipal = new Color(35, 70, 180);
    private final Color azulBoton = new Color(45, 85, 255);
    private final Color azulClaro = new Color(230, 235, 255);
    private final Color grisTexto = new Color(120, 120, 120);

    // =========================
    // CAMPOS USUARIO
    // =========================
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtCorreo;
    private JPasswordField txtPassword;

    // =========================
    // CAMPOS CASA
    // =========================
    private JTextField txtDireccion;
    private JTextField txtCiudad;
    private JTextField txtConsumo;
    private JTextField txtLatitud;
    private JTextField txtLongitud;

    // =========================
    // PANEL SOLAR
    // =========================
    private JComboBox<String> comboPaneles;

    public Registro() {

        // =========================
        // CONFIGURACIÓN VENTANA
        // =========================
        setTitle("EnergiApp - Registro");
        setSize(1400, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // =========================
        // SIDEBAR
        // =========================
        JPanel sidebar = new JPanel();

        sidebar.setBackground(crema);

        sidebar.setPreferredSize(new Dimension(240, getHeight()));

        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        sidebar.setBorder(new EmptyBorder(25, 15, 25, 15));

        // =========================
        // LOGO
        // =========================
        ImageIcon logoIcon = new ImageIcon("logo.png");

        Image scaledImage = logoIcon.getImage().getScaledInstance(
                180,
                80,
                Image.SCALE_SMOOTH
        );

        JLabel logo = new JLabel(new ImageIcon(scaledImage));

        sidebar.add(logo);

        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        // =========================
        // BOTONES MENU
        // =========================
        String[] menuItems = {
                "Dashboard",
                "Propiedades",
                "Paneles",
                "Reportes",
                "Mi perfil",
                "Ajustes"
        };

        for (String item : menuItems) {

            JButton btn = createSidebarButton(item);

            sidebar.add(btn);

            sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        sidebar.add(Box.createVerticalGlue());

        JLabel user = new JLabel("Nuevo Usuario");

        user.setForeground(azulPrincipal);

        user.setFont(new Font("SansSerif", Font.BOLD, 14));

        sidebar.add(user);

        add(sidebar, BorderLayout.WEST);

        // =========================
        // PANEL PRINCIPAL
        // =========================
        JPanel mainPanel = new JPanel();

        mainPanel.setBackground(cremaClaro);

        mainPanel.setLayout(new BorderLayout());

        // =========================
        // HEADER
        // =========================
        JPanel header = new JPanel(new BorderLayout());

        header.setOpaque(false);

        header.setBorder(new EmptyBorder(30, 30, 10, 30));

        JPanel titlePanel = new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Registro de Usuario");

        title.setFont(new Font("SansSerif", Font.BOLD, 34));

        title.setForeground(azulPrincipal);

        JLabel subtitle = new JLabel(
                "Registra tu cuenta y configura tu sistema solar"
        );

        subtitle.setForeground(grisTexto);

        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));

        titlePanel.add(title);

        titlePanel.add(Box.createRigidArea(new Dimension(0, 10)));

        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.WEST);

        mainPanel.add(header, BorderLayout.NORTH);

        // =========================
        // CONTENIDO CENTRAL
        // =========================
        JPanel centerPanel = new JPanel();

        centerPanel.setOpaque(false);

        centerPanel.setBorder(new EmptyBorder(20, 30, 30, 30));

        centerPanel.setLayout(new GridLayout(1,2,25,25));

        // =========================
        // PANEL USUARIO
        // =========================
        JPanel usuarioPanel = createContainerPanel(
                "Información del Usuario"
        );

        txtNombre = createTextField();
        txtApellido = createTextField();
        txtCorreo = createTextField();
        txtPassword = new JPasswordField();

        usuarioPanel.add(createField("Nombre", txtNombre));
        usuarioPanel.add(createField("Apellido", txtApellido));
        usuarioPanel.add(createField("Correo Electrónico", txtCorreo));
        usuarioPanel.add(createField("Contraseña", txtPassword));

        // =========================
        // PANEL CASA
        // =========================
        JPanel casaPanel = createContainerPanel(
                "Información de la Propiedad"
        );

        txtDireccion = createTextField();
        txtCiudad = createTextField();
        txtConsumo = createTextField();
        txtLatitud = createTextField();
        txtLongitud = createTextField();

        comboPaneles = new JComboBox<>();

        comboPaneles.addItem("Trina Solar 450W - $180");
        comboPaneles.addItem("Jinko Solar 400W - $160");
        comboPaneles.addItem("SunPower 500W - $250");
        comboPaneles.addItem("EcoGreen 350W - $140");

        comboPaneles.setBackground(Color.WHITE);

        casaPanel.add(createField("Dirección", txtDireccion));
        casaPanel.add(createField("Ciudad", txtCiudad));
        casaPanel.add(createField("Consumo Mensual kWh", txtConsumo));
        casaPanel.add(createField("Latitud", txtLatitud));
        casaPanel.add(createField("Longitud", txtLongitud));

        JPanel comboWrapper = new JPanel();

        comboWrapper.setOpaque(false);

        comboWrapper.setLayout(new BoxLayout(
                comboWrapper,
                BoxLayout.Y_AXIS
        ));

        JLabel lblPanel = new JLabel("Seleccionar Panel Solar");

        lblPanel.setForeground(azulPrincipal);

        lblPanel.setFont(new Font("SansSerif", Font.BOLD, 14));

        comboWrapper.add(lblPanel);

        comboWrapper.add(Box.createRigidArea(new Dimension(0, 8)));

        comboWrapper.add(comboPaneles);

        casaPanel.add(comboWrapper);

        centerPanel.add(usuarioPanel);

        centerPanel.add(casaPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // =========================
        // BOTONES
        // =========================
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        footer.setBackground(cremaClaro);

        footer.setBorder(new EmptyBorder(0,30,30,30));

        JButton limpiarBtn = new JButton("Limpiar");

        limpiarBtn.setBackground(new Color(220,220,220));

        limpiarBtn.setFocusPainted(false);

        JButton registrarBtn = new JButton("Registrar Usuario");

        registrarBtn.setBackground(azulBoton);

        registrarBtn.setForeground(Color.WHITE);

        registrarBtn.setFocusPainted(false);

        registrarBtn.setBorderPainted(false);

        registrarBtn.setFont(new Font("SansSerif", Font.BOLD, 14));

        // =========================
        // EVENTO REGISTRO
        // =========================
        registrarBtn.addActionListener(e -> registrarUsuario());

        // =========================
        // EVENTO LIMPIAR
        // =========================
        limpiarBtn.addActionListener(e -> limpiarCampos());

        footer.add(limpiarBtn);

        footer.add(registrarBtn);

        mainPanel.add(footer, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    // =========================
    // REGISTRAR USUARIO
    // =========================
    private void registrarUsuario() {

        try {

            Usuario usuario = new Usuario(
                    1,
                    txtNombre.getText(),
                    txtApellido.getText(),
                    txtCorreo.getText(),
                    new String(txtPassword.getPassword())
            );

            validadorUsuario.validarRegistro(usuario);

            JOptionPane.showMessageDialog(
                    this,
                    "✔ Usuario registrado correctamente",
                    "Registro Exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "❌ Error: " + ex.getMessage(),
                    "Error de Registro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================
    // LIMPIAR CAMPOS
    // =========================
    private void limpiarCampos() {

        txtNombre.setText("");
        txtApellido.setText("");
        txtCorreo.setText("");
        txtPassword.setText("");
        txtDireccion.setText("");
        txtCiudad.setText("");
        txtConsumo.setText("");
        txtLatitud.setText("");
        txtLongitud.setText("");
    }

    // =========================
    // PANEL CONTENEDOR
    // =========================
    private JPanel createContainerPanel(String title) {

        JPanel panel = new JPanel();

        panel.setBackground(Color.WHITE);

        panel.setBorder(new EmptyBorder(25,25,25,25));

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel(title);

        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));

        lblTitle.setForeground(azulPrincipal);

        panel.add(lblTitle);

        panel.add(Box.createRigidArea(new Dimension(0,20)));

        return panel;
    }

    // =========================
    // CAMPOS
    // =========================
    private JPanel createField(
            String labelText,
            JComponent field
    ) {

        JPanel panel = new JPanel();

        panel.setOpaque(false);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(labelText);

        label.setForeground(azulPrincipal);

        label.setFont(new Font("SansSerif", Font.BOLD, 14));

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(220,220,220)
                ),
                BorderFactory.createEmptyBorder(8,10,8,10)
        ));

        panel.add(label);

        panel.add(Box.createRigidArea(new Dimension(0,8)));

        panel.add(field);

        panel.add(Box.createRigidArea(new Dimension(0,18)));

        return panel;
    }

    // =========================
    // TEXTFIELD
    // =========================
    private JTextField createTextField() {

        JTextField field = new JTextField();

        field.setBackground(Color.WHITE);

        field.setForeground(Color.BLACK);

        field.setFont(new Font("SansSerif", Font.PLAIN, 14));

        return field;
    }

    // =========================
    // BOTONES SIDEBAR
    // =========================
    private JButton createSidebarButton(String text) {

        JButton button = new JButton(text);

        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.setBackground(azulClaro);

        button.setForeground(azulPrincipal);

        button.setFont(new Font("SansSerif", Font.BOLD, 15));

        button.setBorder(BorderFactory.createEmptyBorder(
                10,
                20,
                10,
                20
        ));

        button.setFocusPainted(false);

        button.setBorderPainted(false);

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {

                button.setBackground(azulBoton);

                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {

                button.setBackground(azulClaro);

                button.setForeground(azulPrincipal);
            }
        });

        return button;
    }

    // =========================
    // MAIN
    // =========================
    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );

        } catch (Exception e) {

            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {

            new Registro().setVisible(true);

        });
    }
}

