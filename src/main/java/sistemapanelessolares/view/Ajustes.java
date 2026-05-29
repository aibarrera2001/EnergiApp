package main.java.sistemapanelessolares.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.Cursor;

public class Ajustes extends JFrame {

    // =========================
    // COLORES DEL SISTEMA
    // =========================
    private final Color crema = new Color(252, 248, 240);
    private final Color cremaClaro = new Color(255, 253, 248);
    private final Color azulPrincipal = new Color(35, 70, 180);
    private final Color azulBoton = new Color(45, 85, 255);
    private final Color azulClaro = new Color(230, 235, 255);
    private final Color grisTexto = new Color(120, 120, 120);

    public Ajustes() {

        // =========================
        // CONFIGURACIÓN VENTANA
        // =========================
        setTitle("EnergiApp - Ajustes");
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

            if (item.equals("Ajustes")) {

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

        JLabel title = new JLabel("Configuración y Ajustes");

        title.setFont(new Font("SansSerif", Font.BOLD, 34));

        title.setForeground(azulPrincipal);

        JLabel subtitle = new JLabel(
                "Administra las preferencias y configuraciones de tu cuenta"
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

        centerPanel.setLayout(new GridLayout(2,2,25,25));

        // =========================
        // PANEL NOTIFICACIONES
        // =========================
        centerPanel.add(createSettingsCard(
                "🔔 Notificaciones",
                new String[]{
                        "Recibir alertas de consumo",
                        "Recibir reportes semanales",
                        "Notificaciones por ahorro",
                        "Alertas del sistema"
                }
        ));

        // =========================
        // PANEL PRIVACIDAD
        // =========================
        centerPanel.add(createSettingsCard(
                "🔒 Privacidad y Seguridad",
                new String[]{
                        "Autenticación en dos pasos",
                        "Cambiar contraseña",
                        "Ocultar información personal",
                        "Bloquear acceso externo"
                }
        ));

        // =========================
        // PANEL APARIENCIA
        // =========================
        centerPanel.add(createSettingsCard(
                "🎨 Apariencia",
                new String[]{
                        "Modo oscuro",
                        "Animaciones de interfaz",
                        "Tema azul EnergiApp",
                        "Fuente grande"
                }
        ));

        // =========================
        // PANEL SISTEMA
        // =========================
        centerPanel.add(createSystemCard());

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    // =========================
    // TARJETA CONFIGURACIÓN
    // =========================
    private JPanel createSettingsCard(
            String title,
            String[] options
    ) {

        JPanel card = new JPanel();

        card.setBackground(Color.WHITE);

        card.setBorder(new EmptyBorder(25,25,25,25));

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel(title);

        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));

        lblTitle.setForeground(azulPrincipal);

        card.add(lblTitle);

        card.add(Box.createRigidArea(new Dimension(0,20)));

        for (String option : options) {

            JCheckBox checkBox = new JCheckBox(option);

            checkBox.setBackground(Color.WHITE);

            checkBox.setForeground(grisTexto);

            checkBox.setFont(new Font("SansSerif", Font.PLAIN, 15));

            card.add(checkBox);

            card.add(Box.createRigidArea(new Dimension(0,12)));
        }

        JButton saveButton = new JButton("Guardar Ajustes");

        saveButton.setBackground(azulBoton);

        saveButton.setForeground(Color.WHITE);

        saveButton.setFocusPainted(false);

        saveButton.setBorderPainted(false);

        saveButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(Box.createVerticalGlue());

        card.add(Box.createRigidArea(new Dimension(0,20)));

        card.add(saveButton);

        return card;
    }

    // =========================
    // PANEL SISTEMA
    // =========================
    private JPanel createSystemCard() {

        JPanel card = new JPanel();

        card.setBackground(Color.WHITE);

        card.setBorder(new EmptyBorder(25,25,25,25));

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("⚙ Configuración del Sistema");

        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));

        lblTitle.setForeground(azulPrincipal);

        JLabel version = new JLabel("Versión de EnergiApp: 2.1.0");

        version.setForeground(grisTexto);

        version.setFont(new Font("SansSerif", Font.PLAIN, 15));

        JLabel storage = new JLabel("Almacenamiento usado: 1.2 GB");

        storage.setForeground(grisTexto);

        storage.setFont(new Font("SansSerif", Font.PLAIN, 15));

        JLabel lastUpdate = new JLabel("Última actualización: 20 Julio 2026");

        lastUpdate.setForeground(grisTexto);

        lastUpdate.setFont(new Font("SansSerif", Font.PLAIN, 15));

        JButton backupButton = new JButton("Crear Copia de Seguridad");

        backupButton.setBackground(azulBoton);

        backupButton.setForeground(Color.WHITE);

        backupButton.setFocusPainted(false);

        backupButton.setBorderPainted(false);

        backupButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        JButton logoutButton = new JButton("Cerrar Sesión");

        logoutButton.setBackground(new Color(220, 70, 70));

        logoutButton.setForeground(Color.WHITE);

        logoutButton.setFocusPainted(false);

        logoutButton.setBorderPainted(false);

        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        card.add(lblTitle);

        card.add(Box.createRigidArea(new Dimension(0,20)));

        card.add(version);

        card.add(Box.createRigidArea(new Dimension(0,12)));

        card.add(storage);

        card.add(Box.createRigidArea(new Dimension(0,12)));

        card.add(lastUpdate);

        card.add(Box.createVerticalGlue());

        card.add(Box.createRigidArea(new Dimension(0,25)));

        card.add(backupButton);

        card.add(Box.createRigidArea(new Dimension(0,15)));

        card.add(logoutButton);

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

            new Ajustes().setVisible(true);

        });
    }
}
