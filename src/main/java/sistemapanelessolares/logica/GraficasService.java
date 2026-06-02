package sistemapanelessolares.logica;

import javafx.scene.chart.*;
import sistemapanelessolares.dominio.Casa;
import sistemapanelessolares.dominio.PanelSolar;
import sistemapanelessolares.dominio.Usuario;

public class GraficasService {

    private final SolarService solarService;

    public GraficasService(SolarService solarService) {
        this.solarService = solarService;
    }

    // ── Gráfica 1: Consumo vs Generación mensual ─────────────────────
    public Chart crearGraficaConsumoVsGeneracion(Usuario usuario) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Mes");
        yAxis.setLabel("kWh");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Consumo vs Generación");
        chart.setLegendVisible(true);

        XYChart.Series<String, Number> serieConsumo = new XYChart.Series<>();
        serieConsumo.setName("Consumo Red");
        XYChart.Series<String, Number> serieGeneracion = new XYChart.Series<>();
        serieGeneracion.setName("Generación Solar");

        String[] meses = {"Ene","Feb","Mar","Abr","May","Jun",
                          "Jul","Ago","Sep","Oct","Nov","Dic"};

        double consumoMensual = obtenerConsumoMensual(usuario);
        double generacionMensual = obtenerGeneracionMensual(usuario);

        for (String mes : meses) {
            serieConsumo.getData().add(
                    new XYChart.Data<>(mes, consumoMensual));
            serieGeneracion.getData().add(
                    new XYChart.Data<>(mes, generacionMensual));
        }

        chart.getData().addAll(serieConsumo, serieGeneracion);
        chart.setStyle("-fx-background-color: transparent;");
        return chart;
    }

    // ── Gráfica 2: Ahorro acumulado por año ──────────────────────────
    public Chart crearGraficaAhorroAcumulado(Usuario usuario) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Año");
        yAxis.setLabel("$ Ahorro");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Ahorro Acumulado");

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Ahorro $");

        double generacion = obtenerGeneracionMensual(usuario);
        double precioKWh = 1000.0;
        double ahorroMensual = generacion * precioKWh;
        double acumulado = 0;

        for (int anio = 1; anio <= 10; anio++) {
            acumulado += ahorroMensual * 12;
            serie.getData().add(new XYChart.Data<>(
                    "Año " + anio, acumulado));
        }

        chart.getData().add(serie);
        chart.setStyle("-fx-background-color: transparent;");
        return chart;
    }

    // ── Gráfica 3: Distribución de costos ────────────────────────────
    public Chart crearGraficaDistribucionCostos() {
        PieChart chart = new PieChart();
        chart.setTitle("Distribución de Costos");

        chart.getData().addAll(
                new PieChart.Data("Paneles Solares", 55),
                new PieChart.Data("Instalación", 25),
                new PieChart.Data("Inversor", 12),
                new PieChart.Data("Baterías", 8)
        );

        chart.setStyle("-fx-background-color: transparent;");
        return chart;
    }

    // ── Gráfica 4: Comparativa semanal ───────────────────────────────
    public Chart crearGraficaComparativaEnergia(Usuario usuario) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Semana");
        yAxis.setLabel("kWh");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Comparativa Semanal");

        XYChart.Series<String, Number> serieRed = new XYChart.Series<>();
        serieRed.setName("Red Eléctrica");
        XYChart.Series<String, Number> serieSolar = new XYChart.Series<>();
        serieSolar.setName("Solar");

        double consumoSemanal = obtenerConsumoMensual(usuario) / 4.0;
        double generacionSemanal = obtenerGeneracionMensual(usuario) / 4.0;

        for (int i = 1; i <= 4; i++) {
            serieRed.getData().add(
                    new XYChart.Data<>("Sem " + i, consumoSemanal));
            serieSolar.getData().add(
                    new XYChart.Data<>("Sem " + i, generacionSemanal));
        }

        chart.getData().addAll(serieRed, serieSolar);
        chart.setStyle("-fx-background-color: transparent;");
        return chart;
    }

    // ── Helpers ───────────────────────────────────────────────────────
    private double obtenerConsumoMensual(Usuario usuario) {
        if (usuario.getCasas() == null || usuario.getCasas().isEmpty()) return 300;
        return usuario.getCasas().stream()
                .mapToDouble(c -> c.getConsumoMensualKWh())
                .sum();
    }

    private double obtenerGeneracionMensual(Usuario usuario) {
    // Intenta con panelSeleccionado (compatibilidad)
    if (usuario.getPanelSeleccionado() != null
            && usuario.getCasas() != null
            && !usuario.getCasas().isEmpty()) {
        double total = 0;
        for (Casa casa : usuario.getCasas()) {
            PanelSolar panel = usuario.getPanelSeleccionado();
            total += new CalculadoraPanels(casa, panel, panel.getCostoInstalacion())
                     .calcularGeneracionMensualKWh();
        }
        return total;
    }
    // Fallback: usa el primer panel disponible del catálogo
    if (usuario.getCasas() != null && !usuario.getCasas().isEmpty()
            && solarService != null) {
        java.util.List<PanelSolar> paneles = solarService.getGestorPaneles().listarPorPrecioAscendente();
        if (!paneles.isEmpty()) {
            double total = 0;
            PanelSolar panel = paneles.get(0);
            for (Casa casa : usuario.getCasas()) {
                total += new CalculadoraPanels(casa, panel, panel.getCostoInstalacion())
                         .calcularGeneracionMensualKWh();
            }
            return total;
        }
    }
    return 250;
}
}