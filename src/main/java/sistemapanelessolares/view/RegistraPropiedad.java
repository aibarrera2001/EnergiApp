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
public class RegistraPropiedad extends JFrame{
    
    public RegistraPropiedad() {

        setTitle("EnergiApp - Registrar Propiedad");
        setSize(1200, 750);
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

        sidebar.add(logo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        String[] menuItems = {
                "Dashboard",
                "Propiedades",
                "Paneles",
                "Reportes"
        };

        for (String item : menuItems) {

            JButton btn = new JButton(item);

            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);

            if (item.equals("Propiedades")) {
                btn.setBackground(new Color(60, 100, 30));
            } else {
                btn.setBackground(new Color(18, 48, 15));
            }

            btn.setForeground(Color.WHITE);

            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        sidebar.add(Box.createVerticalGlue());

        JLabel user = new JLabel("Juan Pérez");
        user.setForeground(Color.WHITE);

        JLabel city = new JLabel("Barranquilla");
        city.setForeground(Color.LIGHT_GRAY);

        sidebar.add(user);
        sidebar.add(city);

        add(sidebar, BorderLayout.WEST);

        
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245,245,245));
        mainPanel.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(25, 30, 30, 30));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        
        JLabel back = new JLabel("← Volver al dashboard");
        back.setForeground(Color.DARK_GRAY);

        JLabel title = new JLabel("Registrar nueva propiedad");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));

        JLabel subtitle = new JLabel("Ingresa los datos de la vivienda");
        subtitle.setForeground(Color.GRAY);

        content.add(back);
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        content.add(title);
        content.add(subtitle);
        content.add(Box.createRigidArea(new Dimension(0, 25)));

        
        JPanel formContainer = new JPanel();
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(new EmptyBorder(25,25,25,25));
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));

        JLabel formTitle = new JLabel("Información de la propiedad");
        formTitle.setFont(new Font("SansSerif", Font.BOLD, 22));

        formContainer.add(formTitle);
        formContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        
        JPanel row1 = new JPanel(new GridLayout(1,2,15,0));
        row1.setOpaque(false);

        row1.add(createInput("Nombre de la propiedad", "Ej: Casa principal"));

        String[] ciudades = {
                "Barranquilla",
                "Cartagena",
                "Valledupar",
                "Santa Marta"
        };

        row1.add(createCombo("Ciudad", ciudades));

        formContainer.add(row1);
        formContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        
        formContainer.add(createInput("Dirección", "Calle 72 # 45-20"));
        formContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        
        JPanel row2 = new JPanel(new GridLayout(1,2,15,0));
        row2.setOpaque(false);

        row2.add(createInput("Consumo mensual (kWh)", "Ej: 350"));

        String[] estratos = {"1","2","3","4","5","6"};

        row2.add(createCombo("Estrato", estratos));

        formContainer.add(row2);
        formContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        
        JPanel geoPanel = new JPanel();
        geoPanel.setBackground(new Color(235,245,225));
        geoPanel.setBorder(new EmptyBorder(20,20,20,20));
        geoPanel.setLayout(new BoxLayout(geoPanel, BoxLayout.Y_AXIS));

        JLabel geoTitle = new JLabel("📍 Coordenadas geográficas (opcional)");
        geoTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        geoTitle.setForeground(new Color(70,120,30));

        JLabel geoDesc = new JLabel("Si las ingresas, la app consultará la radiación solar real");
        geoDesc.setForeground(new Color(90,110,70));

        geoPanel.add(geoTitle);
        geoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        geoPanel.add(geoDesc);
        geoPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel geoRow = new JPanel(new GridLayout(1,2,15,0));
        geoRow.setOpaque(false);

        geoRow.add(createInput("Latitud", "Ej: 10.9685"));
        geoRow.add(createInput("Longitud", "Ej: -74.7813"));

        geoPanel.add(geoRow);

        formContainer.add(geoPanel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        
        JPanel buttons = new JPanel(new GridLayout(1,2,15,0));
        buttons.setOpaque(false);

        JButton cancel = new JButton("Cancelar");
        cancel.setFocusPainted(false);
        cancel.setBackground(Color.WHITE);

        JButton save = new JButton("Guardar y seleccionar panel →");
        save.setFocusPainted(false);
        save.setBackground(new Color(70,120,20));
        save.setForeground(Color.WHITE);

        buttons.add(cancel);
        buttons.add(save);

        formContainer.add(buttons);

        content.add(formContainer);

        mainPanel.add(content, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createInput(String label, String placeholder) {

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);

        JTextField field = new JTextField(placeholder);
        field.setPreferredSize(new Dimension(200,40));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(field);

        return panel;
    }

    private JPanel createCombo(String label, String[] items) {

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);

        JComboBox<String> combo = new JComboBox<>(items);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0,8)));
        panel.add(combo);

        return panel;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new RegistraPropiedad().setVisible(true);
        });
    }
}
