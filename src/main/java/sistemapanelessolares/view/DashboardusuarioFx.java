package sistemapanelessolares.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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

    private final Map<Integer, PanelSolar> panelesPorCasa = new HashMap<>();

    private VBox  listaCasasBox;
    private Label lblMetricaCasas, lblMetricaPanel, lblMetricaConsumo;
    private VBox  vboxInformeFinanciero;

    private static final String C_PRIMARY   = "#0D5BD7";
    private static final String C_PRIMARY_L = "#EEF4FF";
    private static final String C_BG        = "#F5F7FA";
    private static final String C_SURFACE   = "#FFFFFF";
    private static final String C_TEXT      = "#1F2937";
    private static final String C_TEXT_S    = "#6B7280";
    private static final String C_BORDER    = "#E5E7EB";
    private static final String C_SUCCESS   = "#16A34A";
    private static final String C_SUCCESS_L = "#F0FDF4";
    private static final String C_WARNING   = "#D97706";
    private static final String C_WARNING_L = "#FFFBEB";
    private static final String C_ERROR     = "#DC2626";

    public DashboardusuarioFx(Usuario u, SolarService s, Connection c) {
        this.usuarioLogueado = u; this.solarServicio = s; this.conexionDB = c;
    }

    public void mostrar(Stage stage) {
        stage.setTitle("EnergiApp — Panel de Usuario");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + C_BG + ";");
        root.setTop(construirNavbar(stage));

        // Tabs principales
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.setStyle("-fx-background-color: " + C_BG + "; -fx-tab-min-height: 42px;");

        Tab tDash = new Tab("🏠  Dashboard");
        tDash.setContent(construirDashboard(stage));

        Tab tGraf = new Tab("📊  Gráficas");
        PestanaGraficasFX pg = new PestanaGraficasFX(usuarioLogueado, solarServicio);
        tGraf.setContent(((ScrollPane)pg.crearPestanaGraficas().getContent()));

        tabs.getTabs().addAll(tDash, tGraf);
        root.setCenter(tabs);

        stage.setScene(new Scene(root, 1400, 820));
        stage.setMaximized(true);
        stage.setMinWidth(1100); stage.setMinHeight(700);
        stage.show();

        refrescarCasas();
        actualizarMetricas();
    }

    // ── NAVBAR ────────────────────────────────────────────────────────
    private HBox construirNavbar(Stage stage) {
        HBox nav = new HBox(14);
        nav.setPadding(new Insets(10, 24, 10, 24));
        nav.setAlignment(Pos.CENTER_LEFT);
        nav.setStyle("-fx-background-color:" + C_SURFACE + "; -fx-border-color: transparent transparent "
                + C_BORDER + " transparent; -fx-border-width: 0 0 1 0;");
        nav.setEffect(new DropShadow(3, 0, 1, Color.color(0,0,0,0.05)));

        // Logo
        InputStream li = getClass().getResourceAsStream("/sistemapanelessolares/resources/logo.jpeg");
        javafx.scene.Node logo;
        if (li != null) {
            ImageView iv = new ImageView(new Image(li));
            iv.setFitWidth(34); iv.setFitHeight(34); iv.setPreserveRatio(true);
            Circle cl = new Circle(17,17,17); iv.setClip(cl); logo = iv;
        } else {
            Label fb = new Label("⚡"); fb.setStyle("-fx-font-size:18px;"); logo = fb;
        }

        VBox appName = new VBox(1);
        Label lApp = new Label("EnergiApp");
        lApp.setStyle("-fx-font-size:15px; -fx-font-weight:900; -fx-text-fill:"+C_TEXT+";");
        Label lSub = new Label("Sistema de Gestión Solar");
        lSub.setStyle("-fx-font-size:10px; -fx-text-fill:"+C_TEXT_S+";");
        appName.getChildren().addAll(lApp, lSub);

        Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);

        // Avatar
        String ini = usuarioLogueado.getNombre().substring(0,1).toUpperCase();
        Label av = new Label(ini);
        av.setStyle("-fx-background-color:"+C_PRIMARY+"; -fx-text-fill:white; -fx-font-size:14px;"
                + "-fx-font-weight:bold; -fx-background-radius:18; -fx-min-width:36; -fx-min-height:36;"
                + "-fx-alignment:center; -fx-padding:0;");
        VBox uInfo = new VBox(1);
        Label uNom = new Label(usuarioLogueado.getNombre() + " " + usuarioLogueado.getApellido());
        uNom.setStyle("-fx-font-size:13px; -fx-font-weight:bold; -fx-text-fill:"+C_TEXT+";");
        Label uRol = new Label("Usuario  •  " + usuarioLogueado.getCorreo());
        uRol.setStyle("-fx-font-size:11px; -fx-text-fill:"+C_TEXT_S+";");
        uInfo.getChildren().addAll(uNom, uRol);

        Button btnSalir = new Button("Cerrar Sesión");
        btnSalir.setStyle("-fx-background-color:transparent; -fx-text-fill:"+C_ERROR+";"
                + "-fx-font-size:12px; -fx-cursor:hand; -fx-border-color:"+C_ERROR+";"
                + "-fx-border-radius:8; -fx-background-radius:8; -fx-padding:6 14;");
        btnSalir.setOnAction(e -> { try { new IngresoFX().start(stage); } catch(Exception ex){ex.printStackTrace();} });

        nav.getChildren().addAll(logo, appName, sp, av, uInfo, btnSalir);
        return nav;
    }

    // ── DASHBOARD ─────────────────────────────────────────────────────
    private SplitPane construirDashboard(Stage stage) {
        SplitPane split = new SplitPane();
        split.setStyle("-fx-background-color:"+C_BG+"; -fx-box-border: transparent;");
        split.setDividerPositions(0.67);

        // ── PANEL IZQUIERDO ───────────────────────────────────────────
        VBox left = new VBox(14);
        left.setPadding(new Insets(18, 10, 18, 18));
        left.setStyle("-fx-background-color:"+C_BG+";");

        // Métricas
        lblMetricaCasas   = new Label("0");
        lblMetricaPanel   = new Label("—");
        lblMetricaConsumo = new Label("0");
        HBox metricas = new HBox(12,
            metricaCard("🏠","Propiedades",   lblMetricaCasas,   "registradas", C_PRIMARY,   C_PRIMARY_L),
            metricaCard("⚡","Panel Activo",   lblMetricaPanel,   "asignado",    C_SUCCESS,   C_SUCCESS_L),
            metricaCard("📊","Consumo Mensual",lblMetricaConsumo, "kWh",         C_WARNING,   C_WARNING_L)
        );
        metricas.getChildren().forEach(n -> HBox.setHgrow(n, Priority.ALWAYS));

        // Sección casas
        VBox secCasas = new VBox(10);
        secCasas.setStyle("-fx-background-color:"+C_SURFACE+"; -fx-background-radius:14;"
                + "-fx-border-color:"+C_BORDER+"; -fx-border-radius:14; -fx-padding:18;");
        secCasas.setEffect(new DropShadow(5,0,1,Color.color(0,0,0,0.05)));
        VBox.setVgrow(secCasas, Priority.SOMETIMES);

        HBox casasHdr = new HBox(8); casasHdr.setAlignment(Pos.CENTER_LEFT);
        Label casasTit = new Label("🏠  Mis Propiedades");
        casasTit.setStyle("-fx-font-size:15px; -fx-font-weight:bold; -fx-text-fill:"+C_TEXT+";");
        Label casasSub = new Label("Selecciona un panel para cada propiedad");
        casasSub.setStyle("-fx-font-size:12px; -fx-text-fill:"+C_TEXT_S+";");
        Button btnAgregar = new Button("+ Agregar");
        btnAgregar.setStyle("-fx-background-color:"+C_PRIMARY_L+"; -fx-text-fill:"+C_PRIMARY+";"
                + "-fx-font-size:12px; -fx-font-weight:bold; -fx-cursor:hand;"
                + "-fx-background-radius:8; -fx-padding:5 12;");
        Region spH = new Region(); HBox.setHgrow(spH, Priority.ALWAYS);
        casasHdr.getChildren().addAll(new VBox(2,casasTit,casasSub), spH, btnAgregar);

        btnAgregar.setOnAction(e -> {
            try {
                Registro reg = new Registro(conexionDB);
                Optional<Casa> res = reg.mostrarModalRegistroCasa(usuarioLogueado.getId());
                res.ifPresent(casa -> { usuarioLogueado.agregarCasa(casa); refrescarCasas(); actualizarMetricas(); });
            } catch(Exception ex) { alerta("Error", ex.getMessage(), Alert.AlertType.ERROR); }
        });

        listaCasasBox = new VBox(10);
        ScrollPane scrollCasas = new ScrollPane(listaCasasBox);
        scrollCasas.setFitToWidth(true);
        scrollCasas.setStyle("-fx-background:transparent; -fx-background-color:transparent;");
        scrollCasas.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollCasas.setPrefHeight(260);
        VBox.setVgrow(scrollCasas, Priority.ALWAYS);

        secCasas.getChildren().addAll(casasHdr, new Separator(), scrollCasas);

        // Sección informe
        VBox secInforme = new VBox(10);
        secInforme.setStyle("-fx-background-color:"+C_SURFACE+"; -fx-background-radius:14;"
                + "-fx-border-color:"+C_BORDER+"; -fx-border-radius:14; -fx-padding:18;");
        secInforme.setEffect(new DropShadow(5,0,1,Color.color(0,0,0,0.05)));
        VBox.setVgrow(secInforme, Priority.ALWAYS);

        HBox informeHdr = new HBox(8); informeHdr.setAlignment(Pos.CENTER_LEFT);
        Label informeTit = new Label("📋  Estudio Financiero");
        informeTit.setStyle("-fx-font-size:15px; -fx-font-weight:bold; -fx-text-fill:"+C_TEXT+";");
        Button btnGenerar = new Button("Generar Informe");
        btnGenerar.setStyle("-fx-background-color:"+C_SUCCESS+"; -fx-text-fill:white;"
                + "-fx-font-size:12px; -fx-font-weight:bold; -fx-cursor:hand;"
                + "-fx-background-radius:8; -fx-padding:7 16;");
        btnGenerar.setOnAction(e -> generarInformeTodas());
        Region spI = new Region(); HBox.setHgrow(spI, Priority.ALWAYS);
        informeHdr.getChildren().addAll(informeTit, spI, btnGenerar);

        vboxInformeFinanciero = new VBox(12);
        vboxInformeFinanciero.setAlignment(Pos.CENTER);
        vboxInformeFinanciero.setPadding(new Insets(24));
        vboxInformeFinanciero.setStyle("-fx-background-color:"+C_BG+"; -fx-background-radius:10;"
                + "-fx-border-color:"+C_BORDER+"; -fx-border-radius:10;");
        VBox.setVgrow(vboxInformeFinanciero, Priority.ALWAYS);
        Label ico = new Label("📊"); ico.setStyle("-fx-font-size:36px;");
        Label msg = new Label("Selecciona un panel para cada propiedad\ny presiona 'Generar Informe'");
        msg.setStyle("-fx-font-size:13px; -fx-text-fill:"+C_TEXT_S+"; -fx-text-alignment:center;");
        msg.setWrapText(true); msg.setAlignment(Pos.CENTER);
        vboxInformeFinanciero.getChildren().addAll(ico, msg);

        secInforme.getChildren().addAll(informeHdr, new Separator(), vboxInformeFinanciero);

        left.getChildren().addAll(metricas, secCasas, secInforme);
        VBox.setVgrow(secInforme, Priority.ALWAYS);

        // ── PANEL DERECHO: OPERACIONES ────────────────────────────────
        VBox right = new VBox(14);
        right.setPadding(new Insets(18, 18, 18, 10));
        right.setStyle("-fx-background-color:"+C_BG+";");

        // Card IA
        VBox cardIA = new VBox(12);
        cardIA.setStyle("-fx-background-color:"+C_SURFACE+"; -fx-background-radius:14;"
                + "-fx-border-color:"+C_BORDER+"; -fx-border-radius:14; -fx-padding:18;");
        cardIA.setEffect(new DropShadow(5,0,1,Color.color(0,0,0,0.05)));
        Label iaTit = new Label("🤖  Asistente IA Solar");
        iaTit.setStyle("-fx-font-size:14px; -fx-font-weight:bold; -fx-text-fill:"+C_TEXT+";");
        Label iaSub = new Label("Consulta dudas sobre paneles, costos e instalaciones");
        iaSub.setStyle("-fx-font-size:12px; -fx-text-fill:"+C_TEXT_S+"; -fx-wrap-text:true;");
        iaSub.setWrapText(true);
        HBox iaStatus = new HBox(6); iaStatus.setAlignment(Pos.CENTER_LEFT);
        Label onl = new Label("●"); onl.setStyle("-fx-text-fill:"+C_SUCCESS+"; -fx-font-size:10px;");
        Label onlT = new Label("En línea"); onlT.setStyle("-fx-font-size:11px; -fx-text-fill:"+C_SUCCESS+"; -fx-font-weight:bold;");
        iaStatus.getChildren().addAll(onl, onlT);
        Button btnIA = new Button("Abrir Chat IA");
        btnIA.setMaxWidth(Double.MAX_VALUE);
        btnIA.setStyle("-fx-background-color:"+C_PRIMARY+"; -fx-text-fill:white;"
                + "-fx-font-size:13px; -fx-font-weight:bold; -fx-background-radius:10;"
                + "-fx-cursor:hand; -fx-padding:10 0;");
        btnIA.setOnAction(e -> new chatBootFX(solarServicio).mostrar());
        cardIA.getChildren().addAll(iaTit, iaSub, iaStatus, btnIA);

        // Card perfil usuario
        VBox cardPerfil = new VBox(12);
        cardPerfil.setStyle("-fx-background-color:"+C_SURFACE+"; -fx-background-radius:14;"
                + "-fx-border-color:"+C_BORDER+"; -fx-border-radius:14; -fx-padding:18;");
        cardPerfil.setEffect(new DropShadow(5,0,1,Color.color(0,0,0,0.05)));

        // Avatar grande
        Label avG = new Label(usuarioLogueado.getNombre().substring(0,1).toUpperCase());
        avG.setStyle("-fx-background-color:"+C_PRIMARY+"; -fx-text-fill:white; -fx-font-size:22px;"
                + "-fx-font-weight:bold; -fx-background-radius:28; -fx-min-width:56; -fx-min-height:56;"
                + "-fx-alignment:center;");
        HBox avBox = new HBox(avG); avBox.setAlignment(Pos.CENTER);

        Label pNom = new Label(usuarioLogueado.getNombre() + " " + usuarioLogueado.getApellido());
        pNom.setStyle("-fx-font-size:15px; -fx-font-weight:bold; -fx-text-fill:"+C_TEXT+";");
        pNom.setAlignment(Pos.CENTER); pNom.setMaxWidth(Double.MAX_VALUE);

        Separator sepP = new Separator(); sepP.setStyle("-fx-background-color:"+C_BORDER+";");

        VBox infoRows = new VBox(8);
        infoRows.getChildren().addAll(
            infoRow("✉", "Correo", usuarioLogueado.getCorreo()),
            infoRow("📱", "Teléfono", usuarioLogueado.getTelefono() != null ? usuarioLogueado.getTelefono() : "—"),
            infoRow("🏠", "Propiedades", String.valueOf(usuarioLogueado.getCasas() != null ? usuarioLogueado.getCasas().size() : 0))
        );

        cardPerfil.getChildren().addAll(avBox, pNom, sepP, infoRows);

        // Card resumen financiero (actualizable)
        VBox cardResumen = new VBox(10);
        cardResumen.setStyle("-fx-background-color:"+C_PRIMARY_L+"; -fx-background-radius:14;"
                + "-fx-border-color:rgba(13,91,215,0.2); -fx-border-radius:14; -fx-padding:16;");

        Label resTit = new Label("💡  Consejo del Día");
        resTit.setStyle("-fx-font-size:13px; -fx-font-weight:bold; -fx-text-fill:"+C_PRIMARY+";");
        Label resTxt = new Label("La región Caribe colombiana recibe hasta 6.7 horas de sol pico diarias, "
                + "lo que la convierte en una de las zonas con mayor potencial solar del país.");
        resTxt.setStyle("-fx-font-size:12px; -fx-text-fill:#374151; -fx-wrap-text:true;");
        resTxt.setWrapText(true);
        cardResumen.getChildren().addAll(resTit, resTxt);

        Region spacerR = new Region(); VBox.setVgrow(spacerR, Priority.ALWAYS);

        right.getChildren().addAll(cardIA, cardPerfil, cardResumen, spacerR);

        ScrollPane leftScroll = new ScrollPane(left);
        leftScroll.setFitToWidth(true);
        leftScroll.setStyle("-fx-background:"+C_BG+"; -fx-background-color:"+C_BG+";");
        leftScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        ScrollPane rightScroll = new ScrollPane(right);
        rightScroll.setFitToWidth(true);
        rightScroll.setStyle("-fx-background:"+C_BG+"; -fx-background-color:"+C_BG+";");
        rightScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        split.getItems().addAll(leftScroll, rightScroll);
        return split;
    }

    // ── Refrescar casas ───────────────────────────────────────────────
    private void refrescarCasas() {
        listaCasasBox.getChildren().clear();
        List<Casa> casas = usuarioLogueado.getCasas();

        if (casas == null || casas.isEmpty()) {
            VBox empty = new VBox(10); empty.setAlignment(Pos.CENTER);
            empty.setPadding(new Insets(30));
            empty.setStyle("-fx-background-color:"+C_BG+"; -fx-background-radius:10;");
            Label eIco = new Label("🏠"); eIco.setStyle("-fx-font-size:32px;");
            Label eTxt = new Label("Aún no tienes propiedades.\nHaz clic en '+ Agregar' para comenzar.");
            eTxt.setStyle("-fx-font-size:13px; -fx-text-fill:"+C_TEXT_S+"; -fx-text-alignment:center;");
            eTxt.setWrapText(true); eTxt.setAlignment(Pos.CENTER);
            empty.getChildren().addAll(eIco, eTxt);
            listaCasasBox.getChildren().add(empty);
            return;
        }

        List<PanelSolar> paneles = solarServicio.getGestorPaneles().listarPorPrecioAscendente();

        for (int i = 0; i < casas.size(); i++) {
            final int idx = i;
            Casa c = casas.get(i);

            VBox card = new VBox(12);
            card.setStyle("-fx-background-color:"+C_SURFACE+"; -fx-background-radius:12;"
                    + "-fx-border-color:"+C_BORDER+"; -fx-border-radius:12; -fx-padding:14;");
            card.setEffect(new DropShadow(4,0,1,Color.color(0,0,0,0.04)));

            // Header
            HBox hdr = new HBox(10); hdr.setAlignment(Pos.CENTER_LEFT);
            Label badge = new Label("Casa #" + (i+1));
            badge.setStyle("-fx-background-color:"+C_PRIMARY+"; -fx-text-fill:white;"
                    + "-fx-font-size:11px; -fx-font-weight:bold; -fx-padding:3 10; -fx-background-radius:20;");
            Label dirLabel = new Label(c.getDireccion());
            dirLabel.setStyle("-fx-font-size:14px; -fx-font-weight:bold; -fx-text-fill:"+C_TEXT+";");
            dirLabel.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(dirLabel, Priority.ALWAYS);
            hdr.getChildren().addAll(badge, dirLabel);

            // Info row
            HBox infoRow = new HBox(20); infoRow.setAlignment(Pos.CENTER_LEFT);
            Label ciudadL = new Label("📍 " + c.getCiudad());
            ciudadL.setStyle("-fx-font-size:12px; -fx-text-fill:"+C_TEXT_S+";");
            Label consumoL = new Label("⚡ " + String.format("%.1f", c.getConsumoMensualKWh()) + " kWh/mes");
            consumoL.setStyle("-fx-font-size:12px; -fx-text-fill:"+C_PRIMARY+"; -fx-font-weight:bold;");
            Label hspL = new Label("☀ " + String.format("%.1f", new CalculadoraPanels(c, null, 0).getHorasSolEstimadas()) + " h/día");
            hspL.setStyle("-fx-font-size:12px; -fx-text-fill:"+C_WARNING+"; -fx-font-weight:bold;");
            infoRow.getChildren().addAll(ciudadL, consumoL, hspL);

            // Panel selector
            VBox selectorBox = new VBox(6);
            Label selLabel = new Label("Panel Solar Seleccionado");
            selLabel.setStyle("-fx-font-size:11px; -fx-font-weight:bold; -fx-text-fill:"+C_TEXT_S+";");

            ComboBox<PanelSolar> combo = new ComboBox<>();
            combo.getItems().addAll(paneles);
            combo.setPromptText("— Seleccionar panel solar —");
            combo.setMaxWidth(Double.MAX_VALUE);
            combo.setStyle("-fx-background-color:"+C_SURFACE+"; -fx-border-color:"+C_BORDER+";"
                    + "-fx-border-radius:8; -fx-background-radius:8; -fx-font-size:12px;"
                    + "-fx-padding: 2 0;");

            combo.setCellFactory(lv -> new ListCell<PanelSolar>() {
                @Override protected void updateItem(PanelSolar p, boolean empty) {
                    super.updateItem(p, empty);
                    if (empty || p == null) { setText(null); return; }
                    setText(p.getNombre() + "  —  " + (int)p.getPotenciaWatts() + " W  •  η " + p.getEficiencia() + "%  •  $" + String.format("%,.0f", p.getCostoUnidad()));
                }
            });
            combo.setButtonCell(new ListCell<PanelSolar>() {
                @Override protected void updateItem(PanelSolar p, boolean empty) {
                    super.updateItem(p, empty);
                    if (empty || p == null) { setText("— Seleccionar panel solar —"); return; }
                    setText(p.getNombre() + "  (" + (int)p.getPotenciaWatts() + " W)");
                }
            });

            if (panelesPorCasa.containsKey(idx)) combo.setValue(panelesPorCasa.get(idx));

            // Resultado
            HBox resBox = new HBox(12);
            resBox.setAlignment(Pos.CENTER_LEFT);
            resBox.setVisible(panelesPorCasa.containsKey(idx));
            resBox.setManaged(panelesPorCasa.containsKey(idx));

            if (panelesPorCasa.containsKey(idx)) {
                PanelSolar p = panelesPorCasa.get(idx);
                CalculadoraPanels calc = new CalculadoraPanels(c, p, p.getCostoInstalacion());
                resBox.getChildren().addAll(
                    resChip("🔋 " + calc.calcularNumeroPaneles() + " paneles", C_PRIMARY, C_PRIMARY_L),
                    resChip("💰 $" + String.format("%,.0f", calc.calcularCostoTotal()), C_SUCCESS, C_SUCCESS_L),
                    resChip("⚡ " + String.format("%.0f", calc.calcularGeneracionMensualKWh()) + " kWh/mes", C_WARNING, C_WARNING_L)
                );
            }

            combo.setOnAction(ev -> {
                PanelSolar sel = combo.getValue();
                if (sel == null) return;
                panelesPorCasa.put(idx, sel);
                CalculadoraPanels calc = new CalculadoraPanels(c, sel, sel.getCostoInstalacion());
                resBox.getChildren().clear();
                resBox.getChildren().addAll(
                    resChip("🔋 " + calc.calcularNumeroPaneles() + " paneles", C_PRIMARY, C_PRIMARY_L),
                    resChip("💰 $" + String.format("%,.0f", calc.calcularCostoTotal()), C_SUCCESS, C_SUCCESS_L),
                    resChip("⚡ " + String.format("%.0f", calc.calcularGeneracionMensualKWh()) + " kWh/mes", C_WARNING, C_WARNING_L)
                );
                resBox.setVisible(true); resBox.setManaged(true);
                actualizarMetricas();
            });

            selectorBox.getChildren().addAll(selLabel, combo);
            card.getChildren().addAll(hdr, infoRow, new Separator(), selectorBox, resBox);
            listaCasasBox.getChildren().add(card);
        }
    }

    // ── Generar informe ───────────────────────────────────────────────
    private void generarInformeTodas() {
        List<Casa> casas = usuarioLogueado.getCasas();
        if (casas == null || casas.isEmpty()) {
            alerta("Sin propiedades", "Registra al menos una propiedad.", Alert.AlertType.WARNING); return;
        }
        if (panelesPorCasa.isEmpty()) {
            alerta("Sin paneles", "Selecciona un panel para al menos una propiedad.", Alert.AlertType.WARNING); return;
        }

        vboxInformeFinanciero.getChildren().clear();
        vboxInformeFinanciero.setAlignment(Pos.TOP_LEFT);
        vboxInformeFinanciero.setPadding(new Insets(0));
        vboxInformeFinanciero.setStyle("-fx-background-color:"+C_SURFACE+"; -fx-background-radius:10;"
                + "-fx-border-color:"+C_BORDER+"; -fx-border-radius:10;");

        double totalInv = 0, totalAhorro = 0;
        VBox contenido = new VBox(10); contenido.setPadding(new Insets(14));

        for (int i = 0; i < casas.size(); i++) {
            Casa c = casas.get(i);
            PanelSolar panel = panelesPorCasa.get(i);
            if (panel == null) continue;

            CalculadoraPanels calc = new CalculadoraPanels(c, panel, panel.getCostoInstalacion());
            int numP    = calc.calcularNumeroPaneles();
            double hsp  = calc.getHorasSolEstimadas();
            double gen  = calc.calcularGeneracionMensualKWh();
            double inv  = calc.calcularCostoTotal();
            double aho  = Math.min(c.getConsumoMensualKWh(), gen) * 1000.0;
            int meses   = aho > 0 ? (int) Math.ceil(inv / aho) : 0;
            totalInv   += inv; totalAhorro += aho;

            VBox cardCasa = new VBox(10);
            cardCasa.setStyle("-fx-background-color:"+C_BG+"; -fx-background-radius:10;"
                    + "-fx-border-color:"+C_BORDER+"; -fx-border-radius:10; -fx-padding:14;");

            // Header casa
            HBox cHdr = new HBox(10); cHdr.setAlignment(Pos.CENTER_LEFT);
            Label cBadge = new Label("Casa #"+(i+1));
            cBadge.setStyle("-fx-background-color:"+C_PRIMARY+"; -fx-text-fill:white;"
                    + "-fx-font-size:11px; -fx-font-weight:bold; -fx-padding:3 10; -fx-background-radius:20;");
            Label cDir = new Label(c.getDireccion() + "  —  " + c.getCiudad());
            cDir.setStyle("-fx-font-size:13px; -fx-font-weight:bold; -fx-text-fill:"+C_TEXT+";");
            Label cPanel = new Label("Panel: " + panel.getNombre());
            cPanel.setStyle("-fx-font-size:11px; -fx-text-fill:"+C_TEXT_S+";");
            Region spC = new Region(); HBox.setHgrow(spC, Priority.ALWAYS);
            cHdr.getChildren().addAll(cBadge, new VBox(2,cDir,cPanel), spC);

            // Grid datos
            GridPane g = new GridPane(); g.setHgap(16); g.setVgap(6);
            g.add(gridItem("Paneles",      numP + " und.",                    C_PRIMARY),  0, 0);
            g.add(gridItem("HSP",          String.format("%.1f h/día", hsp), C_WARNING),  1, 0);
            g.add(gridItem("Generación",   String.format("%.0f kWh/mes",gen), C_SUCCESS),  2, 0);
            g.add(gridItem("Retorno",      meses>0 ? (meses/12)+"a "+(meses%12)+"m":"N/A", C_TEXT_S), 3, 0);

            // Montos
            HBox montos = new HBox(10);
            montos.getChildren().addAll(
                montoCard("Inversión Total",   "$"+String.format("%,.0f",inv),  C_PRIMARY, C_PRIMARY_L),
                montoCard("Ahorro Mensual",    "$"+String.format("%,.0f",aho),  C_SUCCESS, C_SUCCESS_L),
                montoCard("Ahorro Anual",      "$"+String.format("%,.0f",aho*12), C_SUCCESS, C_SUCCESS_L)
            );
            montos.getChildren().forEach(n -> HBox.setHgrow(n, Priority.ALWAYS));

            cardCasa.getChildren().addAll(cHdr, g, montos);
            contenido.getChildren().add(cardCasa);
        }

        ScrollPane scroll = new ScrollPane(contenido);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:transparent; -fx-background-color:transparent;");
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setPrefHeight(260);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        // Totales
        HBox totales = new HBox(10);
        totales.setPadding(new Insets(12, 14, 14, 14));
        totales.setStyle("-fx-background-color:"+C_BG+"; -fx-background-radius:0 0 10 10;");
        totales.getChildren().addAll(
            montoCard("Total Inversión",   "$"+String.format("%,.0f",totalInv),     C_PRIMARY, C_PRIMARY_L),
            montoCard("Ahorro Total/Mes",  "$"+String.format("%,.0f",totalAhorro),  C_SUCCESS, C_SUCCESS_L),
            montoCard("Ahorro Total/Año",  "$"+String.format("%,.0f",totalAhorro*12),C_SUCCESS,C_SUCCESS_L)
        );
        totales.getChildren().forEach(n -> HBox.setHgrow(n, Priority.ALWAYS));

        vboxInformeFinanciero.getChildren().addAll(scroll, new Separator(), totales);
    }

    // ── Actualizar métricas ───────────────────────────────────────────
    private void actualizarMetricas() {
        int tot = usuarioLogueado.getCasas() != null ? usuarioLogueado.getCasas().size() : 0;
        lblMetricaCasas.setText(String.valueOf(tot));
        if (!panelesPorCasa.isEmpty())
            lblMetricaPanel.setText(panelesPorCasa.size() > 1
                    ? panelesPorCasa.size() + " asignados"
                    : panelesPorCasa.values().iterator().next().getNombre());
        if (usuarioLogueado.getCasas() != null) {
            double c = usuarioLogueado.getCasas().stream().mapToDouble(Casa::getConsumoMensualKWh).sum();
            lblMetricaConsumo.setText(String.format("%.0f", c));
        }
    }

    // ── Helpers UI ────────────────────────────────────────────────────
    private VBox metricaCard(String icon, String titulo, Label valor, String sub, String color, String bgColor) {
        VBox box = new VBox(6);
        box.setStyle("-fx-background-color:"+C_SURFACE+"; -fx-background-radius:14;"
                + "-fx-border-color:"+C_BORDER+"; -fx-border-left-color:"+color+";"
                + "-fx-border-radius:14; -fx-padding:16; -fx-border-width:1 1 1 4;");
        box.setEffect(new DropShadow(4,0,1,Color.color(0,0,0,0.04)));
        HBox hdr = new HBox(6); hdr.setAlignment(Pos.CENTER_LEFT);
        Label ico = new Label(icon); ico.setStyle("-fx-font-size:14px;");
        Label tit = new Label(titulo); tit.setStyle("-fx-font-size:11px; -fx-text-fill:"+C_TEXT_S+"; -fx-font-weight:bold;");
        hdr.getChildren().addAll(ico, tit);
        valor.setStyle("-fx-font-size:28px; -fx-font-weight:900; -fx-text-fill:"+color+";");
        Label subL = new Label(sub); subL.setStyle("-fx-font-size:11px; -fx-text-fill:"+C_TEXT_S+";");
        box.getChildren().addAll(hdr, valor, subL);
        return box;
    }

    private HBox infoRow(String icon, String label, String value) {
        HBox row = new HBox(10); row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 10, 6, 10));
        row.setStyle("-fx-background-color:"+C_BG+"; -fx-background-radius:8;");
        Label ico = new Label(icon); ico.setStyle("-fx-font-size:14px;");
        Label lbl = new Label(label+":"); lbl.setStyle("-fx-font-size:12px; -fx-text-fill:"+C_TEXT_S+"; -fx-min-width:70;");
        Label val = new Label(value); val.setStyle("-fx-font-size:12px; -fx-font-weight:bold; -fx-text-fill:"+C_TEXT+";");
        row.getChildren().addAll(ico, lbl, val);
        return row;
    }

    private Label resChip(String texto, String color, String bg) {
        Label l = new Label(texto);
        l.setStyle("-fx-background-color:"+bg+"; -fx-text-fill:"+color+";"
                + "-fx-font-size:11px; -fx-font-weight:bold; -fx-padding:4 10; -fx-background-radius:20;"
                + "-fx-border-color:"+color+"; -fx-border-radius:20; -fx-border-width:0.5;");
        return l;
    }

    private VBox gridItem(String titulo, String valor, String color) {
        VBox v = new VBox(2);
        Label t = new Label(titulo); t.setStyle("-fx-font-size:10px; -fx-text-fill:"+C_TEXT_S+"; -fx-font-weight:bold;");
        Label va = new Label(valor); va.setStyle("-fx-font-size:13px; -fx-font-weight:bold; -fx-text-fill:"+color+";");
        v.getChildren().addAll(t, va); return v;
    }

    private VBox montoCard(String titulo, String monto, String color, String bg) {
        VBox box = new VBox(4); box.setAlignment(Pos.CENTER_LEFT);
        box.setStyle("-fx-background-color:"+bg+"; -fx-background-radius:10;"
                + "-fx-border-color:"+color+"33; -fx-border-radius:10; -fx-padding:10 14;");
        Label t = new Label(titulo); t.setStyle("-fx-font-size:10px; -fx-text-fill:"+C_TEXT_S+"; -fx-font-weight:bold;");
        Label m = new Label(monto);  m.setStyle("-fx-font-size:16px; -fx-font-weight:bold; -fx-text-fill:"+color+";");
        box.getChildren().addAll(t, m); return box;
    }

    private void alerta(String t, String m, Alert.AlertType tipo) {
        Alert a = new Alert(tipo); a.setTitle(t); a.setHeaderText(null); a.setContentText(m); a.showAndWait();
    }
}