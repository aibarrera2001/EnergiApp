package main.java.sistemapanelessolares.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Paneles extends JFrame {

    // =========================
    // COLORES DEL SISTEMA
    // =========================
    private final Color crema = new Color(252, 248, 240);
    private final Color cremaClaro = new Color(255, 253, 248);
    private final Color azulPrincipal = new Color(35, 70, 180);
    private final Color azulBoton = new Color(45, 85, 255);
    private final Color azulClaro = new Color(230, 235, 255);
    private final Color grisTexto = new Color(120, 120, 120);

    public Paneles() {

        // =========================
        // CONFIGURACIÓN VENTANA
        // =========================
        setTitle("EnergiApp - Paneles Solares");
        setSize(1400, 800);
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

            if (item.equals("Paneles")) {

                btn.setBackground(azulBoton);
                btn.setForeground(Color.WHITE);
            }

            sidebar.add(btn);

            sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        sidebar.add(Box.createVerticalGlue());

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

        JLabel title = new JLabel("Catálogo de Paneles Solares");

        title.setFont(new Font("SansSerif", Font.BOLD, 34));

        title.setForeground(azulPrincipal);

        JLabel subtitle = new JLabel(
                "Selecciona el panel ideal para maximizar el ahorro energético"
        );

        subtitle.setForeground(grisTexto);

        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));

        titlePanel.add(title);

        titlePanel.add(Box.createRigidArea(new Dimension(0, 10)));

        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.WEST);

        mainPanel.add(header, BorderLayout.NORTH);

        // =========================
        // PANEL DE TARJETAS
        // =========================
        JPanel cardsPanel = new JPanel();

        cardsPanel.setOpaque(false);

        cardsPanel.setBorder(new EmptyBorder(20, 30, 30, 30));

        cardsPanel.setLayout(new GridLayout(2, 3, 25, 25));

        // =========================
        // TARJETAS DE PANELES
        // =========================
        cardsPanel.add(createPanelCard(
                "Panel Solar Eco 300W",
                "$850.000",
                "Ideal para hogares pequeños con bajo consumo energético.",
                "300W"
        ));

        cardsPanel.add(createPanelCard(
                "SunPower Max 450W",
                "$1.200.000",
                "Alta eficiencia energética y excelente rendimiento.",
                "450W"
        ));

        cardsPanel.add(createPanelCard(
                "SolarTech Premium 500W",
                "$1.550.000",
                "Perfecto para viviendas de alto consumo y negocios.",
                "500W"
        ));

        cardsPanel.add(createPanelCard(
                "EcoGreen 350W",
                "$980.000",
                "Panel económico y eficiente para consumo moderado.",
                "350W"
        ));

        cardsPanel.add(createPanelCard(
                "PowerVolt 600W",
                "$2.100.000",
                "Máxima potencia para proyectos industriales.",
                "600W"
        ));

        cardsPanel.add(createPanelCard(
                "BlueEnergy 400W",
                "$1.100.000",
                "Excelente relación calidad-precio y larga duración.",
                "400W"
        ));

        JScrollPane scrollPane = new JScrollPane(cardsPanel);

        scrollPane.setBorder(null);

        scrollPane.getViewport().setBackground(cremaClaro);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
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

                if (!text.equals("Paneles")) {

                    button.setBackground(azulClaro);

                    button.setForeground(azulPrincipal);
                }
            }
        });

        return button;
    }

    // =========================
    // TARJETAS DE PANELES
    // =========================
    private JPanel createPanelCard(
            String nombre,
            String precio,
            String descripcion,
            String potencia
    ) {

        JPanel card = new JPanel();

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        card.setBackground(Color.WHITE);

        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        // ICONO
        JLabel icon = new JLabel("☀");

        icon.setFont(new Font("SansSerif", Font.BOLD, 50));

        icon.setForeground(azulBoton);

        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        // NOMBRE
        JLabel lblNombre = new JLabel(nombre);

        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 22));

        lblNombre.setForeground(azulPrincipal);

        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        // PRECIO
        JLabel lblPrecio = new JLabel(precio);

        lblPrecio.setFont(new Font("SansSerif", Font.BOLD, 28));

        lblPrecio.setForeground(new Color(40, 160, 90));

        lblPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);

        // POTENCIA
        JLabel lblPotencia = new JLabel("Potencia: " + potencia);

        lblPotencia.setForeground(grisTexto);

        lblPotencia.setFont(new Font("SansSerif", Font.BOLD, 15));

        lblPotencia.setAlignmentX(Component.CENTER_ALIGNMENT);

        // DESCRIPCIÓN
        JLabel lblDescripcion = new JLabel(
                "<html><div style='text-align:center;width:250px;'>"
                + descripcion
                + "</div></html>"
        );

        lblDescripcion.setForeground(grisTexto);

        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));

        lblDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);

        // BOTÓN
        JButton comprar = new JButton("Seleccionar Panel");

        comprar.setBackground(azulBoton);

        comprar.setForeground(Color.WHITE);

        comprar.setFocusPainted(false);

        comprar.setBorderPainted(false);

        comprar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        comprar.setFont(new Font("SansSerif", Font.BOLD, 14));

        comprar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // AGREGAR COMPONENTES
        card.add(icon);

        card.add(Box.createRigidArea(new Dimension(0, 15)));

        card.add(lblNombre);

        card.add(Box.createRigidArea(new Dimension(0, 10)));

        card.add(lblPrecio);

        card.add(Box.createRigidArea(new Dimension(0, 10)));

        card.add(lblPotencia);

        card.add(Box.createRigidArea(new Dimension(0, 15)));

        card.add(lblDescripcion);

        card.add(Box.createVerticalGlue());

        card.add(Box.createRigidArea(new Dimension(0, 20)));

        card.add(comprar);

        return card;
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

            new PanelesUI().setVisible(true);

        });
    }
}