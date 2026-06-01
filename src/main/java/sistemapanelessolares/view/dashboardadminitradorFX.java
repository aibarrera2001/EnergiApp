package sistemapanelessolares.view;

import java.io.InputStream;
import java.sql.Connection;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import sistemapanelessolares.dominio.PanelSolar;
import sistemapanelessolares.logica.SolarService;

public class dashboardadminitradorFX {

    private final SolarService solarServicio;
    private final Connection   conexionDB;

    private ListView<PanelSolar> listViewPaneles;

    private TextField txtNombrePanel, txtTipo, txtPotencia, txtEficiencia;
    private TextField txtCostoUnidad, txtCostoInstalacion, txtGarantia, txtDescripcion;

    // ── PALETA ────────────────────────────────────────────────────────
    private static final String FONDO_OSCURO  = "#0D1B2A";
    private static final String AZUL_PRIMARY  = "#1565C0";
    private static final String AZUL_HOVER    = "#1E88E5";
    private static final String AZUL_CLARO    = "#90CAF9";
    private static final String TEXTO_BLANCO  = "#E8F4FD";
    private static final String TEXTO_GRIS    = "#B0BEC5";
    private static final String ROJO_DANGER   = "#C62828";
    private static final String ROJO_HOVER    = "#E53935";

    private static final String ESTILO_CAMPO =
            "-fx-background-color: rgba(21, 101, 192, 0.15);"
          + "-fx-border-color: rgba(144, 202, 249, 0.4);"
          + "-fx-border-radius: 8; -fx-background-radius: 8;"
          + "-fx-text-fill: #E8F4FD; -fx-prompt-text-fill: #546E7A;"
          + "-fx-font-size: 12px; -fx-padding: 8 12 8 12;";

    private static final String ESTILO_LABEL =
            "-fx-text-fill: #90CAF9; -fx-font-size: 11px; -fx-font-weight: bold;";

    private static final String TARJETA_ESTILO =
            "-fx-background-color: rgba(21, 101, 192, 0.10);"
          + "-fx-background-radius: 18;"
          + "-fx-border-color: rgba(144, 202, 249, 0.25);"
          + "-fx-border-radius: 18; -fx-border-width: 1;";

    private static final String BTN_GUARDAR =
            "-fx-background-color: #1565C0; -fx-text-fill: white;"
          + "-fx-font-size: 13px; -fx-font-weight: bold;"
          + "-fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 11 0 11 0;";

    private static final String BTN_SALIR =
            "-fx-background-color: #C62828; -fx-text-fill: white;"
          + "-fx-font-size: 13px; -fx-font-weight: bold;"
          + "-fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 11 0 11 0;";

    public dashboardadminitradorFX(SolarService solarServicio, Connection conexionDB) {
        this.solarServicio = solarServicio;
        this.conexionDB    = conexionDB;
    }

    public void mostrar(Stage stage) {

        stage.setTitle("EnergiApp — Dashboard Administrativo");

        // ── FONDO ─────────────────────────────────────────────────────
        StackPane fondoPane = new StackPane();
        Rectangle gradBg = new Rectangle();
        gradBg.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#0D1B2A")),
                new Stop(1, Color.web("#0A1628"))));
        gradBg.widthProperty().bind(fondoPane.widthProperty());
        gradBg.heightProperty().bind(fondoPane.heightProperty());
        fondoPane.getChildren().add(gradBg);

        // ── NAVBAR SUPERIOR ───────────────────────────────────────────
        HBox navbar = construirNavbar(stage);

        // ── PANEL IZQUIERDO: Lista de paneles ─────────────────────────
        VBox panelLista = new VBox(16);
        panelLista.setPadding(new Insets(24));
        panelLista.setStyle(TARJETA_ESTILO);
        panelLista.setEffect(new DropShadow(20, 0, 8, Color.color(0, 0, 0, 0.5)));
        HBox.setHgrow(panelLista, Priority.ALWAYS);

        // Cabecera lista
        Label lblCatalogo = new Label("📋  Catálogo Global de Paneles");
        lblCatalogo.setStyle("-fx-font-size: 18px; -fx-font-weight: 900;"
                           + "-fx-text-fill: " + TEXTO_BLANCO + ";");
        lblCatalogo.setEffect(new DropShadow(8, Color.web(AZUL_CLARO)));

        Label lblTotalBadge = new Label();
        lblTotalBadge.setStyle(
                "-fx-background-color: rgba(21,101,192,0.5);"
              + "-fx-text-fill: #90CAF9; -fx-font-size: 11px; -fx-font-weight: bold;"
              + "-fx-background-radius: 20; -fx-padding: 3 10 3 10;");

        Region spacerNav = new Region();
        HBox.setHgrow(spacerNav, Priority.ALWAYS);

        HBox cabeceraLista = new HBox(10, lblCatalogo, spacerNav, lblTotalBadge);
        cabeceraLista.setAlignment(Pos.CENTER_LEFT);

        // Separador
        Region sep1 = new Region();
        sep1.setPrefHeight(1);
        sep1.setStyle("-fx-background-color: rgba(144,202,249,0.2);");

        // ListView estilizado
        listViewPaneles = new ListView<>();
        listViewPaneles.setStyle(
                "-fx-background-color: transparent;"
              + "-fx-border-color: rgba(144,202,249,0.2);"
              + "-fx-border-radius: 10; -fx-background-radius: 10;");
        VBox.setVgrow(listViewPaneles, Priority.ALWAYS);

        // Cell factory personalizada
        listViewPaneles.setCellFactory(lv -> new ListCell<PanelSolar>() {
            @Override
            protected void updateItem(PanelSolar p, boolean empty) {
                super.updateItem(p, empty);
                if (empty || p == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    // Tarjeta por item
                    VBox card = new VBox(3);
                    card.setPadding(new Insets(10, 14, 10, 14));
                    card.setStyle(
                            "-fx-background-color: rgba(21,101,192,0.12);"
                          + "-fx-background-radius: 10;"
                          + "-fx-border-color: rgba(144,202,249,0.15);"
                          + "-fx-border-radius: 10;");

                    Label nombre = new Label("⚡ " + p.getNombre());
                    nombre.setStyle("-fx-text-fill: #E8F4FD; -fx-font-weight: bold; -fx-font-size: 13px;");

                    Label detalle = new Label(
                            p.getTipo() + "  •  " + p.getPotencia() + " W  •  η " + p.getEficiencia() + "%");
                    detalle.setStyle("-fx-text-fill: #90CAF9; -fx-font-size: 11px;");

                    Label precio = new Label("$ " + String.format("%.2f", p.getCostoUnidad()) + " / unidad");
                    precio.setStyle("-fx-text-fill: #4FC3F7; -fx-font-size: 11px;");

                    card.getChildren().addAll(nombre, detalle, precio);
                    setGraphic(card);
                    setText(null);
                    setStyle("-fx-background-color: transparent; -fx-padding: 3 0 3 0;");
                }
            }
        });

        actualizarListaPaneles();

        // Actualizar badge
        listViewPaneles.getItems().addListener(
            (javafx.collections.ListChangeListener<PanelSolar>) c ->
                lblTotalBadge.setText(listViewPaneles.getItems().size() + " modelos")
        );
        lblTotalBadge.setText(listViewPaneles.getItems().size() + " modelos");

        panelLista.getChildren().addAll(cabeceraLista, sep1, listViewPaneles);

        // ── PANEL DERECHO: Formulario ─────────────────────────────────
        VBox panelForm = new VBox(14);
        panelForm.setPadding(new Insets(24));
        panelForm.setPrefWidth(360);
        panelForm.setMaxWidth(380);
        panelForm.setStyle(TARJETA_ESTILO);
        panelForm.setEffect(new DropShadow(20, 0, 8, Color.color(0, 0, 0, 0.5)));

        Label tituloForm = new Label("⊕  Registrar Nuevo Modelo");
        tituloForm.setStyle("-fx-font-size: 16px; -fx-font-weight: 900;"
                          + "-fx-text-fill: " + TEXTO_BLANCO + ";");
        tituloForm.setEffect(new DropShadow(8, Color.web(AZUL_CLARO)));

        Region sep2 = new Region();
        sep2.setPrefHeight(1);
        sep2.setStyle("-fx-background-color: rgba(144,202,249,0.2);");

        // Campos
        txtNombrePanel    = crearCampo("Nombre del panel");
        txtTipo           = crearCampo("Tipo (Monocristalino, etc.)");
        txtPotencia       = crearCampo("Potencia (W)");
        txtEficiencia     = crearCampo("Eficiencia (%)");
        txtCostoUnidad    = crearCampo("Costo por unidad ($)");
        txtCostoInstalacion = crearCampo("Costo instalación ($)");
        txtGarantia       = crearCampo("Garantía (años)");
        txtDescripcion    = crearCampo("Descripción");

        // Sección numérica en grid 2x2
        GridPane gridNums = new GridPane();
        gridNums.setHgap(10);
        gridNums.setVgap(10);

        agregarGridFila(gridNums, "Potencia (W)",       txtPotencia,         0, 0);
        agregarGridFila(gridNums, "Eficiencia (%)",     txtEficiencia,       0, 1);
        agregarGridFila(gridNums, "Costo Unidad ($)",   txtCostoUnidad,      1, 0);
        agregarGridFila(gridNums, "Costo Inst. ($)",    txtCostoInstalacion, 1, 1);

        GridPane.setHgrow(txtPotencia,          Priority.ALWAYS);
        GridPane.setHgrow(txtEficiencia,        Priority.ALWAYS);
        GridPane.setHgrow(txtCostoUnidad,       Priority.ALWAYS);
        GridPane.setHgrow(txtCostoInstalacion,  Priority.ALWAYS);

        // Botones
        Button btnGuardar = new Button("💾  Guardar Modelo");
        btnGuardar.setMaxWidth(Double.MAX_VALUE);
        btnGuardar.setStyle(BTN_GUARDAR);
        aplicarHoverPrimario(btnGuardar);

        Button btnCerrarSesion = new Button("🚪  Cerrar Sesión");
        btnCerrarSesion.setMaxWidth(Double.MAX_VALUE);
        btnCerrarSesion.setStyle(BTN_SALIR);
        aplicarHoverPeligro(btnCerrarSesion);

      btnGuardar.setOnAction(e -> {
    try {
        String nombre      = txtNombrePanel.getText().trim();
        String tipo        = txtTipo.getText().trim();
        String garantia    = txtGarantia.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        if (nombre.isEmpty() || tipo.isEmpty() || garantia.isEmpty()
                || txtPotencia.getText().trim().isEmpty()
                || txtEficiencia.getText().trim().isEmpty()
                || txtCostoUnidad.getText().trim().isEmpty()
                || txtCostoInstalacion.getText().trim().isEmpty()) {
            mostrarAlertaEstilizada("Campos Vacíos",
                    "Complete todos los campos obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        double potencia         = Double.parseDouble(txtPotencia.getText().trim());
        double eficiencia       = Double.parseDouble(txtEficiencia.getText().trim());
        double costoUnidad      = Double.parseDouble(txtCostoUnidad.getText().trim());
        double costoInstalacion = Double.parseDouble(txtCostoInstalacion.getText().trim());

        PanelSolar panel = new PanelSolar(nombre, tipo, potencia, eficiencia,
                                          costoUnidad, costoInstalacion, garantia, descripcion);

        //  Guardar en Supabase panel solar
        sistemapanelessolares.dao.PanelSolarDAO dao = new sistemapanelessolares.dao.PanelSolarDAO();
        dao.guardar(panel);

        // Agregar al catálogo en memoria
        solarServicio.getGestorPaneles().agregarPanel(panel);

        mostrarAlertaEstilizada("Panel Registrado",
                "El modelo fue agregado al catálogo y guardado en Supabase.",
                Alert.AlertType.INFORMATION);
        limpiarCampos();
        actualizarListaPaneles();
        lblTotalBadge.setText(listViewPaneles.getItems().size() + " modelos");

    } catch (NumberFormatException ex) {
        mostrarAlertaEstilizada("Formato Incorrecto",
                "Ingrese solo números en Potencia, Eficiencia, Costo Unidad y Costo Instalación.",
                Alert.AlertType.ERROR);
    } catch (Exception ex) {
        mostrarAlertaEstilizada("Error", ex.getMessage(), Alert.AlertType.ERROR);
    }
});

        btnCerrarSesion.setOnAction(e ->
                new InicioSessionAdministrativoFX(solarServicio, conexionDB)
                        .mostrarVentanaAcceso(stage));

        ScrollPane scrollForm = new ScrollPane();
        scrollForm.setFitToWidth(true);
        scrollForm.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollForm.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox innerForm = new VBox(10,
                lbl("Nombre del Panel"), txtNombrePanel,
                lbl("Tipo"),             txtTipo,
                gridNums,
                lbl("Garantía"),         txtGarantia,
                lbl("Descripción"),      txtDescripcion
        );
        innerForm.setStyle("-fx-background-color: transparent;");
        scrollForm.setContent(innerForm);
        VBox.setVgrow(scrollForm, Priority.ALWAYS);

        Region sep3 = new Region();
        sep3.setPrefHeight(1);
        sep3.setStyle("-fx-background-color: rgba(144,202,249,0.2);");

        panelForm.getChildren().addAll(
                tituloForm, sep2, scrollForm, sep3, btnGuardar, btnCerrarSesion);

        // ── CONTENIDO PRINCIPAL ───────────────────────────────────────
        HBox contenido = new HBox(20, panelLista, panelForm);
        contenido.setPadding(new Insets(20, 24, 24, 24));
        VBox.setVgrow(contenido, Priority.ALWAYS);
        HBox.setHgrow(panelLista, Priority.ALWAYS);

        VBox mainLayout = new VBox(0, navbar, contenido);
        VBox.setVgrow(contenido, Priority.ALWAYS);

        fondoPane.getChildren().add(mainLayout);

        Scene scene = new Scene(fondoPane, 1400, 800);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setMinWidth(1200);
        stage.setMinHeight(700);
        stage.show();
    }

    // ── NAVBAR ────────────────────────────────────────────────────────
    private HBox construirNavbar(Stage stage) {
        HBox navbar = new HBox(16);
        navbar.setPadding(new Insets(14, 24, 14, 24));
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setStyle(
                "-fx-background-color: rgba(13,27,42,0.95);"
              + "-fx-border-color: transparent transparent rgba(144,202,249,0.2) transparent;"
              + "-fx-border-width: 0 0 1 0;");

        // Logo
        javafx.scene.Node logoNode;
        InputStream logoIs = getClass().getResourceAsStream("/sistemapanelessolares/resources/logo.jpeg");
        if (logoIs == null)
            logoIs = getClass().getResourceAsStream("/sistemapanelessolares/resources/logo.jpeg");

        if (logoIs != null) {
            ImageView iv = new ImageView(new Image(logoIs));
            iv.setFitWidth(42); iv.setFitHeight(42);
            iv.setPreserveRatio(true); iv.setSmooth(true);
            Circle clip = new Circle(21, 21, 21);
            iv.setClip(clip);
            DropShadow g = new DropShadow(10, Color.web("#4CAF50"));
            g.setSpread(0.05);
            iv.setEffect(g);
            logoNode = iv;
        } else {
            Label fb = new Label("⚡");
            fb.setStyle("-fx-font-size: 24px;");
            logoNode = fb;
        }

        VBox lblsNav = new VBox(1);
        Label lblApp = new Label("EnergiApp");
        lblApp.setStyle("-fx-font-size: 15px; -fx-font-weight: 900; -fx-text-fill: #E8F4FD;");
        Label lblRol = new Label("Panel Administrativo");
        lblRol.setStyle("-fx-font-size: 10px; -fx-text-fill: #90CAF9; -fx-font-family: 'Courier New';");
        lblsNav.getChildren().addAll(lblApp, lblRol);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblVersion = new Label("System Core v2.6  //  ADMIN MODE");
        lblVersion.setStyle("-fx-font-size: 10px; -fx-text-fill: rgba(144,202,249,0.4);"
                          + "-fx-font-family: 'Courier New';");

        navbar.getChildren().addAll(logoNode, lblsNav, spacer, lblVersion);
        return navbar;
    }

    // ── HELPERS ───────────────────────────────────────────────────────

    private TextField crearCampo(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle(ESTILO_CAMPO);
        tf.setMaxWidth(Double.MAX_VALUE);
        tf.focusedProperty().addListener((obs, o, focused) ->
            tf.setStyle(ESTILO_CAMPO + (focused
                ? "-fx-border-color: #42A5F5; -fx-border-width: 1.5;" : "")));
        return tf;
    }

    private Label lbl(String texto) {
        Label l = new Label(texto);
        l.setStyle(ESTILO_LABEL);
        return l;
    }

    private void agregarGridFila(GridPane grid, String labelTxt,
                                  TextField campo, int fila, int col) {
        VBox cell = new VBox(4, lbl(labelTxt), campo);
        grid.add(cell, col, fila);
        GridPane.setHgrow(cell, Priority.ALWAYS);
    }

    private void aplicarHoverPrimario(Button b) {
        DropShadow glow = new DropShadow(16, Color.web(AZUL_PRIMARY));
        b.setOnMouseEntered(e -> { b.setStyle(BTN_GUARDAR + "-fx-background-color:" + AZUL_HOVER + ";"); b.setEffect(glow); });
        b.setOnMouseExited(e  -> { b.setStyle(BTN_GUARDAR); b.setEffect(null); });
        b.setOnMousePressed(e -> b.setStyle(BTN_GUARDAR + "-fx-scale-x:0.98;-fx-scale-y:0.98;"));
        b.setOnMouseReleased(e-> b.setStyle(BTN_GUARDAR));
    }

    private void aplicarHoverPeligro(Button b) {
        DropShadow glow = new DropShadow(16, Color.web(ROJO_DANGER));
        b.setOnMouseEntered(e -> { b.setStyle(BTN_SALIR + "-fx-background-color:" + ROJO_HOVER + ";"); b.setEffect(glow); });
        b.setOnMouseExited(e  -> { b.setStyle(BTN_SALIR); b.setEffect(null); });
        b.setOnMousePressed(e -> b.setStyle(BTN_SALIR + "-fx-scale-x:0.98;-fx-scale-y:0.98;"));
        b.setOnMouseReleased(e-> b.setStyle(BTN_SALIR));
    }

    private void actualizarListaPaneles() {
        if (solarServicio != null && listViewPaneles != null) {
            List<PanelSolar> paneles = solarServicio.obtenerPanelesParaCatalogo();
            if (paneles != null) {
                listViewPaneles.getItems().clear();
                listViewPaneles.getItems().addAll(paneles);
            }
        }
    }

    

    private void limpiarCampos() {
        txtNombrePanel.clear(); txtTipo.clear(); txtPotencia.clear();
        txtEficiencia.clear();  txtCostoUnidad.clear();
        txtCostoInstalacion.clear(); txtGarantia.clear(); txtDescripcion.clear();
    }

    private void mostrarAlertaEstilizada(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        DialogPane dp = alerta.getDialogPane();
        dp.setStyle(
                "-fx-background-color: #0D1B2A;"
              + "-fx-border-color: rgba(144,202,249,0.4);"
              + "-fx-border-width: 1; -fx-border-radius: 12; -fx-background-radius: 12;");
        dp.lookup(".content.label").setStyle("-fx-text-fill: #E8F4FD; -fx-font-size: 13px;");
        alerta.showAndWait();
    }
}