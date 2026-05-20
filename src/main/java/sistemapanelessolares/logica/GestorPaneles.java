package sistemapanelessolares.logica;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sistemapanelessolares.dominio.PanelSolar;
import sistemapanelessolares.validadores.ValidadorPanelSolar;
public class GestorPaneles {
    private List<PanelSolar> catalogo;
    private int contadorId;
 
    public GestorPaneles() {
        this.catalogo = new ArrayList<>();
        this.contadorId = 1;
        cargarPanelesPorDefecto();
    }
 
    // ----------------------------------------------------------------
    //  CRUD principal
    // ----------------------------------------------------------------
 
    /**
     * Añade un nuevo panel al catálogo tras validarlo.
     *
     * @param panel Panel a registrar (sin id; se asigna automáticamente).
     * @return El panel con id asignado.
     * @throws IllegalArgumentException si los datos son inválidos.
     */
    public PanelSolar agregarPanel(PanelSolar panel) throws IllegalArgumentException {
        ValidadorPanelSolar.validarPanel(panel);
        panel.setId(contadorId++);
        catalogo.add(panel);
        return panel;
    }
 
    /**
     * Modifica un panel existente.
     *
     * @param id          Id del panel a modificar.
     * @param panelNuevo  Objeto con los nuevos datos (el id se ignora).
     * @return El panel actualizado.
     * @throws IllegalArgumentException si los datos son inválidos o el id no existe.
     */
    public PanelSolar modificarPanel(int id, PanelSolar panelNuevo) throws IllegalArgumentException {
        ValidadorPanelSolar.validarPanel(panelNuevo);
        PanelSolar existente = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe ningún panel con id " + id + "."));
 
        existente.setNombre(panelNuevo.getNombre());
        existente.setTipo(panelNuevo.getTipo());
        existente.setPotenciaWatts(panelNuevo.getPotenciaWatts());
        existente.setEficiencia(panelNuevo.getEficiencia());
        existente.setCostoUnidad(panelNuevo.getCostoUnidad());
        existente.setCostoInstalacion(panelNuevo.getCostoInstalacion());
        existente.setGarantiaAnios(panelNuevo.getGarantiaAnios());
        existente.setDescripcion(panelNuevo.getDescripcion());
        return existente;
    }
 
    /**
     * Elimina un panel del catálogo.
     *
     * @param id Id del panel a eliminar.
     * @return true si se eliminó correctamente.
     * @throws IllegalArgumentException si el id no existe.
     */
    public boolean eliminarPanel(int id) throws IllegalArgumentException {
        PanelSolar panel = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe ningún panel con id " + id + "."));
        catalogo.remove(panel);
        return true;
    }
 
    // ----------------------------------------------------------------
    //  Consultas
    // ----------------------------------------------------------------
 
    /** Devuelve una copia de la lista completa del catálogo. */
    public List<PanelSolar> listarPaneles() {
        return new ArrayList<>(catalogo);
    }
 
    /** Busca un panel por su id. */
    public Optional<PanelSolar> buscarPorId(int id) {
        return catalogo.stream().filter(p -> p.getId() == id).findFirst();
    }
 
    /** Busca paneles cuyo tipo coincida (sin distinguir mayúsculas). */
    public List<PanelSolar> buscarPorTipo(String tipo) {
        List<PanelSolar> resultado = new ArrayList<>();
        for (PanelSolar p : catalogo) {
            if (p.getTipo().equalsIgnoreCase(tipo.trim())) {
                resultado.add(p);
            }
        }
        return resultado;
    }
 
    /** Devuelve paneles ordenados de menor a mayor costo por unidad. */
    public List<PanelSolar> listarPorPrecioAscendente() {
        List<PanelSolar> ordenada = new ArrayList<>(catalogo);
        ordenada.sort((a, b) -> Double.compare(a.getCostoUnidad(), b.getCostoUnidad()));
        return ordenada;
    }
 
    // ----------------------------------------------------------------
    //  Datos de ejemplo al iniciar
    // ----------------------------------------------------------------
 
    private void cargarPanelesPorDefecto() {
        catalogo.add(new PanelSolar(contadorId++, "SunPower Maxeon 3",
                "Monocristalino", 400, 22.6, 350.00, 80.00, "25", "Panel premium alta eficiencia"));
        catalogo.add(new PanelSolar(contadorId++, "Canadian Solar HiKu",
                "Policristalino", 370, 18.9, 210.00, 60.00, "10", "Relación costo-beneficio óptima"));
        catalogo.add(new PanelSolar(contadorId++, "First Solar Series 6",
                "Thin-Film", 420, 19.0, 275.00, 70.00, "10", "Ideal para grandes superficies planas"));
        catalogo.add(new PanelSolar(contadorId++, "LONGi Hi-MO 5",
                "PERC", 500, 21.3, 290.00, 65.00, "12", "Alta potencia por módulo"));
        catalogo.add(new PanelSolar(contadorId++, "Trina Vertex S+",
                "Bifacial", 445, 21.8, 320.00, 75.00, "15", "Captación por ambas caras del panel"));
    }

}
