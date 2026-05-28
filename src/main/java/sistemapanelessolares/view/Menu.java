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

        
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(18, 48, 15));
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel logo = new JLabel("EnergiApp");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 24));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

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
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        add(sidebar, BorderLayout.WEST);

        
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setLayout(new BorderLayout());

        
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(20, 20, 10, 20));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel welcome = new JLabel("Bienvenido, Usuario");
        welcome.setFont(new Font("SansSerif", Font.BOLD, 28));

        JLabel subtitle = new JLabel("Resumen de tus propiedades y paneles solares");
        subtitle.setForeground(Color.GRAY);

        titlePanel.add(welcome);
        titlePanel.add(subtitle);

        JButton nuevaPropiedad = new JButton("Nueva propiedad");
        nuevaPropiedad.setBackground(new Color(78, 130, 35));
        nuevaPropiedad.setForeground(Color.WHITE);
        nuevaPropiedad.setFocusPainted(false);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(nuevaPropiedad, BorderLayout.EAST);

        mainPanel.add(header, BorderLayout.NORTH);

        
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        centerPanel.setLayout(new BorderLayout(20, 20));

        
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        statsPanel.setOpaque(false);

        statsPanel.add(createCard("Propiedades", "3", "↑ 1 nuevo este mes"));
        statsPanel.add(createCard("Paneles estimados", "18", "Para cubrir el consumo"));
        statsPanel.add(createCard("Consumo total", "1.240", "kWh/mes acumulado"));
        statsPanel.add(createCard("Ahorro estimado", "$1.8M", "COP / año proyectado"));

       
        JPanel contentSection = new JPanel(new GridLayout(1, 2, 20, 20));
        contentSection.setOpaque(false);

        
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
            btn.setBackground(Color.WHITE);
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

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.setBackground(new Color(30, 70, 25));
        button.setForeground(Color.WHITE);

        button.setFocusPainted(false);
        button.setBorderPainted(false);

        return button;
    }

    private JPanel createCard(String title, String value, String desc) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.GRAY);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 28));

        JLabel lblDesc = new JLabel(desc);
        lblDesc.setForeground(new Color(80, 140, 50));

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(lblValue);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(lblDesc);

        return card;
    }

    private JPanel createContainerPanel(String title) {

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));

        panel.add(lblTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        return panel;
    }

    private JPanel createPropertyItem(String name, String details, String status) {

        JPanel item = new JPanel(new BorderLayout());
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        item.setOpaque(false);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel lblName = new JLabel(name);
        lblName.setFont(new Font("SansSerif", Font.BOLD, 15));

        JLabel lblDetails = new JLabel(details);
        lblDetails.setForeground(Color.GRAY);

        info.add(lblName);
        info.add(lblDetails);

        JLabel lblStatus = new JLabel(status);
        lblStatus.setOpaque(true);
        lblStatus.setBorder(new EmptyBorder(5, 10, 5, 10));

        if (status.equals("Calculado")) {
            lblStatus.setBackground(new Color(210, 240, 200));
        } else {
            lblStatus.setBackground(new Color(255, 230, 180));
        }

        item.add(info, BorderLayout.WEST);
        item.add(lblStatus, BorderLayout.EAST);

        return item;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new Menu().setVisible(true);
        });
    }
}
