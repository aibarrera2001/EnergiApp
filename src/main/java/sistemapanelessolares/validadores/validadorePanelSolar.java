package sistemapanelessolares.validadores;

import sistemapanelessolares.dominio.PanelSolar;

public class validadorePanelSolar {

    public static void validarNombre(String nombre) throws IllegalArgumentException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del panel no puede estar vacío.");
        }
    }
 
    public static void validarTipo(String tipo) throws IllegalArgumentException {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de panel no puede estar vacío.");
        }
        // Tipos válidos conocidos (extensible)
        String[] tiposValidos = {"Monocristalino", "Policristalino", "Thin-Film", "Bifacial", "PERC"};
        boolean esValido = false;
        for (String t : tiposValidos) {
            if (t.equalsIgnoreCase(tipo.trim())) {
                esValido = true;
                break;
            }
        }
        if (!esValido) {
            throw new IllegalArgumentException(
                "Tipo de panel no reconocido: '" + tipo + "'. " +
                "Tipos válidos: Monocristalino, Policristalino, Thin-Film, Bifacial, PERC."
            );
        }
    }
 
    public static void validarPotencia(double potencia) throws IllegalArgumentException {
        if (potencia <= 0) {
            throw new IllegalArgumentException("La potencia del panel debe ser mayor a 0 vatios.");
        }
        if (potencia > 1000) {
            throw new IllegalArgumentException("La potencia del panel no puede superar 1000W por unidad.");
        }
    }
 
    public static void validarEficiencia(double eficiencia) throws IllegalArgumentException {
        if (eficiencia <= 0 || eficiencia > 100) {
            throw new IllegalArgumentException("La eficiencia debe estar entre 0.1% y 100%.");
        }
    }
 
    public static void validarCosto(double costo, String campo) throws IllegalArgumentException {
        if (costo < 0) {
            throw new IllegalArgumentException("El " + campo + " no puede ser negativo.");
        }
    }
 
    public static void validarGarantia(String garantia) throws IllegalArgumentException {
        if (garantia == null || garantia.trim().isEmpty()) {
            throw new IllegalArgumentException("La garantía no puede estar vacía.");
        }
    }
 
    public static boolean validarPanel(PanelSolar panel) throws IllegalArgumentException {
        validarNombre(panel.getNombre());
        validarTipo(panel.getTipo());
        validarPotencia(panel.getPotenciaWatts());
        validarEficiencia(panel.getEficiencia());
        validarCosto(panel.getCostoUnidad(), "costo por unidad");
        validarCosto(panel.getCostoInstalacion(), "costo de instalación");
        validarGarantia(panel.getGarantiaAnios());
        return true;
    }
}

