package sistemapanelessolares.logica;

import sistemapanelessolares.dominio.Casa;
import sistemapanelessolares.dominio.PanelSolar;
import sistemapanelessolares.dominio.SolarAPIUsuario;

public class CalculadoraPanels {

    private Casa casa;
    private PanelSolar panel;
    private double costoAdicionalSistema;

    public CalculadoraPanels(Casa casa, PanelSolar panel, double costoAdicionalSistema) {
        this.casa = casa;
        this.panel = panel;
        this.costoAdicionalSistema = costoAdicionalSistema;
    }

    public double getHorasSolEstimadas() {
        return SolarAPIUsuario.obtenerHorasSolPico(
                casa.getLatitud(), casa.getLongitud(), casa.getCiudad());
    }

    public int calcularNumeroPaneles() {
        if (panel == null) return 0;
        double consumoDiarioKWh = casa.getConsumoDiarioKWh();
        double horasSol = getHorasSolEstimadas();
        double eficienciaDecimal = panel.getEficiencia() / 100.0;
        double produccionPorPanel = (panel.getPotenciaWatts() / 1000.0) * horasSol * eficienciaDecimal;
        if (produccionPorPanel <= 0) return 0;
        return (int) Math.ceil(consumoDiarioKWh / produccionPorPanel);
    }

    public static int calcularPanelesParaConsumoMensual(PanelSolar panel,
            double consumoMensualKWh, double horasSolEfectivas) {
        if (panel == null) return 0;
        double consumoDiarioKWh = consumoMensualKWh / 30.0;
        double eficienciaDecimal = panel.getEficiencia() / 100.0;
        double produccionPorPanel = (panel.getPotenciaWatts() / 1000.0) * horasSolEfectivas * eficienciaDecimal;
        if (produccionPorPanel <= 0) return 0;
        return (int) Math.ceil(consumoDiarioKWh / produccionPorPanel);
    }

    public double calcularCostoTotal() {
        if (panel == null) return costoAdicionalSistema;
        int numPaneles = calcularNumeroPaneles();
        return (numPaneles * panel.getCostoUnidad()) + costoAdicionalSistema;
    }

    // ✅ NUEVO: requerido por DashboardusuarioFx
    public double calcularGeneracionMensualKWh() {
        if (panel == null) return 0;
        int numPaneles = calcularNumeroPaneles();
        double horasSol = getHorasSolEstimadas();
        double eficienciaDecimal = panel.getEficiencia() / 100.0;
        double produccionDiaria = numPaneles * (panel.getPotenciaWatts() / 1000.0) * horasSol * eficienciaDecimal;
        return produccionDiaria * 30;
    }

    public String generarResumen() {
        if (panel == null) {
            return "Resumen del sistema solar:\n- Casa: " + casa.toString()
                    + "\n No se ha seleccionado ningún panel solar.";
        }
        int numPaneles = calcularNumeroPaneles();
        double costoTotal = calcularCostoTotal();
        double horasSolUsadas = getHorasSolEstimadas();
        return "Resumen del sistema solar:\n"
                + "- Casa: " + casa.toString() + "\n"
                + "- Horas de sol estimadas: " + String.format("%.1f h", horasSolUsadas) + "\n"
                + "- Panel usado: " + panel.getNombre() + " (" + panel.getTipo() + ")\n"
                + "- Paneles necesarios: " + numPaneles + "\n"
                + "- Costo total estimado: $" + String.format("%.2f", costoTotal);
    }
}