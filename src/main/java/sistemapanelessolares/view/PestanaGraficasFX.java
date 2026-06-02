package sistemapanelessolares.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.chart.Chart;
import sistemapanelessolares.dominio.Casa;
import sistemapanelessolares.dominio.PanelSolar;
import sistemapanelessolares.dominio.Usuario;
import sistemapanelessolares.logica.GraficasService;
import sistemapanelessolares.logica.SolarService;
import sistemapanelessolares.logica.CalculadoraPanels;
import javafx.scene.Node;

public class PestanaGraficasFX {

    private final Usuario         usuario;
    private final GraficasService graficasService;

    // ── Paleta de colores (diseño claro) ─────────────────────────────
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
    private static final String C_PURPLE    = "#7C3AED";
    private static final String C_PURPLE_L  = "#F5F3FF";

    public PestanaGraficasFX(Usuario usuario, SolarService solarService) {
        this.usuario         = usuario;
        this.graficasService = new GraficasService(solarService);
    }

    // ── PESTAÑA PRINCIPAL ─────────────────────────────────────────────
    public Tab crearPestanaGraficas() {
        Tab tab = new Tab("📊 Gráficas");
        tab.setClosable(false);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: " + C_BG + "; -fx-background-color: " + C_BG + ";");

        VBox contenedor = new VBox(20);
        contenedor.setPadding(new Insets(20));
        contenedor.setStyle("-fx-background-color: " + C_BG + ";");

        // Cabecera
        HBox cabecera = construirCabecera();

        // Estadísticas rápidas con datos reales
        HBox stats = construirEstadisticas();

        // Grid de gráficas 2×2
        GridPane gridGraficas = new GridPane();
        gridGraficas.setHgap(16);
        gridGraficas.setVgap(16);

        VBox g1 = wrapGrafica(
            graficasService.crearGraficaConsumoVsGeneracion(usuario),
            "📈  Consumo vs Generación Mensual",
            "Comparativa entre lo que consume de la red y lo que generarías con paneles solares.",
            C_PRIMARY, C_PRIMARY_L);

        VBox g2 = wrapGrafica(
            graficasService.crearGraficaAhorroAcumulado(usuario),
            "💰  Ahorro Acumulado (10 años)",
            "Proyección del ahorro económico total desde el primer año de instalación.",
            C_SUCCESS, C_SUCCESS_L);

        VBox g3 = wrapGrafica(
            graficasService.crearGraficaDistribucionCostos(),
            "🧩  Distribución de Inversión",
            "Desglose porcentual del costo inicial: paneles, instalación, inversor y baterías.",
            C_WARNING, C_WARNING_L);

        VBox g4 = wrapGrafica(
            graficasService.crearGraficaComparativaEnergia(usuario),
            "⚡  Energía Semanal Comparada",
            "Comparación semanal entre energía de la red eléctrica y la generada por tus paneles.",
            C_PURPLE, C_PURPLE_L);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        gridGraficas.getColumnConstraints().addAll(col1, col2);

        gridGraficas.add(g1, 0, 0);
        gridGraficas.add(g2, 1, 0);
        gridGraficas.add(g3, 0, 1);
        gridGraficas.add(g4, 1, 1);

        // Panel informativo
        VBox panelInfo = construirPanelInfo();

        contenedor.getChildren().addAll(cabecera, stats, gridGraficas, panelInfo);
        scrollPane.setContent(contenedor);
        tab.setContent(scrollPane);
        return tab;
    }

    // ── CABECERA ──────────────────────────────────────────────────────
    private HBox construirCabecera() {
        HBox cabecera = new HBox(16);
        cabecera.setAlignment(Pos.CENTER_LEFT);
        cabecera.setPadding(new Insets(4, 0, 4, 0));

        VBox textos = new VBox(4);
        Label titulo = new Label("Panel de Análisis Gráfico");
        titulo.setStyle(
            "-fx-font-size: 22px;" +
            "-fx-font-weight: 900;" +
            "-fx-text-fill: " + C_TEXT + ";"
        );

        Label subtitulo = new Label(
            "Usuario: " + usuario.getNombre() + " " + usuario.getApellido()
            + "  •  Datos calculados con API Open-Meteo"
        );
        subtitulo.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + C_TEXT_S + ";"
        );
        textos.getChildren().addAll(titulo, subtitulo);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnActualizar = accionBtn("🔄  Actualizar", C_PRIMARY,  C_PRIMARY_L);
        Button btnExportar   = accionBtn("📥  Exportar",   C_SUCCESS,  C_SUCCESS_L);
        Button btnImprimir   = accionBtn("🖨  Imprimir",   C_TEXT_S,   C_BG);

        btnActualizar.setOnAction(e ->
            mostrarInfo("Actualización", "Gráficas actualizadas con los datos más recientes."));
        btnExportar.setOnAction(e ->
            mostrarInfo("Exportar", "Funcionalidad de exportación en desarrollo.\nPróximamente disponible en PDF y Excel."));
        btnImprimir.setOnAction(e ->
            mostrarInfo("Imprimir", "Preparando documento para impresión..."));

        HBox botones = new HBox(8, btnActualizar, btnExportar, btnImprimir);
        cabecera.getChildren().addAll(textos, spacer, botones);
        return cabecera;
    }

    // ── ESTADÍSTICAS RÁPIDAS ──────────────────────────────────────────
    private HBox construirEstadisticas() {
        double consumoMensual     = 0;
        double generacionMensual  = 0;
        double ahorroMensual      = 0;
        int    totalPaneles       = 0;

        if (usuario.getCasas() != null && !usuario.getCasas().isEmpty()
                && usuario.getPanelSeleccionado() != null) {
            PanelSolar panel = usuario.getPanelSeleccionado();
            for (Casa c : usuario.getCasas()) {
                CalculadoraPanels calc = new CalculadoraPanels(
                    c, panel, panel.getCostoInstalacion());
                consumoMensual    += c.getConsumoMensualKWh();
                generacionMensual += calc.calcularGeneracionMensualKWh();
                totalPaneles      += calc.calcularNumeroPaneles();
            }
            ahorroMensual = Math.min(consumoMensual, generacionMensual) * 1000.0;
        } else if (usuario.getCasas() != null) {
            for (Casa c : usuario.getCasas())
                consumoMensual += c.getConsumoMensualKWh();
        }

        double co2 = generacionMensual * 0.233; // kg CO₂ evitado por kWh

        HBox panel = new HBox(12);
        panel.getChildren().addAll(
            statCard("⚡", "Generación Estimada",
                generacionMensual > 0
                    ? String.format("%.0f kWh", generacionMensual) : "—",
                C_PRIMARY, C_PRIMARY_L),
            statCard("💰", "Ahorro Mensual",
                ahorroMensual > 0
                    ? "$" + String.format("%,.0f", ahorroMensual) : "—",
                C_SUCCESS, C_SUCCESS_L),
            statCard("🌍", "CO₂ Evitado/Mes",
                co2 > 0
                    ? String.format("%.1f kg", co2) : "—",
                C_WARNING, C_WARNING_L),
            statCard("🔋", "Paneles Necesarios",
                totalPaneles > 0 ? totalPaneles + " und." : "—",
                C_PURPLE, C_PURPLE_L)
        );
        panel.getChildren().forEach(n -> HBox.setHgrow((Node) n, Priority.ALWAYS));
        return panel;
    }

    // ── CONTENEDOR DE GRÁFICA ─────────────────────────────────────────
    private VBox wrapGrafica(Chart grafica, String titulo, String descripcion,
                              String color, String bgColor) {
        VBox box = new VBox(12);
        box.setStyle(
            "-fx-background-color: " + C_SURFACE + ";" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: " + C_BORDER + ";" +
            "-fx-border-left-color: " + color + ";" +
            "-fx-border-radius: 14;" +
            "-fx-padding: 18;" +
            "-fx-border-width: 1 1 1 4;"
        );
        box.setEffect(new DropShadow(5, 0, 1, Color.color(0, 0, 0, 0.05)));

        // Título
        HBox hdr = new HBox(10);
        hdr.setAlignment(Pos.CENTER_LEFT);
        Label tit = new Label(titulo);
        tit.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + C_TEXT + ";"
        );
        hdr.getChildren().add(tit);

        // Separador
        Region separador = new Region();
        separador.setPrefHeight(1);
        separador.setStyle("-fx-background-color: " + C_BORDER + ";");

        // Gráfica
        grafica.setMinHeight(280);
        grafica.setPrefHeight(300);
        grafica.setMaxWidth(Double.MAX_VALUE);
        grafica.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(grafica, Priority.ALWAYS);

        // Descripción
        Label desc = new Label(descripcion);
        desc.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-text-fill: " + C_TEXT_S + ";" +
            "-fx-wrap-text: true;"
        );
        desc.setWrapText(true);
        desc.setMaxWidth(Double.MAX_VALUE);

        box.getChildren().addAll(hdr, separador, grafica, desc);
        return box;
    }

    // ── PANEL INFORMATIVO ─────────────────────────────────────────────
    private VBox construirPanelInfo() {
        VBox panel = new VBox(14);
        panel.setStyle(
            "-fx-background-color: " + C_SURFACE + ";" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: " + C_BORDER + ";" +
            "-fx-border-radius: 14;" +
            "-fx-padding: 20;"
        );
        panel.setEffect(new DropShadow(5, 0, 1, Color.color(0, 0, 0, 0.05)));

        // Cabecera del panel
        HBox hdr = new HBox(10);
        hdr.setAlignment(Pos.CENTER_LEFT);
        Label ico = new Label("ℹ️");
        ico.setStyle("-fx-font-size: 20px;");
        VBox txtH = new VBox(2);
        Label tit = new Label("Información del Análisis");
        tit.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + C_TEXT + ";");
        Label subH = new Label("Cómo interpretar las gráficas");
        subH.setStyle("-fx-font-size: 12px; -fx-text-fill: " + C_TEXT_S + ";");
        txtH.getChildren().addAll(tit, subH);
        hdr.getChildren().addAll(ico, txtH);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: " + C_BORDER + ";");

        // Tips en grid 2×2
        GridPane tips = new GridPane();
        tips.setHgap(16);
        tips.setVgap(10);

        String[][] datos = {
            { "📈", "Consumo vs Generación",
              "Si la barra solar supera la de red, tu sistema cubre toda tu demanda." },
            { "💰", "Ahorro Acumulado",
              "El punto donde la curva supera tu inversión inicial es tu retorno de inversión." },
            { "🧩", "Distribución de Costos",
              "Los paneles representan ~55% de la inversión. La instalación el 25%." },
            { "⚡", "Energía Semanal",
              "Monitorea semanalmente para detectar variaciones de rendimiento." },
        };

        for (int i = 0; i < datos.length; i++) {
            HBox tip = new HBox(10);
            tip.setAlignment(Pos.TOP_LEFT);
            tip.setStyle(
                "-fx-background-color: " + C_BG + ";" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 12;"
            );
            Label tipIco = new Label(datos[i][0]);
            tipIco.setStyle("-fx-font-size: 18px;");

            VBox tipTxt = new VBox(3);
            Label tipTit = new Label(datos[i][1]);
            tipTit.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " + C_TEXT + ";"
            );
            Label tipDesc = new Label(datos[i][2]);
            tipDesc.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-text-fill: " + C_TEXT_S + ";" +
                "-fx-wrap-text: true;"
            );
            tipDesc.setWrapText(true);
            tipTxt.getChildren().addAll(tipTit, tipDesc);
            HBox.setHgrow(tipTxt, Priority.ALWAYS);

            tip.getChildren().addAll(tipIco, tipTxt);
            tips.add(tip, i % 2, i / 2);
        }

        ColumnConstraints cc1 = new ColumnConstraints();
        cc1.setPercentWidth(50);
        ColumnConstraints cc2 = new ColumnConstraints();
        cc2.setPercentWidth(50);
        tips.getColumnConstraints().addAll(cc1, cc2);

        // Leyenda de colores
        HBox leyenda = new HBox(20);
        leyenda.setAlignment(Pos.CENTER_LEFT);
        leyenda.setPadding(new Insets(8, 0, 0, 0));
        Label lTit = new Label("Leyenda: ");
        lTit.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + C_TEXT_S + ";");
        leyenda.getChildren().addAll(
            lTit,
            leyendaItem(C_PRIMARY, "Energía de Red"),
            leyendaItem(C_SUCCESS, "Energía Solar"),
            leyendaItem(C_WARNING, "Ahorro Estimado"),
            leyendaItem(C_PURPLE,  "Comparativa")
        );

        panel.getChildren().addAll(hdr, sep, tips, leyenda);
        return panel;
    }

    // ── HELPERS ───────────────────────────────────────────────────────

    /** Tarjeta de estadística con borde de color a la izquierda. */
    private VBox statCard(String icon, String titulo, String valor,
                           String color, String bg) {
        VBox box = new VBox(6);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setStyle(
            "-fx-background-color: " + C_SURFACE + ";" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + C_BORDER + ";" +
            "-fx-border-left-color: " + color + ";" +
            "-fx-border-radius: 12;" +
            "-fx-padding: 14 16;" +
            "-fx-border-width: 1 1 1 4;"
        );
        box.setEffect(new DropShadow(4, 0, 1, Color.color(0, 0, 0, 0.04)));

        HBox hdr = new HBox(6);
        hdr.setAlignment(Pos.CENTER_LEFT);
        Label ico = new Label(icon);
        ico.setStyle("-fx-font-size: 14px;");
        Label tit = new Label(titulo);
        tit.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-text-fill: " + C_TEXT_S + ";" +
            "-fx-font-weight: bold;"
        );
        hdr.getChildren().addAll(ico, tit);

        Label val = new Label(valor);
        val.setStyle(
            "-fx-font-size: 22px;" +
            "-fx-font-weight: 900;" +
            "-fx-text-fill: " + color + ";"
        );
        box.getChildren().addAll(hdr, val);
        return box;
    }

    /** Botón de acción con borde sutil y colores de la paleta. */
    private Button accionBtn(String texto, String color, String bg) {
        Button b = new Button(texto);
        b.setStyle(
            "-fx-background-color: " + bg + ";" +
            "-fx-text-fill: " + color + ";" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-cursor: hand;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 8 14;" +
            "-fx-border-color: " + color + "33;" +
            "-fx-border-radius: 8;"
        );
        return b;
    }

    /** Ítem de leyenda con cuadro de color y texto. */
    private HBox leyendaItem(String color, String texto) {
        HBox item = new HBox(6);
        item.setAlignment(Pos.CENTER_LEFT);
        Region cuadro = new Region();
        cuadro.setPrefSize(12, 12);
        cuadro.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-background-radius: 3;"
        );
        Label lbl = new Label(texto);
        lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: " + C_TEXT_S + ";");
        item.getChildren().addAll(cuadro, lbl);
        return item;
    }

    /** Muestra un diálogo informativo simple. */
    private void mostrarInfo(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}