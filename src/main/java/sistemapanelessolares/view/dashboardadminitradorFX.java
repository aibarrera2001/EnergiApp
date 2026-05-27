
package sistemapanelessolares.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import sistemapanelessolares.dominio.PanelSolar;
import sistemapanelessolares.logica.SolarService;

import java.sql.Connection;
import java.util.List;

public class dashboardadminitradorFX {

    private final SolarService solarServicio;
    private final Connection conexionDB;

    private ListView<PanelSolar> listViewPaneles;

    private TextField txtNombrePanel;
    private TextField txtTipo;
    private TextField txtPotencia;
    private TextField txtEficiencia;
    private TextField txtCostoUnidad;
    private TextField txtCostoInstalacion;
    private TextField txtGarantia;
    private TextField txtDescripcion;

    public dashboardadminitradorFX(
            SolarService solarServicio,
            Connection conexionDB
    ) {

        this.solarServicio = solarServicio;
        this.conexionDB = conexionDB;
    }

    public void mostrar(Stage stage) {

        stage.setTitle("EnergiApp - Dashboard Administrativo");

        String fondo =
                "-fx-background-color: #E1E4E2;";

        String tarjeta =
                "-fx-background-color: #F3F5F4;" +
                "-fx-background-radius: 18;" +
                "-fx-padding: 20;";

        String botonGuardar =
                "-fx-background-color: #7D8C77;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;";

        String botonSalir =
                "-fx-background-color: #E76F51;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;";

        BorderPane root = new BorderPane();

        root.setStyle(fondo);

        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(25));

        Label titulo = new Label("📋 Catálogo Global de Paneles");

        titulo.setStyle(
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;"
        );

        listViewPaneles = new ListView<>();

        VBox.setVgrow(listViewPaneles, Priority.ALWAYS);

        actualizarListaPaneles();

        contenido.getChildren().addAll(
                titulo,
                listViewPaneles
        );

        ScrollPane scroll = new ScrollPane();

        scroll.setFitToWidth(true);
        scroll.setPrefWidth(400);

        VBox formulario = new VBox(12);

        formulario.setPadding(new Insets(20));

        formulario.setStyle(tarjeta);

        Label tituloForm = new Label("⚡ Registrar Nuevo Modelo");

        tituloForm.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;"
        );

        txtNombrePanel = new TextField();
        txtNombrePanel.setPromptText("Nombre del panel");

        txtTipo = new TextField();
        txtTipo.setPromptText("Tipo");

        txtPotencia = new TextField();
        txtPotencia.setPromptText("Potencia");

        txtEficiencia = new TextField();
        txtEficiencia.setPromptText("Eficiencia");

        txtCostoUnidad = new TextField();
        txtCostoUnidad.setPromptText("Costo unidad");

        txtCostoInstalacion = new TextField();
        txtCostoInstalacion.setPromptText("Costo instalación");

        txtGarantia = new TextField();
        txtGarantia.setPromptText("Garantía");

        txtDescripcion = new TextField();
        txtDescripcion.setPromptText("Descripción");

        Button btnGuardar = new Button("💾 Guardar Modelo");

        btnGuardar.setMaxWidth(Double.MAX_VALUE);
        btnGuardar.setPrefHeight(40);

        btnGuardar.setStyle(botonGuardar);

        btnGuardar.setOnAction(e -> {

            try {

                String nombre =
                        txtNombrePanel.getText().trim();

                String tipo =
                        txtTipo.getText().trim();

                String garantia =
                        txtGarantia.getText().trim();

                String descripcion =
                        txtDescripcion.getText().trim();

                if (
                        nombre.isEmpty()
                        ||
                        tipo.isEmpty()
                        ||
                        garantia.isEmpty()
                ) {

                    mostrarAlerta(
                            "Campos Vacíos",
                            "Complete todos los campos obligatorios",
                            Alert.AlertType.WARNING
                    );

                    return;
                }

                double potencia =
                        Double.parseDouble(txtPotencia.getText());

                double eficiencia =
                        Double.parseDouble(txtEficiencia.getText());

                double costoUnidad =
                        Double.parseDouble(txtCostoUnidad.getText());

                double costoInstalacion =
                        Double.parseDouble(txtCostoInstalacion.getText());

                PanelSolar panel = new PanelSolar(
                        nombre,
                        tipo,
                        potencia,
                        eficiencia,
                        costoUnidad,
                        costoInstalacion,
                        garantia,
                        descripcion
                );

                mostrarAlerta(
                        "Éxito",
                        "Panel agregado correctamente",
                        Alert.AlertType.INFORMATION
                );

                limpiarCampos();

                actualizarListaPaneles();

            } catch (NumberFormatException ex) {

                mostrarAlerta(
                        "Error",
                        "Ingrese números válidos",
                        Alert.AlertType.ERROR
                );
            }
        });

        Button btnCerrarSesion =
                new Button("🚪 Cerrar Sesión");

        btnCerrarSesion.setMaxWidth(Double.MAX_VALUE);
        btnCerrarSesion.setPrefHeight(40);

        btnCerrarSesion.setStyle(botonSalir);

        btnCerrarSesion.setOnAction(e -> {

            InicioSessionAdministrativoFX login =
                    new InicioSessionAdministrativoFX(
                            solarServicio,
                            conexionDB
                    );

            login.mostrarVentanaAcceso(stage);
        });

        formulario.getChildren().addAll(
                tituloForm,
                new Separator(),

                new Label("Nombre"),
                txtNombrePanel,

                new Label("Tipo"),
                txtTipo,

                new Label("Potencia"),
                txtPotencia,

                new Label("Eficiencia"),
                txtEficiencia,

                new Label("Costo Unidad"),
                txtCostoUnidad,

                new Label("Costo Instalación"),
                txtCostoInstalacion,

                new Label("Garantía"),
                txtGarantia,

                new Label("Descripción"),
                txtDescripcion,

                new Separator(),

                btnGuardar,
                btnCerrarSesion
        );

        scroll.setContent(formulario);

        root.setCenter(contenido);
        root.setRight(scroll);

        BorderPane.setMargin(scroll, new Insets(25));

        Scene scene = new Scene(root, 1400, 800);

        stage.setScene(scene);

        stage.setMaximized(true);

        stage.setMinWidth(1200);
        stage.setMinHeight(700);

        stage.show();
    }

    private void actualizarListaPaneles() {

        if (solarServicio != null && listViewPaneles != null) {

            List<PanelSolar> paneles =
                    solarServicio.obtenerPanelesParaCatalogo();

            if (paneles != null) {

                listViewPaneles.getItems().clear();

                listViewPaneles.getItems().addAll(paneles);
            }
        }
    }

    private void limpiarCampos() {

        txtNombrePanel.clear();
        txtTipo.clear();
        txtPotencia.clear();
        txtEficiencia.clear();
        txtCostoUnidad.clear();
        txtCostoInstalacion.clear();
        txtGarantia.clear();
        txtDescripcion.clear();
    }

    private void mostrarAlerta(
            String titulo,
            String contenido,
            Alert.AlertType tipo
    ) {

        Alert alert = new Alert(tipo);

        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);

        alert.showAndWait();
    }
}

