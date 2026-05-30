package sistemapanelessolares.view;

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
import javafx.scene.shape.*;
import javafx.stage.Stage;

import sistemapanelessolares.dominio.Casa;
import sistemapanelessolares.dominio.PanelSolar;
import sistemapanelessolares.dominio.Usuario;
import sistemapanelessolares.logica.SolarService;
import sistemapanelessolares.logica.CalculadoraPanels;

import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class DashboardusuarioFx {

    private final Usuario      usuarioLogueado;
    private final SolarService solarServicio;
    private final Connection   conexionDB;

    private VBox      listaCasasBox;
    private Label     lblMetricaCasas;
    private Label     lblMetricaPanel;
    private Label     lblMetricaConsumo;
    private VBox      vboxInformeFinanciero;
    private StackPane contenedorPestanas;
    private VBox      panelDashboard;
    private ScrollPane panelGraficas;

    // Estilos reutilizables entre métodos
    private final String cardItem   = "-fx-background-color: rgba(13,30,50,0.70);"
                                    + "-fx-background-radius: 12; -fx-padding: 12 14;";
    private final String txtBlanco  = "-fx-text-fill: #E8F4FD;";
    private final String txtSub     = "-fx-text-fill: #B0BEC5; -fx-font-size: 12px;";
    private final String txtAzul    = "-fx-text-fill: #90CAF9;";
    private final DropShadow sombra = new DropShadow(12, 0, 4, Color.color(0, 0, 0, 0.45));

    public DashboardusuarioFx(Usuario usuarioLogueado,
                              SolarService solarServicio,
                              Connection conexionDB) {
        this.usuarioLogueado = usuarioLogueado;
        this.solarServicio   = solarServicio;
        this.conexionDB      = conexionDB;
    }

    public void mostrar(Stage stage) {
        stage.setTitle("EnergiApp — Panel de Usuario");

        String card          = "-fx-background-color: rgba(27,42,59,0.82);"
                             + "-fx-background-radius: 16; -fx-padding: 20;";
        String cardAccent    = "-fx-background-color: rgba(26,74,122,0.88);"
                             + "-fx-background-radius: 16; -fx-padding: 20;";
        String cardHighlight = "-fx-background-color: rgba(21,101,192,0.92);"
                             + "-fx-background-radius: 16; -fx-padding: 20;";
        String topBarStyle   = "-fx-background-color: rgba(10,22,40,0.90);"
                             + "-fx-background-radius: 12; -fx-padding: 14 24;"
                             + "-fx-border-color: rgba(30,58,95,0.6);"
                             + "-fx-border-radius: 12; -fx-border-width: 1;";
        String btnPrimario   = "-fx-background-color: #1565C0; -fx-text-fill: white;"
                             + "-fx-font-weight: bold; -fx-font-size: 13px;"
                             + "-fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 10 16;";
        String btnSecundario = "-fx-background-color: #0288D1; -fx-text-fill: white;"
                             + "-fx-font-weight: bold; -fx-font-size: 13px;"
                             + "-fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 10 16;";

        // ── Fondo ─────────────────────────────────────────────────────
        StackPane fondoPane = new StackPane();
        Rectangle gradBg = new Rectangle();
        gradBg.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#0D1B2A")), new Stop(1, Color.web("#0A1628"))));
        gradBg.widthProperty().bind(fondoPane.widthProperty());
        gradBg.heightProperty().bind(fondoPane.heightProperty());
        fondoPane.getChildren().add(gradBg);

        // ── Barra superior ─────────────────────────────────────────────
        HBox topBar = new HBox(12);
        topBar.setStyle(topBarStyle);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setEffect(sombra);

        javafx.scene.Node logoNode;
        InputStream logoIs = getClass().getResourceAsStream("/sistemapanelessolares/imagenes/logoEnergiapp.jpeg");
        if (logoIs == null) logoIs = getClass().getResourceAsStream("/images/logoEnergiapp.jpeg");
        if (logoIs != null) {
            ImageView iv = new ImageView(new Image(logoIs));
            iv.setFitHeight(34); iv.setPreserveRatio(true); iv.setSmooth(true);
            logoNode = iv;
        } else {
            Label lf = new Label("⚡  EnergiApp");
            lf.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;" + txtBlanco);
            logoNode = lf;
        }

        Label badge = new Label("  SISTEMA SOLAR  ");
        badge.setStyle("-fx-background-color: #1565C0; -fx-background-radius: 6;"
                + "-fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 3 8;");
        Region espTop = new Region();
        HBox.setHgrow(espTop, Priority.ALWAYS);
        Label lblUser = new Label("👤  " + usuarioLogueado.getNombre() + " " + usuarioLogueado.getApellido());
        lblUser.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;" + txtAzul);
        topBar.getChildren().addAll(logoNode, badge, espTop, lblUser);

        // ── Tabs ──────────────────────────────────────────────────────
        HBox selectorTabs = new HBox(8);
        selectorTabs.setPadding(new Insets(16, 24, 0, 24));
        selectorTabs.setAlignment(Pos.CENTER_LEFT);

        String btnTabNormal = "-fx-background-color: transparent; -fx-text-fill: #90CAF9;"
                + "-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 20;"
                + "-fx-cursor: hand; -fx-border-radius: 8; -fx-background-radius: 8;";
        String btnTabActivo = "-fx-background-color: #1565C0; -fx-text-fill: white;"
                + "-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 20;"
                + "-fx-cursor: hand; -fx-border-radius: 8; -fx-background-radius: 8;";

        Button btnTabDashboard = new Button("🏠 Dashboard");
        Button btnTabGraficas  = new Button("📊 Gráficas");
        btnTabDashboard.setStyle(btnTabActivo);
        btnTabGraficas.setStyle(btnTabNormal);
        aplicarHoverTab(btnTabDashboard, btnTabNormal, btnTabActivo);
        aplicarHoverTab(btnTabGraficas,  btnTabNormal, btnTabActivo);
        selectorTabs.getChildren().addAll(btnTabDashboard, btnTabGraficas);

        // ── Contenedor pestañas ───────────────────────────────────────
        contenedorPestanas = new StackPane();
        VBox.setVgrow(contenedorPestanas, Priority.ALWAYS);

        panelDashboard = crearPanelDashboard(card, cardAccent, cardHighlight,
                btnPrimario, btnSecundario, stage);
        panelDashboard.setVisible(true);
        panelDashboard.setManaged(true);

        panelGraficas = crearPanelGraficas();
        panelGraficas.setVisible(false);
        panelGraficas.setManaged(false);

        contenedorPestanas.getChildren().addAll(panelDashboard, panelGraficas);

        btnTabDashboard.setOnAction(e -> {
            mostrarSoloPanel(panelDashboard, panelGraficas);
            btnTabDashboard.setStyle(btnTabActivo);
            btnTabGraficas.setStyle(btnTabNormal);
        });
        btnTabGraficas.setOnAction(e -> {
            mostrarSoloPanel(panelGraficas, panelDashboard);
            btnTabGraficas.setStyle(btnTabActivo);
            btnTabDashboard.setStyle(btnTabNormal);
        });

        VBox contenidoPrincipal = new VBox(0);
        contenidoPrincipal.getChildren().addAll(topBar, selectorTabs, contenedorPestanas);
        VBox.setVgrow(contenedorPestanas, Priority.ALWAYS);

        fondoPane.getChildren().add(contenidoPrincipal);
        StackPane.setAlignment(contenidoPrincipal, Pos.TOP_LEFT);

        stage.setScene(new Scene(fondoPane, 1400, 800));
        stage.setMaximized(true);
        stage.setMinWidth(1100);
        stage.setMinHeight(700);
        stage.show();

        // ✅ Cargar casas y métricas al iniciar el dashboard
        refrescarCasas();
        actualizarMetricas();
    }

    // ── Panel Dashboard ───────────────────────────────────────────────
    private VBox crearPanelDashboard(String card, String cardAccent, String cardHighlight,
                                     String btnPrimario, String btnSecundario, Stage stage) {
        VBox contenedor = new VBox(20);
        contenedor.setPadding(new Insets(24));

        lblMetricaCasas   = metricaVal("0 Casas");
        lblMetricaPanel   = metricaVal("Sin Panel");
        lblMetricaConsumo = metricaVal("0 kWh");

        VBox c1 = metricaCard(card,          "🏠  Propiedades",   lblMetricaCasas,   "Registradas",   sombra);
        VBox c2 = metricaCard(cardAccent,    "⚡  Panel Activo",  lblMetricaPanel,   "Modelo actual", sombra);
        VBox c3 = metricaCard(cardHighlight, "📊  Consumo Total", lblMetricaConsumo, "kWh estimados", sombra);
        HBox metricas = new HBox(18, c1, c2, c3);
        HBox.setHgrow(c1, Priority.ALWAYS);
        HBox.setHgrow(c2, Priority.ALWAYS);
        HBox.setHgrow(c3, Priority.ALWAYS);

        HBox contenidoPrincipal = new HBox(18);
        HBox.setHgrow(contenidoPrincipal, Priority.ALWAYS);

        // Panel izquierdo
        VBox panelInfo = new VBox(14);
        panelInfo.setStyle(card);
        panelInfo.setEffect(sombra);
        HBox.setHgrow(panelInfo, Priority.ALWAYS);

        Label lblInfoTitle = new Label("📋  Resumen de tu instalación");
        lblInfoTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;" + txtBlanco);
        Separator sepInfo = new Separator();
        sepInfo.setStyle("-fx-background-color: #1E3A5F;");

        VBox cardPanel = new VBox(6);
        cardPanel.setStyle(cardItem);
        Label lblPanelTitle  = new Label("🔌  Panel Solar");
        lblPanelTitle.setStyle(txtSub);
        Label lblPanelNombre = new Label("Sin seleccionar");
        lblPanelNombre.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;" + txtAzul);
        Label lblPanelDetalle = new Label("—");
        lblPanelDetalle.setStyle(txtSub);
        cardPanel.getChildren().addAll(lblPanelTitle, lblPanelNombre, lblPanelDetalle);

        Label lblCasasTitle = new Label("🏠  Propiedades registradas");
        lblCasasTitle.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;" + txtBlanco);

        listaCasasBox = new VBox(8);
        ScrollPane scrollCasas = new ScrollPane(listaCasasBox);
        scrollCasas.setFitToWidth(true);
        scrollCasas.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollCasas.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollCasas.setPrefHeight(150);

        vboxInformeFinanciero = new VBox(12);
        vboxInformeFinanciero.setAlignment(Pos.CENTER);
        vboxInformeFinanciero.setStyle(
                "-fx-background-color: rgba(13,30,50,0.50);"
              + "-fx-border-color: rgba(30,58,95,0.6);"
              + "-fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 30;");
        VBox.setVgrow(vboxInformeFinanciero, Priority.ALWAYS);
        Label lblPlaceholder = new Label("📊  Estudio Financiero Pendiente");
        lblPlaceholder.setStyle("-fx-text-fill: #90CAF9; -fx-font-weight: bold; -fx-font-size: 14px;");
        Label lblPlaceholderSub = new Label(
                "Selecciona un panel y haz clic en 'Generar Informe' para proyectar tus ahorros.");
        lblPlaceholderSub.setStyle(txtSub);
        lblPlaceholderSub.setWrapText(true);
        lblPlaceholderSub.setAlignment(Pos.CENTER);
        vboxInformeFinanciero.getChildren().addAll(lblPlaceholder, lblPlaceholderSub);

        panelInfo.getChildren().addAll(
                lblInfoTitle, sepInfo, cardPanel,
                lblCasasTitle, scrollCasas, vboxInformeFinanciero);

        VBox acciones = crearPanelAcciones(btnPrimario, btnSecundario, card, stage,
                lblPanelNombre, lblPanelDetalle);

        contenidoPrincipal.getChildren().addAll(panelInfo, acciones);
        contenedor.getChildren().addAll(metricas, contenidoPrincipal);
        VBox.setVgrow(contenidoPrincipal, Priority.ALWAYS);
        return contenedor;
    }

    // ── Panel Acciones ────────────────────────────────────────────────
    private VBox crearPanelAcciones(String btnPrimario, String btnSecundario, String card,
                                    Stage stage, Label lblPanelNombre, Label lblPanelDetalle) {
        VBox acciones = new VBox(14);
        acciones.setStyle(card);
        acciones.setPrefWidth(275);
        acciones.setEffect(sombra);

        Label lblAcc = new Label("Operaciones");
        lblAcc.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;" + txtBlanco);
        Label lblAccSub = new Label("Gestiona tu instalación solar");
        lblAccSub.setStyle(txtSub);
        lblAccSub.setWrapText(true);
        Separator sep2 = new Separator();
        sep2.setStyle("-fx-background-color: #1E3A5F;");

        Button btnCasa    = boton("🏠  Registrar Casa",    btnPrimario,   sombra);
        Button btnPanel   = boton("🔌  Seleccionar Panel", btnSecundario, sombra);
        Button btnInforme = boton("📊  Generar Informe",
                "-fx-background-color: #2E7D32; -fx-text-fill: white;"
              + "-fx-font-weight: bold; -fx-font-size: 13px;"
              + "-fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 10 16;", sombra);

        btnCasa.setOnAction(e -> {
            try {
                Registro reg = new Registro(conexionDB);
                Optional<Casa> res = reg.mostrarModalRegistroCasa(usuarioLogueado.getId());
                res.ifPresent(casa -> {
                    usuarioLogueado.agregarCasa(casa);
                    refrescarCasas();
                    actualizarMetricas();
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                alerta("Error", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        btnPanel.setOnAction(e -> {
            try {
                List<PanelSolar> paneles = solarServicio.getGestorPaneles().listarPorPrecioAscendente();
                if (paneles.isEmpty()) {
                    alerta("Sin paneles", "No hay paneles en el catálogo.", Alert.AlertType.WARNING);
                    return;
                }
                ChoiceDialog<PanelSolar> dlg = new ChoiceDialog<>(paneles.get(0), paneles);
                dlg.setTitle("Catálogo de Paneles");
                dlg.setHeaderText("Selecciona el panel solar para tu instalación");
                dlg.setContentText("Panel:");
                Optional<PanelSolar> res = dlg.showAndWait();
                res.ifPresent(panel -> {
                    usuarioLogueado.setPanelSeleccionado(panel);
                    lblPanelNombre.setText(panel.getNombre());
                    lblPanelDetalle.setText(panel.getTipo()
                            + "  |  " + (int) panel.getPotenciaWatts() + " W"
                            + "  |  $" + String.format("%,.0f", panel.getCostoUnidad()));
                    actualizarMetricas();
                    refrescarCasas();
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                alerta("Error", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        btnInforme.setOnAction(e -> generarInformeFinanciero());

        // Botón IA
        Label icoChat  = new Label("💬");
        icoChat.setStyle("-fx-font-size: 22px;");
        VBox txtChat   = new VBox(1);
        Label lblChatT = new Label("Asistente IA");
        lblChatT.setStyle("-fx-text-fill: #E3F2FD; -fx-font-size: 13px; -fx-font-weight: bold;");
        Label lblChatS = new Label("Deja un mensaje");
        lblChatS.setStyle("-fx-text-fill: #90CAF9; -fx-font-size: 10px;");
        txtChat.getChildren().addAll(lblChatT, lblChatS);
        Label puntito = new Label("●");
        puntito.setStyle("-fx-text-fill: #69F0AE; -fx-font-size: 9px;");
        Region espAI = new Region();
        HBox.setHgrow(espAI, Priority.ALWAYS);
        HBox cAI = new HBox(12, icoChat, txtChat, new HBox(4, espAI, puntito));
        cAI.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(txtChat, Priority.ALWAYS);

        String esAI  = "-fx-background-color: rgba(21,101,192,0.55); -fx-background-radius: 14;"
                + "-fx-border-color: rgba(100,181,246,0.40); -fx-border-radius: 14;"
                + "-fx-border-width: 1; -fx-cursor: hand; -fx-padding: 11 14;";
        String esAIH = "-fx-background-color: rgba(21,101,192,0.78); -fx-background-radius: 14;"
                + "-fx-border-color: rgba(144,202,249,0.70); -fx-border-radius: 14;"
                + "-fx-border-width: 1; -fx-cursor: hand; -fx-padding: 11 14;";

        Pane btnIAPane = new Pane(cAI);
        btnIAPane.setStyle(esAI);
        btnIAPane.setMaxWidth(Double.MAX_VALUE);
        btnIAPane.setPrefHeight(54);
        cAI.prefWidthProperty().bind(btnIAPane.widthProperty());
        cAI.prefHeightProperty().bind(btnIAPane.heightProperty());
        btnIAPane.setEffect(new DropShadow(10, 0, 3, Color.color(0, 0.3, 0.8, 0.25)));
        btnIAPane.setOnMouseEntered(ev -> btnIAPane.setStyle(esAIH));
        btnIAPane.setOnMouseExited(ev  -> btnIAPane.setStyle(esAI));
        btnIAPane.setOnMouseClicked(ev -> new chatBootFX(solarServicio).mostrar());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button btnCerrar = boton("🚪  Cerrar Sesión",
                "-fx-background-color: #1B2A3B; -fx-text-fill: #90CAF9;"
              + "-fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 10;"
              + "-fx-cursor: hand; -fx-border-color: #1E3A5F; -fx-border-radius: 10;"
              + "-fx-border-width: 1; -fx-padding: 10 16;", sombra);
        btnCerrar.setOnAction(ev -> {
            try { new IngresoFX().start(stage); }
            catch (Exception ex) { ex.printStackTrace(); }
        });

        acciones.getChildren().addAll(lblAcc, lblAccSub, sep2,
                btnCasa, btnPanel, btnInforme, btnIAPane, spacer, btnCerrar);
        return acciones;
    }

    // ── Panel Gráficas ────────────────────────────────────────────────
    private ScrollPane crearPanelGraficas() {
        PestanaGraficasFX pestana = new PestanaGraficasFX(usuarioLogueado, solarServicio);
        Tab tab = pestana.crearPestanaGraficas();
        ScrollPane sp = (ScrollPane) tab.getContent();
        sp.setVisible(true);
        sp.setManaged(true);
        return sp;
    }

    // ── Informe Financiero ────────────────────────────────────────────
    private void generarInformeFinanciero() {
        if (usuarioLogueado.getPanelSeleccionado() == null) {
            alerta("Faltan Datos", "Selecciona un panel solar antes de calcular.", Alert.AlertType.WARNING);
            return;
        }
        if (usuarioLogueado.getCasas() == null || usuarioLogueado.getCasas().isEmpty()) {
            alerta("Faltan Datos", "Registra al menos una propiedad.", Alert.AlertType.WARNING);
            return;
        }

        Casa casaActiva = usuarioLogueado.getCasas().get(0);
        PanelSolar panelSel = usuarioLogueado.getPanelSeleccionado();
        CalculadoraPanels calc = new CalculadoraPanels(casaActiva, panelSel, panelSel.getCostoInstalacion());

        int numPaneles = calc.calcularNumeroPaneles();
        double hsp = calc.getHorasSolEstimadas();
        double generacionMensual = calc.calcularGeneracionMensualKWh();
        double inversionTotal    = calc.calcularCostoTotal();
        double precioKWh         = 1000.0;
        double ahorroMensual     = Math.min(casaActiva.getConsumoMensualKWh(), generacionMensual) * precioKWh;
        double ahorroAnual       = ahorroMensual * 12;
        int mesesRetorno = (ahorroMensual > 0) ? (int) Math.ceil(inversionTotal / ahorroMensual) : 0;
        int aniosROI  = mesesRetorno / 12;
        int mesesROI  = mesesRetorno % 12;

        vboxInformeFinanciero.getChildren().clear();
        vboxInformeFinanciero.setAlignment(Pos.TOP_LEFT);
        vboxInformeFinanciero.setSpacing(14);
        vboxInformeFinanciero.setStyle(
                "-fx-background-color: rgba(10,24,43,0.95);"
              + "-fx-border-color: #0288D1; -fx-border-radius: 12;"
              + "-fx-background-radius: 12; -fx-padding: 20; -fx-border-width: 1.5;");

        HBox headerFactura = new HBox();
        VBox headerLeft    = new VBox(2);
        Label titleF = new Label("PROYECCIÓN DE AUDITORÍA ENERGÉTICA");
        titleF.setStyle("-fx-text-fill: #E8F4FD; -fx-font-weight: bold; -fx-font-size: 13px;");
        Label subF = new Label("EnergiApp Engine v2.1 — Resumen de Amortización");
        subF.setStyle("-fx-text-fill: #64B5F6; -fx-font-size: 10px; -fx-font-weight: bold;");
        headerLeft.getChildren().addAll(titleF, subF);
        Region rSpacer = new Region();
        HBox.setHgrow(rSpacer, Priority.ALWAYS);
        Label lblStatus = new Label("● DIAGNÓSTICO FINANCIERO");
        lblStatus.setStyle("-fx-text-fill: #69F0AE; -fx-font-size: 10px; -fx-font-weight: bold;");
        headerFactura.getChildren().addAll(headerLeft, rSpacer, lblStatus);

        Separator sepF = new Separator();
        sepF.setStyle("-fx-background-color: #1E3A5F;");

        GridPane gridMetricas = new GridPane();
        gridMetricas.setHgap(30); gridMetricas.setVgap(12);
        gridMetricas.add(crearItemFactura("UBICACIÓN EVALUADA",
                casaActiva.getCiudad() + " (" + casaActiva.getDireccion() + ")", "#E8F4FD"), 0, 0);
        gridMetricas.add(crearItemFactura("RADIACIÓN SOLAR (HSP)",
                String.format("%.2f horas/día", hsp), "#90CAF9"), 1, 0);
        gridMetricas.add(crearItemFactura("ARQUITECTURA DEL SISTEMA",
                numPaneles + " Paneles × " + panelSel.getNombre(), "#E8F4FD"), 0, 1);
        gridMetricas.add(crearItemFactura("PRODUCCIÓN CALCULADA",
                String.format("%.1f kWh/mes", generacionMensual), "#A5D6A7"), 1, 1);

        Separator sepF2 = new Separator();
        sepF2.setStyle("-fx-background-color: #1E3A5F;");

        HBox filaMontos = new HBox(20);
        filaMontos.setAlignment(Pos.CENTER_LEFT);
        VBox boxInversion = crearBloqueDestacado("INVERSIÓN TOTAL",
                String.format("$%,.0f", inversionTotal), "rgba(2,136,209,0.15)", "#0288D1");
        VBox boxAhorroM   = crearBloqueDestacado("AHORRO MENSUAL",
                String.format("$%,.0f", ahorroMensual), "rgba(46,125,50,0.15)", "#69F0AE");
        VBox boxAhorroA   = crearBloqueDestacado("AHORRO ANUAL",
                String.format("$%,.0f", ahorroAnual), "rgba(46,125,50,0.15)", "#69F0AE");
        HBox.setHgrow(boxInversion, Priority.ALWAYS);
        HBox.setHgrow(boxAhorroM,   Priority.ALWAYS);
        HBox.setHgrow(boxAhorroA,   Priority.ALWAYS);
        filaMontos.getChildren().addAll(boxInversion, boxAhorroM, boxAhorroA);

        HBox boxROI = new HBox(12);
        boxROI.setAlignment(Pos.CENTER_LEFT);
        boxROI.setStyle("-fx-background-color: rgba(21,101,192,0.2); -fx-padding: 12;"
                + "-fx-background-radius: 8; -fx-border-color: #1565C0; -fx-border-radius: 8;");
        Label icoClock = new Label("⏳");
        icoClock.setStyle("-fx-font-size: 20px;");
        VBox txtROIBox = new VBox(2);
        Label lblROILector = new Label("TIEMPO ESTIMADO PARA RECUPERAR EL 100% DE TU INVERSIÓN");
        lblROILector.setStyle("-fx-text-fill: #90CAF9; -fx-font-size: 9px; -fx-font-weight: bold;");
        String textoTiempo = (mesesRetorno > 0)
                ? String.format("%d Años y %d Meses (%d meses totales)", aniosROI, mesesROI, mesesRetorno)
                : "Incalculable (Generacion nula)";
        Label lblROITiempo = new Label(textoTiempo);
        lblROITiempo.setStyle("-fx-text-fill: #FFF59D; -fx-font-size: 14px; -fx-font-weight: bold;");
        txtROIBox.getChildren().addAll(lblROILector, lblROITiempo);
        boxROI.getChildren().addAll(icoClock, txtROIBox);

        vboxInformeFinanciero.getChildren().addAll(
                headerFactura, sepF, gridMetricas, sepF2, filaMontos, boxROI);
    }

    // ── Refrescar casas ───────────────────────────────────────────────
    private void refrescarCasas() {
        listaCasasBox.getChildren().clear();
        List<Casa> casas = usuarioLogueado.getCasas();
        if (casas == null || casas.isEmpty()) {
            Label v = new Label("  Aún no tienes casas registradas.");
            v.setStyle(txtSub);
            listaCasasBox.getChildren().add(v);
            return;
        }
        for (int i = 0; i < casas.size(); i++) {
            Casa c = casas.get(i);
            VBox item = new VBox(4);
            item.setStyle(cardItem);
            item.setEffect(new DropShadow(6, 0, 2, Color.color(0, 0, 0, 0.3)));

            HBox fila1 = new HBox(8);
            Label num = new Label("#" + (i + 1));
            num.setStyle("-fx-text-fill: #64B5F6; -fx-font-weight: bold; -fx-font-size: 13px;");
            Label dir = new Label(c.getDireccion());
            dir.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;" + txtBlanco);
            fila1.getChildren().addAll(num, dir);

            Label ciudad  = new Label("📍  " + c.getCiudad());
            ciudad.setStyle(txtSub);
            Label consumo = new Label("⚡  " + String.format("%.1f", c.getConsumoDiarioKWh() * 30) + " kWh/mes");
            consumo.setStyle("-fx-text-fill: #4FC3F7; -fx-font-size: 12px;");

            if (usuarioLogueado.getPanelSeleccionado() != null) {
                CalculadoraPanels calc = new CalculadoraPanels(c,
                        usuarioLogueado.getPanelSeleccionado(),
                        usuarioLogueado.getPanelSeleccionado().getCostoInstalacion());
                Label res = new Label("🔋  " + calc.calcularNumeroPaneles() + " paneles  |  $"
                        + String.format("%,.0f", calc.calcularCostoTotal()) + " estimado");
                res.setStyle("-fx-text-fill: #A5D6A7; -fx-font-size: 12px;");
                item.getChildren().addAll(fila1, ciudad, consumo, res);
            } else {
                item.getChildren().addAll(fila1, ciudad, consumo);
            }
            listaCasasBox.getChildren().add(item);
        }
    }

    // ── Actualizar métricas ───────────────────────────────────────────
    private void actualizarMetricas() {
        int total = usuarioLogueado.getCasas() != null ? usuarioLogueado.getCasas().size() : 0;
        lblMetricaCasas.setText(total + (total == 1 ? " Casa" : " Casas"));
        if (usuarioLogueado.getPanelSeleccionado() != null)
            lblMetricaPanel.setText(usuarioLogueado.getPanelSeleccionado().getNombre());
        if (usuarioLogueado.getCasas() != null) {
            double consumo = usuarioLogueado.getCasas().stream()
                    .mapToDouble(c -> c.getConsumoDiarioKWh() * 30).sum();
            lblMetricaConsumo.setText(String.format("%.0f", consumo) + " kWh");
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────
    private void mostrarSoloPanel(javafx.scene.Node visible, javafx.scene.Node... ocultos) {
        visible.setVisible(true); visible.setManaged(true);
        for (javafx.scene.Node n : ocultos) { n.setVisible(false); n.setManaged(false); }
    }

    private void aplicarHoverTab(Button btn, String estiloNormal, String estiloActivo) {
        btn.setOnMouseEntered(e -> {
            if (!btn.getStyle().equals(estiloActivo))
                btn.setStyle(estiloNormal + "-fx-background-color: rgba(21,101,192,0.15);");
        });
        btn.setOnMouseExited(e -> {
            if (!btn.getStyle().equals(estiloActivo)) btn.setStyle(estiloNormal);
        });
    }

    private VBox crearItemFactura(String titulo, String valor, String colorHex) {
        VBox box = new VBox(2);
        Label lblT = new Label(titulo);
        lblT.setStyle("-fx-text-fill: #B0BEC5; -fx-font-size: 9px; -fx-font-weight: bold;");
        Label lblV = new Label(valor);
        lblV.setStyle("-fx-text-fill: " + colorHex + "; -fx-font-size: 12px; -fx-font-weight: bold;");
        box.getChildren().addAll(lblT, lblV);
        return box;
    }

    private VBox crearBloqueDestacado(String titulo, String monto, String fondoRgba, String colorBorde) {
        VBox box = new VBox(4);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: " + fondoRgba + "; -fx-border-color: " + colorBorde + ";"
                + "-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10 14;");
        Label lblT = new Label(titulo);
        lblT.setStyle("-fx-text-fill: #B0BEC5; -fx-font-size: 9px; -fx-font-weight: bold;");
        Label lblM = new Label(monto);
        lblM.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        box.getChildren().addAll(lblT, lblM);
        return box;
    }

    private Label metricaVal(String texto) {
        Label l = new Label(texto);
        l.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #E8F4FD;");
        return l;
    }

    private VBox metricaCard(String estilo, String titulo, Label valor, String sub, DropShadow s) {
        VBox v = new VBox(8);
        v.setStyle(estilo); v.setEffect(s);
        Label t  = new Label(titulo); t.setStyle(txtSub);
        Label sb = new Label(sub);    sb.setStyle(txtSub);
        v.getChildren().addAll(t, valor, sb);
        return v;
    }

    private Button boton(String texto, String estilo, DropShadow s) {
        Button b = new Button(texto);
        b.setMaxWidth(Double.MAX_VALUE); b.setPrefHeight(42);
        b.setStyle(estilo); b.setEffect(s);
        b.setOnMouseEntered(e  -> b.setStyle(estilo + "-fx-opacity:0.82;"));
        b.setOnMouseExited(e   -> b.setStyle(estilo));
        b.setOnMousePressed(e  -> b.setStyle(estilo + "-fx-scale-x:0.97;-fx-scale-y:0.97;"));
        b.setOnMouseReleased(e -> b.setStyle(estilo));
        return b;
    }

    private void alerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo); a.setHeaderText(null); a.setContentText(msg);
        a.showAndWait();
    }
}