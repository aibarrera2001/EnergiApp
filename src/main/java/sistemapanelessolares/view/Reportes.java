package main.java.sistemapanelessolares.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.Cursor;

public class Reportes extends JFrame {

    // =========================
    // COLORES DEL SISTEMA
    // =========================
    private final Color crema = new Color(252, 248, 240);
    private final Color cremaClaro = new Color(255, 253, 248);
    private final Color azulPrincipal = new Color(35, 70, 180);
    private final Color azulBoton = new Color(45, 85, 255);
    private final Color azulClaro = new Color(230, 235, 255);
    private final Color grisTexto = new Color(120, 120, 120);

    public Reportes() {

        // =========================
        // CONFIGURACIÓN VENTANA
        // =========================
        setTitle("EnergiApp - Reportes");
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
        // MENÚ
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

            if (item.equals("Reportes")) {

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
        // MAIN PANEL
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

        JLabel title = new JLabel("Reportes Energéticos");

        title.setFont(new Font("SansSerif", Font.BOLD, 34));

        title.setForeground(azulPrincipal);

        JLabel subtitle = new JLabel(
                "Visualiza el ahorro y consumo energético de los últimos días"
        );

        subtitle.setForeground(grisTexto);

        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));

        titlePanel.add(title);

        titlePanel.add(Box.createRigidArea(new Dimension(0, 10)));

        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.WEST);

        mainPanel.add(header, BorderLayout.NORTH);

        // =========================
        // PANEL CENTRAL
        // =========================
        JPanel centerPanel = new JPanel();

        centerPanel.setOpaque(false);

        centerPanel.setBorder(new EmptyBorder(20, 30, 30, 30));

        centerPanel.setLayout(new BorderLayout(20,20));

        // =========================
        // TARJETAS RESUMEN
        // =========================
        JPanel resumenPanel = new JPanel(new GridLayout(1,3,20,20));

        resumenPanel.setOpaque(false);

        resumenPanel.add(createResumenCard(
                "Ahorro Total",
                "$3.250.000",
                "Últimos 30 días"
        ));

        resumenPanel.add(createResumenCard(
                "Consumo Total",
                "1.840 kWh",
                "Energía consumida"
        ));

        resumenPanel.add(createResumenCard(
                "Panel Más Eficiente",
                "SunPower Max 450W",
                "92% eficiencia"
        ));

        // =========================
        // REPORTES
        // =========================
        JPanel reportesPanel = new JPanel();

        reportesPanel.setLayout(new GridLayout(5,1,15,15));

        reportesPanel.setOpaque(false);

        reportesPanel.add(createReporteItem(
                "01 Julio 2026",
                "320 kWh",
                "$450.000 ahorrados"
        ));

        reportesPanel.add(createReporteItem(
                "05 Julio 2026",
                "280 kWh",
                "$390.000 ahorrados"
        ));

        reportesPanel.add(createReporteItem(
                "10 Julio 2026",
                "350 kWh",
                "$520.000 ahorrados"
        ));

        reportesPanel.add(createReporteItem(
                "15 Julio 2026",
                "300 kWh",
                "$430.000 ahorrados"
        ));

        reportesPanel.add(createReporteItem(
                "20 Julio 2026",
                "270 kWh",
                "$370.000 ahorrados"
        ));

        JPanel wrapper = new JPanel();

        wrapper.setOpaque(false);

        wrapper.setLayout(new BorderLayout(0,25));

        wrapper.add(resumenPanel, BorderLayout.NORTH);

        wrapper.add(reportesPanel, BorderLayout.CENTER);

        centerPanel.add(wrapper, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    // =========================
    // TARJETAS RESUMEN
    // =========================
    private JPanel createResumenCard(
            String titulo,
            String valor,
            String descripcion
    ) {

        JPanel card = new JPanel();

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        card.setBackground(Color.WHITE);

        card.setBorder(new EmptyBorder(20,20,20,20));

        JLabel lblTitulo = new JLabel(titulo);

        lblTitulo.setForeground(grisTexto);

        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel lblValor = new JLabel(valor);

        lblValor.setForeground(azulPrincipal);

        lblValor.setFont(new Font("SansSerif", Font.BOLD, 28));

        JLabel lblDescripcion = new JLabel(descripcion);

        lblDescripcion.setForeground(new Color(60,160,90));

        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));

        card.add(lblTitulo);

        card.add(Box.createRigidArea(new Dimension(0,10)));

        card.add(lblValor);

        card.add(Box.createRigidArea(new Dimension(0,10)));

        card.add(lblDescripcion);

        return card;
    }

    // =========================
    // ITEMS REPORTE
    // =========================
    private JPanel createReporteItem(
            String fecha,
            String consumo,
            String ahorro
    ) {

        JPanel item = new JPanel(new BorderLayout());

        item.setBackground(Color.WHITE);

        item.setBorder(new EmptyBorder(20,20,20,20));

        JPanel infoPanel = new JPanel();

        infoPanel.setOpaque(false);

        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel lblFecha = new JLabel(fecha);

        lblFecha.setFont(new Font("SansSerif", Font.BOLD, 20));

        lblFecha.setForeground(azulPrincipal);

        JLabel lblConsumo = new JLabel(
                "Consumo energético: " + consumo
        );

        lblConsumo.setForeground(grisTexto);

        lblConsumo.setFont(new Font("SansSerif", Font.PLAIN, 15));

        infoPanel.add(lblFecha);

        infoPanel.add(Box.createRigidArea(new Dimension(0,8)));

        infoPanel.add(lblConsumo);

        JLabel ahorroLabel = new JLabel(ahorro);

        ahorroLabel.setOpaque(true);

        ahorroLabel.setBackground(new Color(220,245,220));

        ahorroLabel.setForeground(new Color(40,160,90));

        ahorroLabel.setBorder(new EmptyBorder(10,15,10,15));

        ahorroLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

        item.add(infoPanel, BorderLayout.WEST);

        item.add(ahorroLabel, BorderLayout.EAST);

        return item;
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

            new Reportes().setVisible(true);

        });
    }
}