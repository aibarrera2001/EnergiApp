package sistemapanelessolares.view;

import java.sql.Connection;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import sistemapanelessolares.logica.SolarService;

public class IngresoFX extends Application {

    private static Connection conexionDBStatic = null;

    private Connection conexionDB;
    private SolarService solarServicio;

    public IngresoFX() {
        this.conexionDB = conexionDBStatic;
        this.solarServicio = new SolarService(this.conexionDB);
    }

    public static void setConexionDB(Connection con) {
        conexionDBStatic = con;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("EnergiApp - Bienvenido");

        String fondoEstilo = "-fx-background-color: #E1E4E2;";
        String tarjetaIzquierda = "-fx-background-color: #7D8C77; -fx-background-radius: 20; -fx-padding: 40;";
        String tarjetaDerecha = "-fx-background-color: #F3F5F4; -fx-background-radius: 20; -fx-padding: 40;";
        String botonPrincipal = "-fx-background-color: #7D8C77; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-cursor: hand;";
        String botonSecundario = "-fx-background-color: transparent; -fx-text-fill: #E76F51; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-color: #E76F51; -fx-border-radius: 10; -fx-border-width: 1.5; -fx-cursor: hand;";
        String botonSalir = "-fx-background-color: #2B2D2F; -fx-text-fill: white; -fx-font-size: 12px; -fx-background-radius: 8; -fx-cursor: hand;";

        VBox panelIzquierdo = new VBox(20);
        panelIzquierdo.setStyle(tarjetaIzquierda);
        panelIzquierdo.setPrefWidth(350);
        panelIzquierdo.setAlignment(Pos.CENTER_LEFT);

        Label lblLogo = new Label("☀️ EnergiApp");
        lblLogo.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label lblTitulo = new Label("NEW ENERGY\nPREDICTION\nSYSTEM");
        lblTitulo.setStyle("-fx-font-size: 34px; -fx-font-weight: 800; -fx-text-fill: white;");

        Label lblDesc = new Label(
                "Optimiza la captación y el análisis proyectivo de tu consumo de energía solar.");
        lblDesc.setWrapText(true);
        lblDesc.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        panelIzquierdo.getChildren().addAll(lblLogo, lblTitulo, lblDesc);

        VBox panelDerecho = new VBox(30);
        panelDerecho.setStyle(tarjetaDerecha);
        panelDerecho.setAlignment(Pos.CENTER);

        HBox.setHgrow(panelDerecho, Priority.ALWAYS);

        Label lblIngreso = new Label("Selecciona tu rol de acceso");
        lblIngreso.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Button btnUsuario = new Button("Ingresar como Usuario");
        btnUsuario.setStyle(botonPrincipal);
        btnUsuario.setPrefWidth(300);
        btnUsuario.setPrefHeight(50);

        btnUsuario.setOnAction(e -> {

            inicioSessionUsuarioFX vistaUsuario =
                    new inicioSessionUsuarioFX(solarServicio, conexionDB);

            vistaUsuario.mostrarVentanaAcceso(primaryStage);
        });

        Button btnAdmin = new Button("Acceso Administrador");
        btnAdmin.setStyle(botonSecundario);
        btnAdmin.setPrefWidth(300);
        btnAdmin.setPrefHeight(50);

        btnAdmin.setOnAction(e -> {

            InicioSessionAdministrativoFX vistaAdmin =
                    new InicioSessionAdministrativoFX(solarServicio, conexionDB);

            vistaAdmin.mostrarVentanaAcceso(primaryStage);
        });

        Button btnSalir = new Button("Cerrar Aplicación");
        btnSalir.setStyle(botonSalir);
        btnSalir.setPrefWidth(180);
        btnSalir.setPrefHeight(40);

        btnSalir.setOnAction(e -> primaryStage.close());

        panelDerecho.getChildren().addAll(
                lblIngreso,
                btnUsuario,
                btnAdmin,
                btnSalir
        );

        HBox root = new HBox(40);
        root.setStyle(fondoEstilo);
        root.setPadding(new Insets(40));

        HBox.setHgrow(panelDerecho, Priority.ALWAYS);

        root.getChildren().addAll(panelIzquierdo, panelDerecho);

        Scene scene = new Scene(root, 1300, 750);

        primaryStage.setScene(scene);

        primaryStage.setMaximized(true);

        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(650);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}