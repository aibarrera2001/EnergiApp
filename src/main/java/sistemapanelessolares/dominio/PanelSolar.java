package sistemapanelessolares.dominio;

public class PanelSolar {

    private String modelo;
    private double potenciaW;
    private double eficiencia;
    private double costoPorPanel;
    private double costoInstalacion;

    public PanelSolar(String modelo, double potenciaW, double eficiencia, double costoPorPanel, double costoInstalacion) {
        this.modelo = modelo;
        this.potenciaW = potenciaW;
        this.eficiencia = eficiencia;
        this.costoPorPanel = costoPorPanel;        this.costoInstalacion = costoInstalacion;    }

    public double produccionDiariaKWh(double horasSolEfectivas) {
        double kilowatts = 1000.0;
        double produccionSinPerdidasKWh = potenciaW * horasSolEfectivas / kilowatts;
        return produccionSinPerdidasKWh * eficiencia;
    }

    public double getPotenciaW() {
        return potenciaW;
    }

    public double getCostoPorPanel() {
        return costoPorPanel;
    }

    public double getCostoInstalacion() {
        return costoInstalacion;
    }

    @Override
    public String toString() {
        return "Panel " + modelo + ", " + potenciaW + " W, eficiencia " + (eficiencia * 100) + "%";
    }
}
