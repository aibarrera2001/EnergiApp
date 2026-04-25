package sistemapanelessolares.logica;

import java.util.HashMap;
import java.util.Map;

import sistemapanelessolares.dominio.Casa;
import sistemapanelessolares.dominio.PanelSolar;
import sistemapanelessolares.dominio.SolarAPIUsuario;
public class CalculadoraPanels {

    private Casa casa;
    private PanelSolar panel;
    private double costoAdicionalSistema;

    private static final Map<String, Double> MAPA_HORAS_SOL = new HashMap<>();

    static {
        MAPA_HORAS_SOL.put("Valledupar", 5.8);
        MAPA_HORAS_SOL.put("Aguachica", 5.6);
        MAPA_HORAS_SOL.put("Bogota", 3.6);
        MAPA_HORAS_SOL.put("Cartagena", 6.9);
        MAPA_HORAS_SOL.put("Barranquilla", 7.0);
    }

    public CalculadoraPanels(Casa casa, PanelSolar panel, double costoAdicionalSistema) {
        this.casa = casa;
        this.panel = panel;
        this.costoAdicionalSistema = costoAdicionalSistema;
    }

   public double getHorasSolEstimadas() {
            // Si la casa tiene coordenadas configuradas (diferentes a 0)
        if (casa.getLatitud() != 0.0 || casa.getLongitud() != 0.0) {
            return SolarAPIUsuario.obtenerHorasSolPico(casa.getLatitud(), casa.getLongitud());
        }

        // Si no hay coordenadas, usamos el mapa estático de ciudades
        String ciudadClave = casa.getCiudad().trim().toLowerCase();
        for (String key : MAPA_HORAS_SOL.keySet()) {
            if (key.toLowerCase().equals(ciudadClave)) {
                return MAPA_HORAS_SOL.get(key);
            }
        }
        return 5.0;// Fallback total

        }
    public int calcularNumeroPaneles() {
        double consumoDiarioKWh = casa.getConsumoDiarioKWh();
        double horasSol = getHorasSolEstimadas();
        double produccionPorPanel = panel.produccionDiariaKWh(horasSol);

        if (produccionPorPanel <= 0) {
            return 0;
        }

        return (int) Math.ceil(consumoDiarioKWh / produccionPorPanel);
    }

    public double calcularCostoTotal() {
        int numPaneles = calcularNumeroPaneles();
        double costoPaneles = numPaneles * panel.getCostoPorPanel();
        return costoPaneles + costoAdicionalSistema;
    }

    public String generarResumen() {
        int numPaneles = calcularNumeroPaneles();
        double costoTotal = calcularCostoTotal();
        double horasSolUsadas = getHorasSolEstimadas();

        return "Resumen del sistema solar:\n" +
               "- Casa: " + casa.toString() + "\n" +
               "- Horas de sol estimadas: " + String.format("%.1f h", horasSolUsadas) + "\n" +
               "- Panel usado: " + panel.toString() + "\n" +
               "- Paneles necesarios: " + numPaneles + "\n" +
               "- Costo total estimado: $" + String.format("%.2f", costoTotal);
    }
}
