package sistemapanelessolares.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.chart.Chart;
import sistemapanelessolares.dominio.Usuario;
import sistemapanelessolares.logica.GraficasService;
import sistemapanelessolares.logica.SolarService;
import javafx.print.PrinterJob;
import javafx.scene.Node;

public class PestanaGraficasFX {
    
    private final Usuario usuario;
    private final GraficasService graficasService;
    
    private static final String FONDO_OSCURO = "#0D1B2A";
    private static final String AZUL_PRIMARIO = "#1565C0";
    private static final String AZUL_HOVER = "#1E88E5";
    private static final String TEXTO_BLANCO = "#E8F4FD";
    private static final String TEXTO_GRIS = "#B0BEC5";
    
    private static final String BTN_ACCION =
            "-fx-background-color: #1565C0; -fx-text-fill: white;" +
            "-fx-font-size: 12px; -fx-font-weight: bold;" +
            "-fx-background-radius: 10; -fx-cursor: hand;" +
            "-fx-padding: 10 20 10 20;";
    
    public PestanaGraficasFX(Usuario usuario, SolarService solarService) {
        this.usuario = usuario;
        this.graficasService = new GraficasService(solarService);
    }
    
    public Tab crearPestanaGraficas() {
        Tab tab = new Tab("📊 Gráficas");
        tab.setClosable(false);
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: " + FONDO_OSCURO + ";" +
                           "-fx-background-color: " + FONDO_OSCURO + ";");
        
        VBox contenedor = new VBox(25);
        contenedor.setPadding(new Insets(30));
        contenedor.setStyle("-fx-background-color: " + FONDO_OSCURO + ";");
        
        // Cabecera con título y botones de acción
        HBox cabecera = crearCabecera();
        
        // Grid para organizar gráficas (2x2)
        GridPane gridGraficas = new GridPane();
        gridGraficas.setHgap(20);
        gridGraficas.setVgap(20);
        gridGraficas.setPadding(new Insets(20, 0, 20, 0));
        
        // Crear las 4 gráficas
        VBox contenedorGrafica1 = crearContenedorGrafica(
            graficasService.crearGraficaConsumoVsGeneracion(usuario),
            "📈 Consumo vs Generación Mensual",
            "Análisis comparativo del consumo energético de la red eléctrica versus la generación solar mensual. " +
            "Esta gráfica te permite identificar patrones de consumo y optimizar el uso de energía solar."
        );
        
        VBox contenedorGrafica2 = crearContenedorGrafica(
            graficasService.crearGraficaAhorroAcumulado(usuario),
            "💰 Ahorro Acumulado",
            "Proyección del ahorro económico acumulado a lo largo de los años desde la instalación del sistema solar. " +
            "Visualiza el retorno de inversión y los beneficios financieros a largo plazo."
        );
        
        VBox contenedorGrafica3 = crearContenedorGrafica(
            graficasService.crearGraficaDistribucionCostos(),
            "💵 Distribución de Costos",
            "Desglose porcentual de la inversión inicial del sistema fotovoltaico. " +
            "Incluye paneles solares, instalación, inversor y sistema de almacenamiento de baterías."
        );
        
        VBox contenedorGrafica4 = crearContenedorGrafica(
            graficasService.crearGraficaComparativaEnergia(usuario),
            "⚡ Comparativa Semanal",
            "Comparación semanal entre la energía proveniente de la red eléctrica y la generada por paneles solares. " +
            "Útil para monitorear el rendimiento del sistema en períodos cortos."
        );
        
        // Organizar en grid 2x2
        gridGraficas.add(contenedorGrafica1, 0, 0);
        gridGraficas.add(contenedorGrafica2, 1, 0);
        gridGraficas.add(contenedorGrafica3, 0, 1);
        gridGraficas.add(contenedorGrafica4, 1, 1);
        
        // Configurar columnas para que se expandan proporcionalmente
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        gridGraficas.getColumnConstraints().addAll(col1, col2);
        
        // Panel de información adicional
        VBox panelInfo = crearPanelInformacion();
        
        // Panel de estadísticas rápidas
        HBox panelEstadisticas = crearPanelEstadisticas();
        
        contenedor.getChildren().addAll(cabecera, panelEstadisticas, gridGraficas, panelInfo);
        scrollPane.setContent(contenedor);
        
        tab.setContent(scrollPane);
        
        // Aplicar estilos CSS personalizados
        aplicarEstilosCSS(scrollPane);
        
        return tab;
    }
    
    private HBox crearCabecera() {
        HBox cabecera = new HBox(20);
        cabecera.setAlignment(Pos.CENTER_LEFT);
        cabecera.setPadding(new Insets(0, 0, 10, 0));
        
        VBox textos = new VBox(5);
        Label titulo = new Label("Panel de Análisis Gráfico");
        titulo.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + TEXTO_BLANCO + ";"
        );
        titulo.setEffect(new DropShadow(10, Color.web(AZUL_PRIMARIO)));
        
        Label subtitulo = new Label("Usuario: " + usuario.getNombre() + " " + usuario.getApellido());
        subtitulo.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: " + TEXTO_GRIS + ";"
        );
        
        textos.getChildren().addAll(titulo, subtitulo);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Botones de acción
        Button btnActualizar = new Button("🔄 Actualizar");
        btnActualizar.setStyle(BTN_ACCION);
        aplicarHoverBoton(btnActualizar);
        btnActualizar.setOnAction(e -> actualizarGraficas());
        
        Button btnExportar = new Button("📥 Exportar");
        btnExportar.setStyle(BTN_ACCION);
        aplicarHoverBoton(btnExportar);
        btnExportar.setOnAction(e -> exportarGraficas());
        
        Button btnImprimir = new Button("🖨️ Imprimir");
        btnImprimir.setStyle(BTN_ACCION);
        aplicarHoverBoton(btnImprimir);
        btnImprimir.setOnAction(e -> imprimirGraficas());
        
        HBox botones = new HBox(10, btnActualizar, btnExportar, btnImprimir);
        
        cabecera.getChildren().addAll(textos, spacer, botones);
        
        return cabecera;
    }
    
    private HBox crearPanelEstadisticas() {
        HBox panel = new HBox(15);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(20));
        panel.setStyle(
            "-fx-background-color: linear-gradient(to right, rgba(21, 101, 192, 0.2), rgba(76, 175, 80, 0.2));" +
            "-fx-border-color: rgba(144, 202, 249, 0.4);" +
            "-fx-border-radius: 16;" +
            "-fx-background-radius: 16;"
        );
        panel.setEffect(new DropShadow(12, Color.color(0, 0, 0, 0.3)));
        
        VBox stat1 = crearEstadistica("⚡", "Generación Total", "1,245 kWh", "#4CAF50");
        VBox stat2 = crearEstadistica("💰", "Ahorro Mensual", "$156.50", "#FF9800");
        VBox stat3 = crearEstadistica("🌍", "CO₂ Evitado", "892 kg", "#2196F3");
        VBox stat4 = crearEstadistica("📊", "Eficiencia", "94.2%", "#9C27B0");
        
        Region spacer1 = new Region();
        Region spacer2 = new Region();
        Region spacer3 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        HBox.setHgrow(spacer3, Priority.ALWAYS);
        
        panel.getChildren().addAll(stat1, spacer1, stat2, spacer2, stat3, spacer3, stat4);
        
        return panel;
    }
    
    private VBox crearEstadistica(String icono, String titulo, String valor, String color) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        
        Label lblIcono = new Label(icono);
        lblIcono.setStyle("-fx-font-size: 32px;");
        
        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle(
            "-fx-text-fill: " + TEXTO_GRIS + ";" +
            "-fx-font-size: 11px;" +
            "-fx-font-weight: bold;"
        );
        
        Label lblValor = new Label(valor);
        lblValor.setStyle(
            "-fx-text-fill: " + color + ";" +
            "-fx-font-size: 20px;" +
            "-fx-font-weight: bold;"
        );
        
        box.getChildren().addAll(lblIcono, lblTitulo, lblValor);
        
        return box;
    }
    
    private VBox crearContenedorGrafica(Chart grafica, String titulo, String descripcion) {
        VBox contenedor = new VBox(15);
        contenedor.setStyle(
            "-fx-background-color: rgba(13, 27, 42, 0.7);" +
            "-fx-border-color: rgba(144, 202, 249, 0.3);" +
            "-fx-border-radius: 16;" +
            "-fx-background-radius: 16;" +
            "-fx-padding: 20;"
        );
        contenedor.setEffect(new DropShadow(15, Color.color(0, 0, 0, 0.5)));
        
        // Título de la gráfica
        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle(
            "-fx-text-fill: " + TEXTO_BLANCO + ";" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;"
        );
        
        // Descripción de la gráfica
        Label lblDescripcion = new Label(descripcion);
        lblDescripcion.setStyle(
            "-fx-text-fill: " + TEXTO_GRIS + ";" +
            "-fx-font-size: 11px;" +
            "-fx-font-style: italic;" +
            "-fx-wrap-text: true;"
        );
        lblDescripcion.setMaxWidth(Double.MAX_VALUE);
        lblDescripcion.setWrapText(true);
        
        // Separador
        Region separador = new Region();
        separador.setPrefHeight(1);
        separador.setStyle("-fx-background-color: rgba(144, 202, 249, 0.2);");
        
        grafica.setMinHeight(320);
        grafica.setPrefHeight(380);
        
        contenedor.getChildren().addAll(lblTitulo, separador, grafica, lblDescripcion);
        VBox.setVgrow(grafica, Priority.ALWAYS);
        
        return contenedor;
    }
    
    private VBox crearPanelInformacion() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(25));
        panel.setStyle(
            "-fx-background-color: rgba(21, 101, 192, 0.15);" +
            "-fx-border-color: rgba(144, 202, 249, 0.3);" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;"
        );
        
        HBox cabecera = new HBox(15);
        cabecera.setAlignment(Pos.CENTER_LEFT);
        
        Label icono = new Label("ℹ️");
        icono.setStyle("-fx-font-size: 28px;");
        
        Label tituloInfo = new Label("Información de Análisis");
        tituloInfo.setStyle(
            "-fx-text-fill: " + TEXTO_BLANCO + ";" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;"
        );
        
        cabecera.getChildren().addAll(icono, tituloInfo);
        
        Label descripcionInfo = new Label(
            "• Las gráficas se actualizan en tiempo real basándose en tu consumo energético registrado.\n" +
            "• Utiliza esta información para identificar patrones y optimizar tu uso de energía solar.\n" +
            "• Los datos de ahorro y generación son calculados automáticamente por el sistema.\n" +
            "• Puedes exportar o imprimir estos informes para mantener un registro histórico."
        );
        descripcionInfo.setStyle(
            "-fx-text-fill: " + TEXTO_GRIS + ";" +
            "-fx-font-size: 12px;" +
            "-fx-line-spacing: 5px;"
        );
        descripcionInfo.setWrapText(true);
        
        // Leyenda de colores
        HBox leyenda = new HBox(20);
        leyenda.setAlignment(Pos.CENTER_LEFT);
        leyenda.setPadding(new Insets(10, 0, 0, 0));
        
        HBox item1 = crearItemLeyenda("#1565C0", "Energía de Red");
        HBox item2 = crearItemLeyenda("#4CAF50", "Energía Solar");
        HBox item3 = crearItemLeyenda("#FF9800", "Ahorro");
        
        leyenda.getChildren().addAll(item1, item2, item3);
        
        panel.getChildren().addAll(cabecera, descripcionInfo, leyenda);
        
        return panel;
    }
    
    private HBox crearItemLeyenda(String color, String texto) {
        HBox item = new HBox(8);
        item.setAlignment(Pos.CENTER_LEFT);
        
        Region cuadro = new Region();
        cuadro.setPrefSize(16, 16);
        cuadro.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-background-radius: 3;"
        );
        
        Label label = new Label(texto);
        label.setStyle(
            "-fx-text-fill: " + TEXTO_BLANCO + ";" +
            "-fx-font-size: 11px;"
        );
        
        item.getChildren().addAll(cuadro, label);
        
        return item;
    }
    
    private void aplicarHoverBoton(Button btn) {
        btn.setOnMouseEntered(e -> 
            btn.setStyle(BTN_ACCION + "-fx-background-color: " + AZUL_HOVER + ";")
        );
        btn.setOnMouseExited(e -> 
            btn.setStyle(BTN_ACCION)
        );
    }
    
    private void actualizarGraficas() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Actualización");
        alert.setHeaderText(null);
        alert.setContentText("Las gráficas han sido actualizadas con los datos más recientes.");
        estilizarAlerta(alert);
        alert.showAndWait();
    }
    
    private void exportarGraficas() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exportar");
        alert.setHeaderText(null);
        alert.setContentText("Funcionalidad de exportación en desarrollo.\nPróximamente podrás exportar a PDF y Excel.");
        estilizarAlerta(alert);
        alert.showAndWait();
    }
    
    private void imprimirGraficas() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Imprimir");
        alert.setHeaderText(null);
        alert.setContentText("Preparando documento para impresión...");
        estilizarAlerta(alert);
        alert.showAndWait();
    }
    
    private void estilizarAlerta(Alert alert) {
        DialogPane dp = alert.getDialogPane();
        dp.setStyle(
            "-fx-background-color: #0D1B2A;" +
            "-fx-border-color: rgba(144,202,249,0.4);" +
            "-fx-border-width: 1; -fx-border-radius: 12; -fx-background-radius: 12;"
        );
        dp.lookup(".content.label").setStyle(
            "-fx-text-fill: #E8F4FD; -fx-font-size: 13px;"
        );
    }
    
    private void aplicarEstilosCSS(Node nodo) {
        String css = 
            ".chart {" +
            "    -fx-padding: 10px;" +
            "    -fx-background-color: rgba(13, 27, 42, 0.95);" +
            "}" +
            ".chart-plot-background {" +
            "    -fx-background-color: rgba(21, 101, 192, 0.08);" +
            "}" +
            ".chart-vertical-grid-lines, .chart-horizontal-grid-lines {" +
            "    -fx-stroke: rgba(144, 202, 249, 0.15);" +
            "}" +
            ".axis {" +
            "    -fx-tick-label-fill: #90CAF9;" +
            "    -fx-font-size: 11px;" +
            "}" +
            ".axis-label {" +
            "    -fx-text-fill: #E8F4FD;" +
            "    -fx-font-size: 12px;" +
            "    -fx-font-weight: bold;" +
            "}" +
            ".chart-title {" +
            "    -fx-text-fill: #E8F4FD;" +
            "    -fx-font-size: 14px;" +
            "    -fx-font-weight: bold;" +
            "}" +
            ".default-color0.chart-series-line {" +
            "    -fx-stroke: #1565C0;" +
            "    -fx-stroke-width: 3px;" +
            "}" +
            ".default-color0.chart-line-symbol {" +
            "    -fx-background-color: #1565C0, white;" +
            "}" +
            ".default-color0.chart-bar {" +
            "    -fx-bar-fill: #1565C0;" +
            "}" +
            ".default-color1.chart-bar {" +
            "    -fx-bar-fill: #4CAF50;" +
            "}" +
            ".default-color1.chart-series-line {" +
            "    -fx-stroke: #4CAF50;" +
            "}" +
            ".chart-legend {" +
            "    -fx-background-color: rgba(13, 27, 42, 0.8);" +
            "    -fx-border-color: rgba(144, 202, 249, 0.2);" +
            "    -fx-border-radius: 8px;" +
            "    -fx-background-radius: 8px;" +
            "    -fx-padding: 10px;" +
            "}" +
            ".chart-legend-item {" +
            "    -fx-text-fill: #E8F4FD;" +
            "}" +
            ".chart-pie-label {" +
            "    -fx-fill: #E8F4FD;" +
            "    -fx-font-size: 11px;" +
            "    -fx-font-weight: bold;" +
            "}" +
            ".chart-pie-label-line {" +
            "    -fx-stroke: #90CAF9;" +
            "    -fx-stroke-width: 1;" +
            "}";
        
        nodo.setStyle(nodo.getStyle() + css);
    }
}