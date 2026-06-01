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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DashboardusuarioFx {

    private final Usuario      usuarioLogueado;
    private final SolarService solarServicio;
    private final Connection   conexionDB;

    // Panel seleccionado por cada índice de casa
    private final Map<Integer, PanelSolar> panelesPorCasa = new HashMap<>();

    private VBox       listaCasasBox;
    private Label      lblMetricaCasas;
    private Label      lblMetricaPanel;
    private Label      lblMetricaConsumo;
    private VBox       vboxInformeFinanciero;
    private StackPane  contenedorPestanas;
    private VBox       panelDashboard;
    private ScrollPane panelGraficas;

    private final String cardItem  = "-fx-background-color: rgba(13,30,50,0.70);"
                                   + "-fx-background-radius: 12; -fx-padding: 12 14;";
    private final String txtBlanco = "-fx-text-fill: #E8F4FD;";
    private final String txtSub    = "-fx-text-fill: #B0BEC5; -fx-font-size: 12px;";
    private final String txtAzul   = "-fx-text-fill: #90CAF9;";
    private final DropShadow sombra = new DropShadow(12, 0, 4, Color.color(0, 0, 0, 0.45));

    public DashboardusuarioFx(Usuario usuarioLogueado, SolarService solarServicio, Connection conexionDB) {
        this.usuarioLogueado = usuarioLogueado;
        this.solarServicio   = solarServicio;
        this.conexionDB      = conexionDB;
    }

    public void mostrar(Stage stage) {
        stage.setTitle("EnergiApp — Panel de Usuario");

        String card          = "-fx-background-color: rgba(27,42,59,0.82); -fx-background-radius: 16; -fx-padding: 20;";
        String cardAccent    = "-fx-background-color: rgba(26,74,122,0.88); -fx-background-radius: 16; -fx-padding: 20;";
        String cardHighlight = "-fx-background-color: rgba(21,101,192,0.92); -fx-background-radius: 16; -fx-padding: 20;";
        String topBarStyle   = "-fx-background-color: rgba(10,22,40,0.90); -fx-background-radius: 12; -fx-padding: 14 24;"
                             + "-fx-border-color: rgba(30,58,95,0.6); -fx-border-radius: 12; -fx-border-width: 1;";
        String btnPrimario   = "-fx-background-color: #1565C0; -fx-text-fill: white; -fx-font-weight: bold;"
                             + "-fx-font-size: 13px; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 10 16;";
        String btnSecundario = "-fx-background-color: #0288D1; -fx-text-fill: white; -fx-font-weight: bold;"
                             + "-fx-font-size: 13px; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 10 16;";

        StackPane fondoPane = new StackPane();
        Rectangle gradBg = new Rectangle();
        gradBg.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#0D1B2A")), new Stop(1, Color.web("#0A1628"))));
        gradBg.widthProperty().bind(fondoPane.widthProperty());
        gradBg.heightProperty().bind(fondoPane.heightProperty());
        fondoPane.getChildren().add(gradBg);

        HBox topBar = new HBox(12);
        topBar.setStyle(topBarStyle);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setEffect(sombra);

        javafx.scene.Node logoNode;
        InputStream logoIs = getClass().getResourceAsStream("/sistemapanelessolares/imagenes/logoEnergiapp.jpeg");
        if (logoIs == null) logoIs = getClass().getResourceAsStream("/images/logoEnergiapp.jpeg");
        if (logoIs != null) {
            ImageView iv = new ImageView(new Image(logoIs));
            iv.setFitHeight(34); iv.setPreserveRatio(true);
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

        HBox selectorTabs = new HBox(8);
        selectorTabs.setPadding(new Insets(16, 24, 0, 24));
        selectorTabs.setAlignment(Pos.CENTER_LEFT);
        String btnTabNormal = "-fx-background-color: transparent; -fx-text-fill: #90CAF9; -fx-font-size: 13px;"
                + "-fx-font-weight: bold; -fx-padding: 10 20; -fx-cursor: hand; -fx-border-radius: 8; -fx-background-radius: 8;";
        String btnTabActivo = "-fx-background-color: #1565C0; -fx-text-fill: white; -fx-font-size: 13px;"
                + "-fx-font-weight: bold; -fx-padding: 10 20; -fx-cursor: hand; -fx-border-radius: 8; -fx-background-radius: 8;";
        Button btnTabDashboard = new Button("🏠 Dashboard");
        Button btnTabGraficas  = new Button("📊 Gráficas");
        btnTabDashboard.setStyle(btnTabActivo);
        btnTabGraficas.setStyle(btnTabNormal);
        aplicarHoverTab(btnTabDashboard, btnTabNormal, btnTabActivo);
        aplicarHoverTab(btnTabGraficas,  btnTabNormal, btnTabActivo);
        selectorTabs.getChildren().addAll(btnTabDashboard, btnTabGraficas);

        contenedorPestanas = new StackPane();
        VBox.setVgrow(contenedorPestanas, Priority.ALWAYS);

        panelDashboard = crearPanelDashboard(card, cardAccent, cardHighlight, btnPrimario, btnSecundario, stage);
        panelDashboard.setVisible(true); panelDashboard.setManaged(true);

        panelGraficas = crearPanelGraficas();
        panelGraficas.setVisible(false); panelGraficas.setManaged(false);

        contenedorPestanas.getChildren().addAll(panelDashboard, panelGraficas);

        btnTabDashboard.setOnAction(e -> {
            mostrarSoloPanel(panelDashboard, panelGraficas);
            btnTabDashboard.setStyle(btnTabActivo); btnTabGraficas.setStyle(btnTabNormal);
        });
        btnTabGraficas.setOnAction(e -> {
            mostrarSoloPanel(panelGraficas, panelDashboard);
            btnTabGraficas.setStyle(btnTabActivo); btnTabDashboard.setStyle(btnTabNormal);
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

        refrescarCasas();
        actualizarMetricas();
    }

    private VBox crearPanelDashboard(String card, String cardAccent, String cardHighlight,
                                     String btnPrimario, String btnSecundario, Stage stage) {
        VBox contenedor = new VBox(20);
        contenedor.setPadding(new Insets(24));

        lblMetricaCasas   = metricaVal("0 Casas");
        lblMetricaPanel   = metricaVal("Sin Panel");
        lblMetricaConsumo = metricaVal("0 kWh");

        VBox c1 = metricaCard(card,          "🏠  Propiedades",   lblMetricaCasas,   "Registradas");
        VBox c2 = metricaCard(cardAccent,    "⚡  Panel Activo",  lblMetricaPanel,   "Modelo actual");
        VBox c3 = metricaCard(cardHighlight, "📊  Consumo Total", lblMetricaConsumo, "kWh estimados");
        HBox metricas = new HBox(18, c1, c2, c3);
        HBox.setHgrow(c1, Priority.ALWAYS); HBox.setHgrow(c2, Priority.ALWAYS); HBox.setHgrow(c3, Priority.ALWAYS);

        HBox contenidoPrincipal = new HBox(18);
        HBox.setHgrow(contenidoPrincipal, Priority.ALWAYS);

        // Panel izquierdo
        VBox panelInfo = new VBox(14);
        panelInfo.setStyle(card); panelInfo.setEffect(sombra);
        HBox.setHgrow(panelInfo, Priority.ALWAYS);

        Label lblInfoTitle = new Label("📋  Resumen de tu instalación");
        lblInfoTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;" + txtBlanco);
        Separator sepInfo = new Separator();
        sepInfo.setStyle("-fx-background-color: #1E3A5F;");

        Label lblCasasTitle = new Label("🏠  Propiedades registradas — Panel por propiedad");
        lblCasasTitle.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;" + txtBlanco);

        listaCasasBox = new VBox(10);
        ScrollPane scrollCasas = new ScrollPane(listaCasasBox);
        scrollCasas.setFitToWidth(true);
        scrollCasas.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollCasas.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollCasas.setPrefHeight(220);

        // Informe financiero multi-casa
        vboxInformeFinanciero = new VBox(12);
        vboxInformeFinanciero.setAlignment(Pos.CENTER);
        vboxInformeFinanciero.setStyle("-fx-background-color: rgba(13,30,50,0.50);"
                + "-fx-border-color: rgba(30,58,95,0.6); -fx-border-radius: 12;"
                + "-fx-background-radius: 12; -fx-padding: 30;");
        VBox.setVgrow(vboxInformeFinanciero, Priority.ALWAYS);
        Label lblPlaceholder    = new Label("📊  Estudio Financiero Pendiente");
        lblPlaceholder.setStyle("-fx-text-fill: #90CAF9; -fx-font-weight: bold; -fx-font-size: 14px;");
        Label lblPlaceholderSub = new Label("Selecciona un panel para cada propiedad y genera el informe.");
        lblPlaceholderSub.setStyle(txtSub); lblPlaceholderSub.setWrapText(true);
        vboxInformeFinanciero.getChildren().addAll(lblPlaceholder, lblPlaceholderSub);

        panelInfo.getChildren().addAll(lblInfoTitle, sepInfo, lblCasasTitle, scrollCasas, vboxInformeFinanciero);

        VBox acciones = crearPanelAcciones(btnPrimario, btnSecundario, card, stage);
        contenidoPrincipal.getChildren().addAll(panelInfo, acciones);
        contenedor.getChildren().addAll(metricas, contenidoPrincipal);
        VBox.setVgrow(contenidoPrincipal, Priority.ALWAYS);
        return contenedor;
    }

    private VBox crearPanelAcciones(String btnPrimario, String btnSecundario, String card, Stage stage) {
        VBox acciones = new VBox(14);
        acciones.setStyle(card); acciones.setPrefWidth(275); acciones.setEffect(sombra);

        Label lblAcc    = new Label("Operaciones");
        lblAcc.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;" + txtBlanco);
        Label lblAccSub = new Label("Gestiona tu instalación solar");
        lblAccSub.setStyle(txtSub); lblAccSub.setWrapText(true);
        Separator sep2  = new Separator();
        sep2.setStyle("-fx-background-color: #1E3A5F;");

        Button btnCasa    = boton("🏠  Registrar Casa",    btnPrimario,   sombra);
        Button btnInforme = boton("📊  Generar Informe",
                "-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-weight: bold;"
              + "-fx-font-size: 13px; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 10 16;", sombra);

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
                alerta("Error", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        btnInforme.setOnAction(e -> generarInformeTodas());

        // Botón IA
        Label icoChat  = new Label("💬"); icoChat.setStyle("-fx-font-size: 22px;");
        VBox txtChat   = new VBox(1);
        Label lblChatT = new Label("Asistente IA");
        lblChatT.setStyle("-fx-text-fill: #E3F2FD; -fx-font-size: 13px; -fx-font-weight: bold;");
        Label lblChatS = new Label("Deja un mensaje");
        lblChatS.setStyle("-fx-text-fill: #90CAF9; -fx-font-size: 10px;");
        txtChat.getChildren().addAll(lblChatT, lblChatS);
        HBox cAI = new HBox(12, icoChat, txtChat);
        cAI.setAlignment(Pos.CENTER_LEFT);
        String esAI  = "-fx-background-color: rgba(21,101,192,0.55); -fx-background-radius: 14;"
                + "-fx-border-color: rgba(100,181,246,0.40); -fx-border-radius: 14; -fx-border-width: 1; -fx-cursor: hand; -fx-padding: 11 14;";
        String esAIH = "-fx-background-color: rgba(21,101,192,0.78); -fx-background-radius: 14;"
                + "-fx-border-color: rgba(144,202,249,0.70); -fx-border-radius: 14; -fx-border-width: 1; -fx-cursor: hand; -fx-padding: 11 14;";
        Pane btnIAPane = new Pane(cAI);
        btnIAPane.setStyle(esAI); btnIAPane.setMaxWidth(Double.MAX_VALUE); btnIAPane.setPrefHeight(54);
        cAI.prefWidthProperty().bind(btnIAPane.widthProperty());
        cAI.prefHeightProperty().bind(btnIAPane.heightProperty());
        btnIAPane.setOnMouseEntered(ev -> btnIAPane.setStyle(esAIH));
        btnIAPane.setOnMouseExited(ev  -> btnIAPane.setStyle(esAI));
        btnIAPane.setOnMouseClicked(ev -> new chatBootFX(solarServicio).mostrar());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button btnCerrar = boton("🚪  Cerrar Sesión",
                "-fx-background-color: #1B2A3B; -fx-text-fill: #90CAF9; -fx-font-weight: bold;"
              + "-fx-font-size: 13px; -fx-background-radius: 10; -fx-cursor: hand;"
              + "-fx-border-color: #1E3A5F; -fx-border-radius: 10; -fx-border-width: 1; -fx-padding: 10 16;", sombra);
        btnCerrar.setOnAction(ev -> {
            try { new IngresoFX().start(stage); } catch (Exception ex) { ex.printStackTrace(); }
        });

        acciones.getChildren().addAll(lblAcc, lblAccSub, sep2, btnCasa, btnInforme, btnIAPane, spacer, btnCerrar);
        return acciones;
    }

    // ── Refrescar casas con selector de panel por cada una ────────────
    private void refrescarCasas() {
        listaCasasBox.getChildren().clear();
        List<Casa> casas = usuarioLogueado.getCasas();
        if (casas == null || casas.isEmpty()) {
            Label v = new Label("  Aún no tienes casas registradas.");
            v.setStyle(txtSub);
            listaCasasBox.getChildren().add(v);
            return;
        }

        List<PanelSolar> paneles = solarServicio.getGestorPaneles().listarPorPrecioAscendente();

        for (int i = 0; i < casas.size(); i++) {
            final int idx = i;
            Casa c = casas.get(i);

            VBox item = new VBox(8);
            item.setStyle(cardItem);
            item.setEffect(new DropShadow(6, 0, 2, Color.color(0, 0, 0, 0.3)));

            // Fila superior: número y dirección
            HBox fila1 = new HBox(8);
            Label num = new Label("#" + (i + 1));
            num.setStyle("-fx-text-fill: #64B5F6; -fx-font-weight: bold; -fx-font-size: 13px;");
            Label dir = new Label(c.getDireccion());
            dir.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;" + txtBlanco);
            fila1.getChildren().addAll(num, dir);

            Label ciudad  = new Label("📍  " + c.getCiudad());
            ciudad.setStyle(txtSub);
            Label consumo = new Label("⚡  " + String.format("%.1f", c.getConsumoMensualKWh()) + " kWh/mes");
            consumo.setStyle("-fx-text-fill: #4FC3F7; -fx-font-size: 12px;");

            // Selector de panel por casa
            HBox filaPanel = new HBox(8);
            filaPanel.setAlignment(Pos.CENTER_LEFT);
            Label lblPanelCasa = new Label("Panel:");
            lblPanelCasa.setStyle(txtSub);

            ComboBox<PanelSolar> comboPaneles = new ComboBox<>();
            comboPaneles.getItems().addAll(paneles);
            comboPaneles.setPromptText("Seleccionar panel...");
            comboPaneles.setStyle("-fx-background-color: rgba(21,101,192,0.2);"
                    + "-fx-text-fill: #E8F4FD; -fx-font-size: 11px; -fx-cursor: hand;");
            comboPaneles.setPrefWidth(220);

            // Mostrar nombre del panel en el combo
            comboPaneles.setCellFactory(lv -> new ListCell<PanelSolar>() {
                @Override protected void updateItem(PanelSolar p, boolean empty) {
                    super.updateItem(p, empty);
                    setText(empty || p == null ? null : p.getNombre() + " (" + (int)p.getPotenciaWatts() + "W)");
                }
            });
            comboPaneles.setButtonCell(new ListCell<PanelSolar>() {
                @Override protected void updateItem(PanelSolar p, boolean empty) {
                    super.updateItem(p, empty);
                    setStyle("-fx-text-fill: #E8F4FD;");
                    setText(empty || p == null ? "Seleccionar panel..." : p.getNombre());
                }
            });

            // Recuperar panel ya seleccionado si existe
            if (panelesPorCasa.containsKey(idx)) {
                comboPaneles.setValue(panelesPorCasa.get(idx));
            }

            // Label de resultado cuando hay panel seleccionado
            Label lblResultado = new Label();
            lblResultado.setStyle("-fx-text-fill: #A5D6A7; -fx-font-size: 11px;");

            if (panelesPorCasa.containsKey(idx)) {
                PanelSolar p = panelesPorCasa.get(idx);
                CalculadoraPanels calc = new CalculadoraPanels(c, p, p.getCostoInstalacion());
                lblResultado.setText("🔋  " + calc.calcularNumeroPaneles() + " paneles  |  $"
                        + String.format("%,.0f", calc.calcularCostoTotal()));
            }

            comboPaneles.setOnAction(ev -> {
                PanelSolar seleccionado = comboPaneles.getValue();
                if (seleccionado != null) {
                    panelesPorCasa.put(idx, seleccionado);
                    CalculadoraPanels calc = new CalculadoraPanels(c, seleccionado, seleccionado.getCostoInstalacion());
                    lblResultado.setText("🔋  " + calc.calcularNumeroPaneles() + " paneles  |  $"
                            + String.format("%,.0f", calc.calcularCostoTotal()));
                    actualizarMetricas();
                }
            });

            filaPanel.getChildren().addAll(lblPanelCasa, comboPaneles);
            item.getChildren().addAll(fila1, ciudad, consumo, filaPanel, lblResultado);
            listaCasasBox.getChildren().add(item);
        }
    }

    // ── Generar informe para TODAS las casas con su panel ────────────
    private void generarInformeTodas() {
        List<Casa> casas = usuarioLogueado.getCasas();
        if (casas == null || casas.isEmpty()) {
            alerta("Faltan Datos", "Registra al menos una propiedad.", Alert.AlertType.WARNING); return;
        }
        if (panelesPorCasa.isEmpty()) {
            alerta("Faltan Paneles", "Selecciona un panel para al menos una propiedad.", Alert.AlertType.WARNING); return;
        }

        vboxInformeFinanciero.getChildren().clear();
        vboxInformeFinanciero.setAlignment(Pos.TOP_LEFT);
        vboxInformeFinanciero.setSpacing(16);
        vboxInformeFinanciero.setStyle("-fx-background-color: rgba(10,24,43,0.95);"
                + "-fx-border-color: #0288D1; -fx-border-radius: 12;"
                + "-fx-background-radius: 12; -fx-padding: 20; -fx-border-width: 1.5;");

        Label titulo = new Label("PROYECCIÓN ENERGÉTICA POR PROPIEDAD");
        titulo.setStyle("-fx-text-fill: #E8F4FD; -fx-font-weight: bold; -fx-font-size: 14px;");
        Label sub = new Label("EnergiApp Engine v2.1 — Informe multi-propiedad");
        sub.setStyle("-fx-text-fill: #64B5F6; -fx-font-size: 10px;");
        Separator sep0 = new Separator(); sep0.setStyle("-fx-background-color: #1E3A5F;");
        vboxInformeFinanciero.getChildren().addAll(titulo, sub, sep0);

        double totalInversion = 0, totalAhorroMensual = 0;

        ScrollPane scrollInforme = new ScrollPane();
        scrollInforme.setFitToWidth(true);
        scrollInforme.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox contenedorCasas = new VBox(14);
        contenedorCasas.setStyle("-fx-background-color: transparent;");

        for (int i = 0; i < casas.size(); i++) {
            Casa c = casas.get(i);
            PanelSolar panel = panelesPorCasa.get(i);
            if (panel == null) continue;

            CalculadoraPanels calc = new CalculadoraPanels(c, panel, panel.getCostoInstalacion());
            int numPaneles       = calc.calcularNumeroPaneles();
            double hsp           = calc.getHorasSolEstimadas();
            double generacion    = calc.calcularGeneracionMensualKWh();
            double inversion     = calc.calcularCostoTotal();
            double ahorro        = Math.min(c.getConsumoMensualKWh(), generacion) * 1000.0;
            int meses            = (ahorro > 0) ? (int) Math.ceil(inversion / ahorro) : 0;

            totalInversion    += inversion;
            totalAhorroMensual += ahorro;

            // Tarjeta por casa
            VBox cardCasa = new VBox(8);
            cardCasa.setStyle("-fx-background-color: rgba(21,101,192,0.12);"
                    + "-fx-border-color: rgba(144,202,249,0.25); -fx-border-radius: 10;"
                    + "-fx-background-radius: 10; -fx-padding: 12;");

            Label lblCasaTit = new Label("🏠 Casa #" + (i+1) + " — " + c.getDireccion() + " (" + c.getCiudad() + ")");
            lblCasaTit.setStyle("-fx-text-fill: #90CAF9; -fx-font-weight: bold; -fx-font-size: 12px;");

            GridPane g = new GridPane(); g.setHgap(24); g.setVgap(6);
            g.add(crearItemFactura("PANEL",       panel.getNombre(),                    "#E8F4FD"), 0, 0);
            g.add(crearItemFactura("HSP",         String.format("%.1f h/día", hsp),     "#90CAF9"), 1, 0);
            g.add(crearItemFactura("PANELES",     numPaneles + " unidades",             "#E8F4FD"), 0, 1);
            g.add(crearItemFactura("GENERACIÓN",  String.format("%.0f kWh/mes", generacion), "#A5D6A7"), 1, 1);

            HBox montos = new HBox(12);
            montos.getChildren().addAll(
                crearBloqueDestacado("INVERSIÓN",     String.format("$%,.0f", inversion),   "rgba(2,136,209,0.15)", "#0288D1"),
                crearBloqueDestacado("AHORRO/MES",    String.format("$%,.0f", ahorro),       "rgba(46,125,50,0.15)", "#69F0AE"),
                crearBloqueDestacado("RETORNO",       meses > 0 ? (meses/12) + "a " + (meses%12) + "m" : "N/A",
                                                                                              "rgba(255,152,0,0.15)", "#FFB74D")
            );
            montos.getChildren().forEach(n -> HBox.setHgrow(n, Priority.ALWAYS));

            cardCasa.getChildren().addAll(lblCasaTit, g, montos);
            contenedorCasas.getChildren().add(cardCasa);
        }

        scrollInforme.setContent(contenedorCasas);
        scrollInforme.setPrefHeight(320);

        // Totales
        Separator sepTot = new Separator(); sepTot.setStyle("-fx-background-color: #1E3A5F;");
        HBox totales = new HBox(16);
        totales.getChildren().addAll(
            crearBloqueDestacado("INVERSIÓN TOTAL",  String.format("$%,.0f", totalInversion),    "rgba(2,136,209,0.2)", "#0288D1"),
            crearBloqueDestacado("AHORRO TOTAL/MES", String.format("$%,.0f", totalAhorroMensual), "rgba(46,125,50,0.2)", "#69F0AE"),
            crearBloqueDestacado("AHORRO TOTAL/AÑO", String.format("$%,.0f", totalAhorroMensual * 12), "rgba(46,125,50,0.2)", "#69F0AE")
        );
        totales.getChildren().forEach(n -> HBox.setHgrow(n, Priority.ALWAYS));

        vboxInformeFinanciero.getChildren().addAll(scrollInforme, sepTot, totales);
    }

    private ScrollPane crearPanelGraficas() {
        PestanaGraficasFX pestana = new PestanaGraficasFX(usuarioLogueado, solarServicio);
        Tab tab = pestana.crearPestanaGraficas();
        ScrollPane sp = (ScrollPane) tab.getContent();
        sp.setVisible(true); sp.setManaged(true);
        return sp;
    }

    private void actualizarMetricas() {
        int total = usuarioLogueado.getCasas() != null ? usuarioLogueado.getCasas().size() : 0;
        lblMetricaCasas.setText(total + (total == 1 ? " Casa" : " Casas"));

        if (!panelesPorCasa.isEmpty()) {
            PanelSolar p = panelesPorCasa.values().iterator().next();
            lblMetricaPanel.setText(panelesPorCasa.size() > 1
                    ? panelesPorCasa.size() + " paneles asignados" : p.getNombre());
        }
        if (usuarioLogueado.getCasas() != null) {
            double consumo = usuarioLogueado.getCasas().stream()
                    .mapToDouble(c -> c.getConsumoMensualKWh()).sum();
            lblMetricaConsumo.setText(String.format("%.0f", consumo) + " kWh");
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────
    private void mostrarSoloPanel(javafx.scene.Node visible, javafx.scene.Node... ocultos) {
        visible.setVisible(true); visible.setManaged(true);
        for (javafx.scene.Node n : ocultos) { n.setVisible(false); n.setManaged(false); }
    }
    private void aplicarHoverTab(Button btn, String normal, String activo) {
        btn.setOnMouseEntered(e -> { if (!btn.getStyle().equals(activo)) btn.setStyle(normal + "-fx-background-color: rgba(21,101,192,0.15);"); });
        btn.setOnMouseExited(e  -> { if (!btn.getStyle().equals(activo)) btn.setStyle(normal); });
    }
    private VBox crearItemFactura(String titulo, String valor, String colorHex) {
        VBox box = new VBox(2);
        Label t = new Label(titulo); t.setStyle("-fx-text-fill: #B0BEC5; -fx-font-size: 9px; -fx-font-weight: bold;");
        Label v = new Label(valor);  v.setStyle("-fx-text-fill: " + colorHex + "; -fx-font-size: 11px; -fx-font-weight: bold;");
        box.getChildren().addAll(t, v); return box;
    }
    private VBox crearBloqueDestacado(String titulo, String monto, String fondo, String borde) {
        VBox box = new VBox(4); box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: " + fondo + "; -fx-border-color: " + borde + ";"
                + "-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8 12;");
        Label t = new Label(titulo); t.setStyle("-fx-text-fill: #B0BEC5; -fx-font-size: 8px; -fx-font-weight: bold;");
        Label m = new Label(monto);  m.setStyle("-fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold;");
        box.getChildren().addAll(t, m); return box;
    }
    private Label metricaVal(String texto) {
        Label l = new Label(texto);
        l.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #E8F4FD;"); return l;
    }
    private VBox metricaCard(String estilo, String titulo, Label valor, String sub) {
        VBox v = new VBox(8); v.setStyle(estilo); v.setEffect(sombra);
        Label t  = new Label(titulo); t.setStyle(txtSub);
        Label sb = new Label(sub);    sb.setStyle(txtSub);
        v.getChildren().addAll(t, valor, sb); return v;
    }
    private Button boton(String texto, String estilo, DropShadow s) {
        Button b = new Button(texto); b.setMaxWidth(Double.MAX_VALUE); b.setPrefHeight(42);
        b.setStyle(estilo); b.setEffect(s);
        b.setOnMouseEntered(e  -> b.setStyle(estilo + "-fx-opacity:0.82;"));
        b.setOnMouseExited(e   -> b.setStyle(estilo));
        b.setOnMousePressed(e  -> b.setStyle(estilo + "-fx-scale-x:0.97;-fx-scale-y:0.97;"));
        b.setOnMouseReleased(e -> b.setStyle(estilo));
        return b;
    }
    private void alerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert a = new Alert(tipo); a.setTitle(titulo); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}