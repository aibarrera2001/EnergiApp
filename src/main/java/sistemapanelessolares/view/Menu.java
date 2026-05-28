/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.sistemapanelessolares.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
/**
 *
 * @author jose
 */
public class Menu extends JFrame{
    
     public Menu() {

      
        setTitle("EnergiApp - Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        
        Color crema = new Color(252, 248, 240);
        Color cremaClaro = new Color(255, 253, 248);
        Color azulPrincipal = new Color(35, 70, 180);
        Color azulBoton = new Color(45, 85, 255);
        Color grisTexto = new Color(120, 120, 120);

        
        JPanel sidebar = new JPanel();
        sidebar.setBackground(crema);
        sidebar.setBorder(BorderFactory.createMatteBorder(
        0,
        0,
        0,
        1,
        new Color(220,220,220)
));
        sidebar.setPreferredSize(new Dimension(240, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(25, 15, 25, 15));

        
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
        // MENÚ LATERAL
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

            // Resaltar Dashboard
            if (item.equals("Dashboard")) {
                btn.setBackground(new Color(230, 235, 255));
            }

            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        sidebar.add(Box.createVerticalGlue());

        // =========================
        // INFORMACIÓN USUARIO
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
        header.setBorder(new EmptyBorder(20, 20, 10, 20));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel welcome = new JLabel("Bienvenido, Usuario");
        welcome.setFont(new Font("SansSerif", Font.BOLD, 30));
        welcome.setForeground(azulPrincipal);

        JLabel subtitle = new JLabel("Resumen de tus propiedades y paneles solares");
        subtitle.setForeground(grisTexto);

        titlePanel.add(welcome);
        titlePanel.add(subtitle);

        // =========================
        // BOTÓN NUEVA PROPIEDAD
        // =========================
        JButton nuevaPropiedad = new JButton("Nueva propiedad");

        nuevaPropiedad.setBackground(azulBoton);
        nuevaPropiedad.setForeground(Color.WHITE);

        nuevaPropiedad.setFocusPainted(false);
        nuevaPropiedad.setBorderPainted(false);

        nuevaPropiedad.setFont(new Font("SansSerif", Font.BOLD, 14));
        nuevaPropiedad.setCursor(new Cursor(Cursor.HAND_CURSOR));

        header.add(titlePanel, BorderLayout.WEST);
        header.add(nuevaPropiedad, BorderLayout.EAST);

        mainPanel.add(header, BorderLayout.NORTH);

        // =========================
        // PANEL CENTRAL
        // =========================
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        centerPanel.setLayout(new BorderLayout(20, 20));

        // =========================
        // TARJETAS ESTADÍSTICAS
        // =========================
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        statsPanel.setOpaque(false);

        statsPanel.add(createCard(
                "Propiedades",
                "3",
                "↑ 1 nuevo este mes"
        ));

        statsPanel.add(createCard(
                "Paneles estimados",
                "18",
                "Para cubrir el consumo"
        ));

        statsPanel.add(createCard(
                "Consumo total",
                "1.240",
                "kWh/mes acumulado"
        ));

        statsPanel.add(createCard(
                "Ahorro estimado",
                "$1.8M",
                "COP / año proyectado"
        ));

        // =========================
        // CONTENIDO INFERIOR
        // =========================
        JPanel contentSection = new JPanel(new GridLayout(1, 2, 20, 20));
        contentSection.setOpaque(false);

        // =========================
        // PANEL PROPIEDADES
        // =========================
        JPanel propertiesPanel = createContainerPanel("Mis propiedades");

        propertiesPanel.add(createPropertyItem(
                "Casa principal",
                "Barranquilla - 420 kWh/mes",
                "Calculado"
        ));

        propertiesPanel.add(createPropertyItem(
                "Apartamento norte",
                "Cartagena - 310 kWh/mes",
                "Calculado"
        ));

        propertiesPanel.add(createPropertyItem(
                "Casa campestre",
                "Valledupar - 510 kWh/mes",
                "Pendiente"
        ));

        // =========================
        // PANEL ACCIONES
        // =========================
        JPanel actionsPanel = createContainerPanel("Acciones rápidas");

        String[] actions = {
                "Registrar nueva propiedad",
                "Ver catálogo de paneles",
                "Generar reporte",
                "Consultar radiación solar"
        };

        for (String action : actions) {

            JButton btn = new JButton(action);

            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

            btn.setFocusPainted(false);
            btn.setBorderPainted(false);

            btn.setBackground(Color.WHITE);

            btn.setForeground(azulPrincipal);

            btn.setFont(new Font("SansSerif", Font.BOLD, 14));

            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btn.setAlignmentX(Component.CENTER_ALIGNMENT);

            actionsPanel.add(btn);
            actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        contentSection.add(propertiesPanel);
        contentSection.add(actionsPanel);

        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BorderLayout(0, 20));

        wrapper.add(statsPanel, BorderLayout.NORTH);
        wrapper.add(contentSection, BorderLayout.CENTER);

        centerPanel.add(wrapper, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    // =========================
    // BOTONES SIDEBAR
    // =========================
    private JButton createSidebarButton(String text) {

         Color azulClaro = new Color(230, 235, 255);
    Color azulHover = new Color(45, 85, 255);
    Color azulTexto = new Color(35, 70, 180);

    JButton button = new JButton(text);

    button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

    button.setAlignmentX(Component.LEFT_ALIGNMENT);

    // Fondo inicial
    button.setBackground(azulClaro);

    // Texto
    button.setForeground(azulTexto);

    // Fuente
    button.setFont(new Font("SansSerif", Font.BOLD, 15));

    // Bordes
    button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

    button.setFocusPainted(false);

    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Quitar borde feo de Swing
    button.setBorderPainted(false);

    // Hover effect
    button.addMouseListener(new java.awt.event.MouseAdapter() {

        @Override
        public void mouseEntered(java.awt.event.MouseEvent evt) {

            button.setBackground(azulHover);
            button.setForeground(Color.WHITE);
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent evt) {

            // Mantener Dashboard seleccionado
            if (text.equals("Dashboard")) {

                button.setBackground(new Color(45, 85, 255));
                button.setForeground(Color.WHITE);

            } else {

                button.setBackground(azulClaro);
                button.setForeground(azulTexto);
            }
        }
    });

    // Dashboard activo por defecto
    if (text.equals("Dashboard")) {

        button.setBackground(new Color(45, 85, 255));
        button.setForeground(Color.WHITE);
    }

    return button;
    }

    // =========================
    // TARJETAS
    // =========================
    private JPanel createCard(String title, String value, String desc) {

        JPanel card = new JPanel();

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        card.setBackground(new Color(255, 255, 252));

        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.GRAY);

        JLabel lblValue = new JLabel(value);

        lblValue.setFont(new Font("SansSerif", Font.BOLD, 30));

        lblValue.setForeground(new Color(35, 70, 180));

        JLabel lblDesc = new JLabel(desc);

        lblDesc.setForeground(new Color(90, 140, 90));

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(lblValue);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(lblDesc);

        return card;
    }

    // =========================
    // CONTENEDORES
    // =========================
    private JPanel createContainerPanel(String title) {

        JPanel panel = new JPanel();

        panel.setBackground(Color.WHITE);

        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel(title);

        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));

        lblTitle.setForeground(new Color(35, 70, 180));

        panel.add(lblTitle);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        return panel;
    }

    // =========================
    // ITEMS PROPIEDADES
    // =========================
    private JPanel createPropertyItem(String name, String details, String status) {

        JPanel item = new JPanel(new BorderLayout());

        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        item.setOpaque(false);

        JPanel info = new JPanel();

        info.setOpaque(false);

        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel lblName = new JLabel(name);

        lblName.setFont(new Font("SansSerif", Font.BOLD, 15));

        lblName.setForeground(new Color(35, 70, 180));

        JLabel lblDetails = new JLabel(details);

        lblDetails.setForeground(Color.GRAY);

        info.add(lblName);
        info.add(lblDetails);

        JLabel lblStatus = new JLabel(status);

        lblStatus.setOpaque(true);

        lblStatus.setBorder(new EmptyBorder(5, 10, 5, 10));

        if (status.equals("Calculado")) {

            lblStatus.setBackground(new Color(220, 245, 220));

        } else {

            lblStatus.setBackground(new Color(255, 235, 190));
        }

        item.add(info, BorderLayout.WEST);

        item.add(lblStatus, BorderLayout.EAST);

        return item;
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
            new Menu().setVisible(true);
        });
    }
}
