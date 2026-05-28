package main.java.sistemapanelessolares.view;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NavigationManager {

    public static void openDashboard(JFrame currentFrame) {

        currentFrame.dispose();

        new Menu().setVisible(true);
    }

   
    public static void openPropiedades(JFrame currentFrame) {

        currentFrame.dispose();

        new RegistraPropiedad().setVisible(true);
    }

   
    public static void openPaneles(JFrame currentFrame) {

        currentFrame.dispose();

        new Paneles().setVisible(true);
    }

   
    public static void openReportes(JFrame currentFrame) {

        currentFrame.dispose();

        new Reportes().setVisible(true);
    }

  
    public static void openPerfil(JFrame currentFrame) {

        currentFrame.dispose();

        new Perfil().setVisible(true);
    }

   
    public static void openAjustes(JFrame currentFrame) {

        currentFrame.dispose();

        new Ajustes().setVisible(true);
    }
}