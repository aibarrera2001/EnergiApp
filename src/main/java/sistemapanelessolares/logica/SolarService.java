package sistemapanelessolares.logica;

import sistemapanelessolares.dominio.Casa;
import sistemapanelessolares.dominio.Usuario;
import sistemapanelessolares.dominio.PanelSolar;

public class SolarService {
public String generarResumenSolar(Usuario usuario, int indiceCasa, double costoAdicional) {
        if (usuario.getPanelSeleccionado() == null) {
            return "Error: el usuario no tiene un panel solar seleccionado.";
        }
        if (usuario.getCasas().isEmpty() || indiceCasa >= usuario.getCasas().size()) {
            return "Error: no existe una casa en el índice " + indiceCasa + ".";
        }

        Casa casa = usuario.getCasas().get(indiceCasa);
        CalculadoraPanels calc = new CalculadoraPanels(casa, usuario.getPanelSeleccionado(), costoAdicional);
        return calc.generarResumen();
    }

    public String generarResumenTodasLasCasas(Usuario usuario, double costoAdicional) {
        if (usuario.getPanelSeleccionado() == null) {
            return "Error: el usuario no tiene un panel solar seleccionado.";
        }
        if (usuario.getCasas().isEmpty()) {
            return "El usuario no tiene casas registradas.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== Resumenes solares de ").append(usuario.getNombre()).append(" ")
          .append(usuario.getApellido()).append(" ===\n\n");

        for (int i = 0; i < usuario.getCasas().size(); i++) {
            sb.append(">>> Casa ").append(i + 1).append(":\n");
            CalculadoraPanels calc = new CalculadoraPanels(usuario.getCasas().get(i), 
                                     usuario.getPanelSeleccionado(), costoAdicional);
            sb.append(calc.generarResumen()).append("\n\n");
        }
        return sb.toString();
    }

    public double getConsumoTotalMensualKWh(Usuario usuario) {
        double total = 0;
        for (Casa casa : usuario.getCasas()) {
            total += casa.getConsumoDiarioKWh() * 30;
        }
        return total;
    }
}
