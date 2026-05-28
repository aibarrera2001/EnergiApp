package main.java.sistemapanelessolares.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.Cursor;

public class Ingreso extends JFrame {

    // =========================
    // COLORES DEL SISTEMA
    // =========================
    private final Color crema = new Color(252, 248, 240);
    private final Color cremaClaro = new Color(255, 253, 248);
    private final Color azulPrincipal = new Color(35, 70, 180);
    private final Color azulBoton = new Color(45, 85, 255);
    private final Color azulClaro = new Color(230, 235, 255);
    private final Color grisTexto = new Color(120, 120, 120);

    public Ingreso() {

        // =========================
        // CONFIGURACIÓN VENTANA
        // =========================
        setTitle("EnergiApp - Inicio");
        setSize(1200, 700);
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

        // Línea lateral elegante
        sidebar.setBorder(BorderFactory.createMatteBorder(
                0,
                0,
                0,
                1,
                new Color(220,220,220)
        ));

        // =========================
        // LOGO
        // =========================
        ImageIcon logoIcon = new ImageIcon(
                getClass().getResource("/main/java/sistemapanelessolares/resources/logo.jpeg")
        );

        Image scaledImage = logoIcon.getImage().getScaledInstance(
                180,
                80,
                Image.SCALE_SMOOTH
        );

        JLabel logo = new JLabel(new ImageIcon(scaledImage));

        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(logo);

        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        // =========================
        // BOTONES MENU
        // =========================
        String[] menuItems = {
                "Agregar Casa",
                "Seleccionar Panel",
                "Generar Reporte",
                "Salir"
        };

        for (String item : menuItems) {

            JButton btn = createSidebarButton(item);

            sidebar.add(btn);

            sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        sidebar.add(Box.createVerticalGlue());

        // =========================
        // USUARIO
        // =========================
        JLabel user = new JLabel("Juan Pérez");

        user.setForeground(azulPrincipal);

        user.setFont(new Font("SansSerif", Font.BOLD, 14));

        JLabel city = new JLabel("Barranquilla");

        city.setForeground(grisTexto);

        sidebar.add(user);

        sidebar.add(city);

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

        JLabel welcome = new JLabel("Bienvenido a EnergiApp");

        welcome.setFont(new Font("SansSerif", Font.BOLD, 34));

        welcome.setForeground(azulPrincipal);

        JLabel subtitle = new JLabel(
                "Sistema inteligente para gestión de energía solar"
        );

        subtitle.setForeground(grisTexto);

        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));

        titlePanel.add(welcome);

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

        centerPanel.setLayout(new GridLayout(2, 2, 20, 20));

        // =========================
        // TARJETAS
        // =========================
        centerPanel.add(createCard(
                "Agregar una Casa",
                "Registrar una nueva vivienda en el sistema."
        ));

        centerPanel.add(createCard(
                "Seleccionar Panel Solar",
                "Escoge el panel solar ideal para tu hogar."
        ));

        centerPanel.add(createCard(
                "Generar Reporte",
                "Visualiza costos, consumo y ahorro energético."
        ));

        centerPanel.add(createCard(
                "Salir",
                "Cerrar sesión y salir de EnergiApp."
        ));

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }


    // =========================
    // TARJETAS
    // =========================
    private JPanel createCard(String title, String desc) {

        JPanel card = new JPanel();

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        card.setBackground(Color.WHITE);

        card.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel lblTitle = new JLabel(title);

        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));

        lblTitle.setForeground(azulPrincipal);

        JLabel lblDesc = new JLabel(
                "<html><body style='width:250px'>" + desc + "</body></html>"
        );

        lblDesc.setForeground(grisTexto);

        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 15));

        JButton actionButton = new JButton("Ingresar");

        actionButton.setBackground(azulBoton);

        actionButton.setForeground(Color.WHITE);

        actionButton.setFocusPainted(false);

        actionButton.setBorderPainted(false);

        actionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        actionButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        actionButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lblTitle);

        card.add(Box.createRigidArea(new Dimension(0, 15)));

        card.add(lblDesc);

        card.add(Box.createVerticalGlue());

        card.add(Box.createRigidArea(new Dimension(0, 20)));

        card.add(actionButton);

        return card;
    }

    
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

    // =========================
    // HOVER
    // =========================
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

    // =========================
    // NAVEGACIÓN
    // =========================
    button.addActionListener(e -> {

        switch (text) {

            case "Dashboard":

                NavigationManager.openDashboard(this);

                break;

            case "Propiedades":

                NavigationManager.openPropiedades(this);

                break;

            case "Paneles":

                NavigationManager.openPaneles(this);

                break;

            case "Reportes":

                NavigationManager.openReportes(this);

                break;

            case "Mi perfil":

                NavigationManager.openPerfil(this);

                break;

            case "Ajustes":

                NavigationManager.openAjustes(this);

                break;
        }
    });

    return button;
}
    
    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );

        } catch (Exception e) {

            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {

            new Ingreso().setVisible(true);

        });
    }
}