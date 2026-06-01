package sistemapanelessolares.view;

import java.io.InputStream;
import java.sql.Connection;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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

        primaryStage.setTitle("EnergiApp — Sistema de Gestión Solar");

        String tarjetaIzquierda = "-fx-background-color: rgba(21, 101, 192, 0.22);"
                                + "-fx-background-radius: 24;"
                                + "-fx-border-color: rgba(144, 202, 249, 0.35);"
                                + "-fx-border-radius: 24;"
                                + "-fx-border-width: 1.5;"
                                + "-fx-padding: 40;";

        String tarjetaDerecha   = "-fx-background-color: rgba(27, 42, 59, 0.85);"
                                + "-fx-background-radius: 24;"
                                + "-fx-border-color: rgba(30, 58, 95, 0.5);"
                                + "-fx-border-radius: 24;"
                                + "-fx-border-width: 1;"
                                + "-fx-padding: 40;";

        String botonPrincipal   = "-fx-background-color: #1565C0; -fx-text-fill: white;"
                                + "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 12;"
                                + "-fx-cursor: hand;";

        String botonSecundario  = "-fx-background-color: transparent; -fx-text-fill: #0288D1;"
                                + "-fx-font-size: 14px; -fx-font-weight: bold; -fx-border-color: #0288D1;"
                                + "-fx-border-radius: 12; -fx-border-width: 1.5; -fx-background-radius: 12;"
                                + "-fx-cursor: hand;";

        String botonSalir       = "-fx-background-color: #1B2A3B; -fx-text-fill: #90CAF9;"
                                + "-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 10;"
                                + "-fx-border-color: rgba(30, 58, 95, 0.8); -fx-border-radius: 10;"
                                + "-fx-cursor: hand;";

        String txtBlanco        = "-fx-text-fill: #E8F4FD;";
        String txtSub           = "-fx-text-fill: #B0BEC5;";
        String txtCianNeon      = "-fx-text-fill: #90CAF9;";

        DropShadow glowAzul  = new DropShadow(20, Color.web("#1565C0"));
        DropShadow sombraCard = new DropShadow(25, 0, 10, Color.color(0, 0, 0, 0.55));

        // ── FONDO ─────────────────────────────────────────────────────
        StackPane fondoPane = new StackPane();
        Rectangle gradBg = new Rectangle();
        gradBg.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#0D1B2A")), new Stop(1, Color.web("#0A1628"))));
        gradBg.widthProperty().bind(fondoPane.widthProperty());
        gradBg.heightProperty().bind(fondoPane.heightProperty());
        fondoPane.getChildren().add(gradBg);

        // ── PANEL IZQUIERDO ──────────────────────────────────────────
        VBox panelIzquierdo = new VBox(30);
        panelIzquierdo.setStyle(tarjetaIzquierda);
        panelIzquierdo.setPrefWidth(400);
        // *** CLAVE: centrar todo el contenido del panel izquierdo ***
        panelIzquierdo.setAlignment(Pos.TOP_CENTER);
        panelIzquierdo.setEffect(sombraCard);

        // ── CARGA Y CENTRADO DEL LOGO ────────────────────────────────
        javafx.scene.Node logoNode;

        InputStream logoIs = getClass().getResourceAsStream(
                "/sistemapanelessolares/resources/logo.jpeg");

        if (logoIs == null) {
            logoIs = getClass().getClassLoader().getResourceAsStream("/sistemapanelessolares/resources/logo.jpeg");
        }

        if (logoIs != null) {
            Image img = new Image(logoIs);
            ImageView iv = new ImageView(img);
            iv.setFitWidth(200);
            iv.setFitHeight(200);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);

            // Recorte circular para eliminar el fondo blanco visualmente
            Circle clip = new Circle(100, 100, 100);
            iv.setClip(clip);

            // Glow suave verde/azul acorde al logo
            DropShadow logoGlow = new DropShadow(22, Color.web("#4CAF50"));
            logoGlow.setSpread(0.08);
            iv.setEffect(logoGlow);

            // Contenedor centrado horizontalmente
            HBox contenedorLogo = new HBox(iv);
            contenedorLogo.setAlignment(Pos.CENTER);
            logoNode = contenedorLogo;

        } else {
            System.err.println("[EnergiApp] Logo no encontrado. Usando texto de reemplazo.");
            Label lblLogoFallback = new Label("⚡ EnergiApp");
            lblLogoFallback.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;" + txtBlanco);
            logoNode = lblLogoFallback;
        }

        Label lblTitulo = new Label("EnergiApp");
        lblTitulo.setStyle("-fx-font-size: 30px; -fx-font-weight: 900; -fx-line-spacing: -2px;"
                         + "-fx-text-alignment: center;" + txtBlanco);
        lblTitulo.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        lblTitulo.setEffect(new DropShadow(10, Color.web("#90CAF9")));

        Label lblDesc = new Label("Optimiza la captación y el análisis proyectivo de tu consumo de energía solar a través de inteligencia predictiva.");
        lblDesc.setWrapText(true);
        lblDesc.setStyle("-fx-font-size: 13px; -fx-line-spacing: 3px; -fx-text-alignment: center;" + txtSub);
        lblDesc.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        lblDesc.setMaxWidth(300);

        Region spacerIzquierdo = new Region();
        VBox.setVgrow(spacerIzquierdo, Priority.ALWAYS);

        Label lblFooterVersion = new Label("System Core v2.6 // Active");
        lblFooterVersion.setStyle("-fx-font-size: 10px; -fx-font-family: 'Courier New';" + txtCianNeon);

        panelIzquierdo.getChildren().addAll(
                logoNode,
                lblTitulo,
                lblDesc,
                spacerIzquierdo,
                lblFooterVersion
        );

        // ── PANEL DERECHO ─────────────────────────────────────────────
        VBox panelDerecho = new VBox(26);
        panelDerecho.setStyle(tarjetaDerecha);
        panelDerecho.setAlignment(Pos.CENTER);
        panelDerecho.setEffect(sombraCard);
        HBox.setHgrow(panelDerecho, Priority.ALWAYS);

        Label lblIngreso = new Label("Selecciona tu rol de acceso");
        lblIngreso.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;" + txtBlanco);

        Label lblSubIngreso = new Label("Inicia sesión para interactuar con la red de paneles solares.");
        lblSubIngreso.setStyle("-fx-font-size: 13px;" + txtSub);
        lblSubIngreso.setPadding(new Insets(-15, 0, 10, 0));

        Button btnUsuario = new Button("Ingresar como Usuario");
        btnUsuario.setStyle(botonPrincipal);
        btnUsuario.setPrefWidth(340);
        btnUsuario.setPrefHeight(52);
        configurarAnimacionBoton(btnUsuario, botonPrincipal, "#1565C0", true, glowAzul);

        btnUsuario.setOnAction(e -> {
            inicioSessionUsuarioFX vistaUsuario = new inicioSessionUsuarioFX(solarServicio, conexionDB);
            vistaUsuario.mostrarVentanaAcceso(primaryStage);
        });

        Button btnAdmin = new Button("Acceso Administrador");
        btnAdmin.setStyle(botonSecundario);
        btnAdmin.setPrefWidth(340);
        btnAdmin.setPrefHeight(52);
        configurarAnimacionBoton(btnAdmin, botonSecundario, "#0288D1", false, glowAzul);

        btnAdmin.setOnAction(e -> {
            InicioSessionAdministrativoFX vistaAdmin = new InicioSessionAdministrativoFX(solarServicio, conexionDB);
            vistaAdmin.mostrarVentanaAcceso(primaryStage);
        });

        Region divider = new Region();
        divider.setPrefHeight(2);
        divider.setStyle("-fx-background-color: rgba(30, 58, 95, 0.4);");
        divider.setMaxWidth(200);

        Button btnSalir = new Button("🚪 Cerrar Aplicación");
        btnSalir.setStyle(botonSalir);
        btnSalir.setPrefWidth(200);
        btnSalir.setPrefHeight(42);
        btnSalir.setOnMouseEntered(e -> btnSalir.setStyle(botonSalir + "-fx-background-color: #A5D6A7; -fx-text-fill: #0D1B2A;"));
        btnSalir.setOnMouseExited(e -> btnSalir.setStyle(botonSalir));
        btnSalir.setOnAction(e -> primaryStage.close());

        panelDerecho.getChildren().addAll(
                lblIngreso,
                lblSubIngreso,
                btnUsuario,
                btnAdmin,
                divider,
                btnSalir
        );

        // ── ROOT ──────────────────────────────────────────────────────
        HBox root = new HBox(35);
        root.setStyle("-fx-background-color: transparent;");
        root.setPadding(new Insets(45));
        root.setAlignment(Pos.CENTER);

        HBox.setHgrow(panelDerecho, Priority.ALWAYS);
        root.getChildren().addAll(panelIzquierdo, panelDerecho);

        fondoPane.getChildren().add(root);

        Scene scene = new Scene(fondoPane, 1300, 750);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(680);
        primaryStage.show();
    }

    private void configurarAnimacionBoton(Button b, String estiloBase, String colorHex,
                                           boolean esPrincipal, DropShadow glow) {
        b.setOnMouseEntered(e -> {
            if (esPrincipal) {
                b.setStyle(estiloBase + "-fx-background-color: #1E88E5;");
                b.setEffect(glow);
            } else {
                b.setStyle(estiloBase + "-fx-background-color: rgba(2, 136, 209, 0.15);");
            }
        });
        b.setOnMouseExited(e -> {
            b.setStyle(estiloBase);
            b.setEffect(null);
        });
        b.setOnMousePressed(e -> b.setStyle(estiloBase + "-fx-scale-x: 0.98; -fx-scale-y: 0.98;"));
        b.setOnMouseReleased(e -> b.setStyle(estiloBase));
    }

    public static void main(String[] args) {
        launch(args);
    }
}