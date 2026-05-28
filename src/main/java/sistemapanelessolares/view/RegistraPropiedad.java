package main.java.sistemapanelessolares.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegistraPropiedad extends JFrame {

    // =========================
    // COLORES DEL SISTEMA
    // =========================
    private final Color crema = new Color(252, 248, 240);

    private final Color cremaClaro = new Color(255, 253, 248);

    private final Color azulPrincipal = new Color(35, 70, 180);

    private final Color azulBoton = new Color(45, 85, 255);

    private final Color azulClaro = new Color(230, 235, 255);

    private final Color grisTexto = new Color(120, 120, 120);

    public RegistraPropiedad() {

        // =========================
        // CONFIGURACIÓN VENTANA
        // =========================
        setTitle("EnergiApp - Registrar Propiedad");

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
        JLabel logo = new JLabel("EnergiApp");

        logo.setForeground(azulPrincipal);

        logo.setFont(new Font("SansSerif", Font.BOLD, 28));

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

            sidebar.add(btn);

            sidebar.add(Box.createRigidArea(new Dimension(0, 12)));
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
        JPanel header = new JPanel();

        header.setOpaque(false);

        header.setBorder(new EmptyBorder(30, 35, 10, 35));

        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Registrar Nueva Propiedad");

        title.setFont(new Font("SansSerif", Font.BOLD, 34));

        title.setForeground(azulPrincipal);

        JLabel subtitle = new JLabel(
                "Ingresa los datos de la vivienda y configura el sistema solar"
        );

        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));

        subtitle.setForeground(grisTexto);

        header.add(title);

        header.add(Box.createRigidArea(new Dimension(0, 10)));

        header.add(subtitle);

        mainPanel.add(header, BorderLayout.NORTH);

        // =========================
        // CONTENIDO CENTRAL
        // =========================
        JPanel content = new JPanel();

        content.setOpaque(false);

        content.setBorder(new EmptyBorder(20, 35, 35, 35));

        content.setLayout(new BorderLayout());

        JPanel formContainer = new JPanel();

        formContainer.setBackground(Color.WHITE);

        formContainer.setBorder(new EmptyBorder(30, 30, 30, 30));

        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));

        // =========================
        // TÍTULO FORMULARIO
        // =========================
        JLabel formTitle = new JLabel("Información de la Propiedad");

        formTitle.setFont(new Font("SansSerif", Font.BOLD, 24));

        formTitle.setForeground(azulPrincipal);

        formContainer.add(formTitle);

        formContainer.add(Box.createRigidArea(new Dimension(0, 25)));

        // =========================
        // FILA 1
        // =========================
        JPanel row1 = new JPanel(new GridLayout(1, 2, 20, 0));

        row1.setOpaque(false);

        row1.add(createInput(
                "Nombre de la propiedad",
                "Ej: Casa principal"
        ));

        String[] ciudades = {
                "Barranquilla",
                "Cartagena",
                "Valledupar",
                "Santa Marta",
                "Bogotá",
                "Medellín"
        };

        row1.add(createCombo("Ciudad", ciudades));

        formContainer.add(row1);

        formContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        // =========================
        // DIRECCIÓN
        // =========================
        formContainer.add(createInput(
                "Dirección",
                "Ej: Calle 72 #45-20"
        ));

        formContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        // =========================
        // FILA 2
        // =========================
        JPanel row2 = new JPanel(new GridLayout(1, 2, 20, 0));

        row2.setOpaque(false);

        row2.add(createInput(
                "Consumo mensual (kWh)",
                "Ej: 350"
        ));

        String[] estratos = {
                "1",
                "2",
                "3",
                "4",
                "5",
                "6"
        };

        row2.add(createCombo("Estrato", estratos));

        formContainer.add(row2);

        formContainer.add(Box.createRigidArea(new Dimension(0, 25)));

        // =========================
        // PANEL GEOLOCALIZACIÓN
        // =========================
        JPanel geoPanel = new JPanel();

        geoPanel.setBackground(azulClaro);

        geoPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        geoPanel.setLayout(new BoxLayout(geoPanel, BoxLayout.Y_AXIS));

        JLabel geoTitle = new JLabel(
                "📍 Coordenadas Geográficas"
        );

        geoTitle.setFont(new Font("SansSerif", Font.BOLD, 20));

        geoTitle.setForeground(azulPrincipal);

        JLabel geoDesc = new JLabel(
                "Permite calcular radiación solar real automáticamente"
        );

        geoDesc.setForeground(grisTexto);

        geoPanel.add(geoTitle);

        geoPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        geoPanel.add(geoDesc);

        geoPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel geoRow = new JPanel(new GridLayout(1, 2, 20, 0));

        geoRow.setOpaque(false);

        geoRow.add(createInput(
                "Latitud",
                "Ej: 10.9685"
        ));

        geoRow.add(createInput(
                "Longitud",
                "Ej: -74.7813"
        ));

        geoPanel.add(geoRow);

        formContainer.add(geoPanel);

        formContainer.add(Box.createRigidArea(new Dimension(0, 30)));

        // =========================
        // BOTONES
        // =========================
        JPanel buttons = new JPanel(new GridLayout(1, 2, 20, 0));

        buttons.setOpaque(false);

        JButton cancel = new JButton("Cancelar");

        cancel.setFocusPainted(false);

        cancel.setBackground(Color.WHITE);

        cancel.setForeground(azulPrincipal);

        cancel.setFont(new Font("SansSerif", Font.BOLD, 14));

        cancel.setBorder(BorderFactory.createLineBorder(
                azulPrincipal,
                2
        ));

        JButton save = new JButton(
                "Guardar y Seleccionar Panel →"
        );

        save.setFocusPainted(false);

        save.setBackground(azulBoton);

        save.setForeground(Color.WHITE);

        save.setFont(new Font("SansSerif", Font.BOLD, 14));

        save.setBorderPainted(false);

        buttons.add(cancel);

        buttons.add(save);

        formContainer.add(buttons);

        content.add(formContainer, BorderLayout.CENTER);

        mainPanel.add(content, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    // =========================
    // INPUTS
    // =========================
    private JPanel createInput(String label, String placeholder) {

        JPanel panel = new JPanel();

        panel.setOpaque(false);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);

        lbl.setForeground(azulPrincipal);

        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));

        JTextField field = new JTextField();

        field.setText(placeholder);

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        field.setPreferredSize(new Dimension(200, 45));

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(220,220,220)
                ),
                BorderFactory.createEmptyBorder(10,12,10,12)
        ));

        panel.add(lbl);

        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        panel.add(field);

        return panel;
    }

    // =========================
    // COMBOBOX
    // =========================
    private JPanel createCombo(String label, String[] items) {

        JPanel panel = new JPanel();

        panel.setOpaque(false);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);

        lbl.setForeground(azulPrincipal);

        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));

        JComboBox<String> combo = new JComboBox<>(items);

        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        combo.setBackground(Color.WHITE);

        panel.add(lbl);

        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        panel.add(combo);

        return panel;
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

    // =========================
    // MAIN
    // =========================
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new RegistraPropiedad().setVisible(true);

        });
    }
}