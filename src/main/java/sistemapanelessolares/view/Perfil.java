package main.java.sistemapanelessolares.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.Cursor;

public class Perfil extends JFrame {

    // =========================
    // COLORES DEL SISTEMA
    // =========================
    private final Color crema = new Color(252, 248, 240);
    private final Color cremaClaro = new Color(255, 253, 248);
    private final Color azulPrincipal = new Color(35, 70, 180);
    private final Color azulBoton = new Color(45, 85, 255);
    private final Color azulClaro = new Color(230, 235, 255);
    private final Color grisTexto = new Color(120, 120, 120);

    public Perfil() {

        // =========================
        // CONFIGURACIÓN VENTANA
        // =========================
        setTitle("EnergiApp - Perfil de Usuario");
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

            if (item.equals("Mi perfil")) {

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

        JLabel title = new JLabel("Perfil de Usuario");

        title.setFont(new Font("SansSerif", Font.BOLD, 34));

        title.setForeground(azulPrincipal);

        JLabel subtitle = new JLabel(
                "Administra tu información personal y configuración"
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

        centerPanel.setLayout(new BorderLayout());

        // =========================
        // CARD PERFIL
        // =========================
        JPanel profileCard = new JPanel();

        profileCard.setBackground(Color.WHITE);

        profileCard.setBorder(new EmptyBorder(30, 30, 30, 30));

        profileCard.setLayout(new BoxLayout(profileCard, BoxLayout.Y_AXIS));

        // FOTO PERFIL
        JLabel profileIcon = new JLabel("👤");

        profileIcon.setFont(new Font("SansSerif", Font.PLAIN, 80));

        profileIcon.setForeground(azulBoton);

        profileIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel profileName = new JLabel("Juan Pérez");

        profileName.setFont(new Font("SansSerif", Font.BOLD, 28));

        profileName.setForeground(azulPrincipal);

        profileName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel role = new JLabel("Usuario Premium");

        role.setForeground(grisTexto);

        role.setFont(new Font("SansSerif", Font.PLAIN, 16));

        role.setAlignmentX(Component.CENTER_ALIGNMENT);

        profileCard.add(profileIcon);

        profileCard.add(Box.createRigidArea(new Dimension(0, 15)));

        profileCard.add(profileName);

        profileCard.add(Box.createRigidArea(new Dimension(0, 10)));

        profileCard.add(role);

        profileCard.add(Box.createRigidArea(new Dimension(0, 30)));

        // =========================
        // FORMULARIO
        // =========================
        JPanel formPanel = new JPanel(new GridLayout(6,2,20,20));

        formPanel.setOpaque(false);

        formPanel.add(createField("Nombre"));
        formPanel.add(createField("Apellidos"));

        formPanel.add(createField("Correo Electrónico"));
        formPanel.add(createField("Número de Teléfono"));

        formPanel.add(createField("Ciudad"));
        formPanel.add(createField("Dirección"));

        formPanel.add(createField("Tipo de Vivienda"));
        formPanel.add(createField("Consumo Promedio"));

        formPanel.add(createField("Panel Solar Actual"));
        formPanel.add(createField("Fecha de Registro"));

        formPanel.add(createField("Contraseña"));
        formPanel.add(createField("Confirmar Contraseña"));

        profileCard.add(formPanel);

        profileCard.add(Box.createRigidArea(new Dimension(0, 30)));

        // =========================
        // BOTONES
        // =========================
        JPanel buttonsPanel = new JPanel();

        buttonsPanel.setOpaque(false);

        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton cancelButton = new JButton("Cancelar");

        cancelButton.setBackground(new Color(230,230,230));

        cancelButton.setForeground(Color.BLACK);

        cancelButton.setFocusPainted(false);

        cancelButton.setBorderPainted(false);

        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        JButton saveButton = new JButton("Guardar Cambios");

        saveButton.setBackground(azulBoton);

        saveButton.setForeground(Color.WHITE);

        saveButton.setFocusPainted(false);

        saveButton.setBorderPainted(false);

        saveButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        buttonsPanel.add(cancelButton);

        buttonsPanel.add(saveButton);

        profileCard.add(buttonsPanel);

        centerPanel.add(profileCard, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    // =========================
    // CAMPOS FORMULARIO
    // =========================
    private JPanel createField(String labelText) {

        JPanel panel = new JPanel();

        panel.setOpaque(false);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(labelText);

        label.setForeground(azulPrincipal);

        label.setFont(new Font("SansSerif", Font.BOLD, 14));

        JTextField field = new JTextField();

        field.setPreferredSize(new Dimension(200,40));

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220)),
                BorderFactory.createEmptyBorder(8,10,8,10)
        ));

        panel.add(label);

        panel.add(Box.createRigidArea(new Dimension(0,8)));

        panel.add(field);

        return panel;
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

            new Perfil().setVisible(true);

        });
    }
}
