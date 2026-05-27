
package sistemapanelessolares.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import sistemapanelessolares.dominio.Usuario;
import sistemapanelessolares.logica.SolarService;

import java.sql.Connection;

public class DashboardusuarioFx {

    private final Usuario usuarioLogueado;
    private final SolarService solarServicio;
    private final Connection conexionDB;

    public DashboardusuarioFx(
            Usuario usuarioLogueado,
            SolarService solarServicio,
            Connection conexionDB
    ) {

        this.usuarioLogueado = usuarioLogueado;
        this.solarServicio = solarServicio;
        this.conexionDB = conexionDB;
    }

    public void mostrar(Stage stage) {

        stage.setTitle("EnergiApp - Dashboard Usuario");

        String fondo = "-fx-background-color: #E1E4E2;";
        String tarjeta = "-fx-background-color: #F3F5F4; -fx-background-radius: 18; -fx-padding: 20;";
        String tarjetaVerde = "-fx-background-color: #7D8C77; -fx-background-radius: 18; -fx-padding: 20;";
        String boton = "-fx-background-color: #7D8C77; -fx-text-fill: white; -fx-font-weight: bold;";
        String botonNaranja = "-fx-background-color: #E76F51; -fx-text-fill: white; -fx-font-weight: bold;";

        BorderPane root = new BorderPane();
        root.setStyle(fondo);

        VBox contenido = new VBox(25);
        contenido.setPadding(new Insets(25));

        HBox topBar = new HBox();

        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle(tarjeta);

        Label logo = new Label("☀️ ENERGIAPP");
        logo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);

        Label usuario = new Label("👤 " + usuarioLogueado.getNombre());

        topBar.getChildren().addAll(
                logo,
                espacio,
                usuario
        );

        HBox metricas = new HBox(20);

        VBox card1 = new VBox(10);
        card1.setStyle(tarjeta);

        Label casas = new Label("0 Casas");
        casas.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        card1.getChildren().addAll(
                new Label("Propiedades"),
                casas
        );

        VBox card2 = new VBox(10);
        card2.setStyle(tarjetaVerde);

        Label panel = new Label("Sin Panel");
        panel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        card2.getChildren().addAll(
                new Label("Panel Actual"),
                panel
        );

        VBox card3 = new VBox(10);
        card3.setStyle(tarjeta);

        Label consumo = new Label("0 kWh");
        consumo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        card3.getChildren().addAll(
                new Label("Consumo"),
                consumo
        );

        HBox.setHgrow(card1, Priority.ALWAYS);
        HBox.setHgrow(card2, Priority.ALWAYS);
        HBox.setHgrow(card3, Priority.ALWAYS);

        metricas.getChildren().addAll(
                card1,
                card2,
                card3
        );

        VBox acciones = new VBox(20);

        acciones.setStyle(tarjeta);
        acciones.setPrefWidth(320);

        Button btnCasa = new Button("🏠 Registrar Casa");
        btnCasa.setMaxWidth(Double.MAX_VALUE);
        btnCasa.setStyle(boton);

        Button btnPanel = new Button("🔌 Seleccionar Panel");
        btnPanel.setMaxWidth(Double.MAX_VALUE);
        btnPanel.setStyle(boton);

        Button btnIA = new Button("🤖 Abrir Asistente IA");
        btnIA.setMaxWidth(Double.MAX_VALUE);
        btnIA.setStyle(botonNaranja);

        btnIA.setOnAction(e -> {

            chatBootFX chat =
                    new chatBootFX(solarServicio);

            chat.mostrar();
        });

        acciones.getChildren().addAll(
                new Label("Operaciones"),
                new Separator(),
                btnCasa,
                btnPanel,
                btnIA
        );

        contenido.getChildren().addAll(
                topBar,
                metricas
        );

        root.setCenter(contenido);
        root.setRight(acciones);

        BorderPane.setMargin(acciones, new Insets(25));

        Scene scene = new Scene(root, 1400, 800);

        stage.setScene(scene);

        stage.setMaximized(true);

        stage.setMinWidth(1100);
        stage.setMinHeight(700);

        stage.show();
    }
}

