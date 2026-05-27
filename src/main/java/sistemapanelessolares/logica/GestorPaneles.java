package sistemapanelessolares.logica;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sistemapanelessolares.dominio.PanelSolar;
import sistemapanelessolares.validadores.validadorePanelSolar;

public class GestorPaneles {

    private final List<PanelSolar> catalogo;
    private int contadorId;

    // ----------------------------------------------------------------
    //  Constructores
    // ----------------------------------------------------------------

    /** * Catálogo en memoria vacío por defecto.
     * Los datos iniciales o de prueba se inyectan desde afuera.
     */
    public GestorPaneles() {
        this.catalogo = new ArrayList<>();
        this.contadorId = 1;
    }

    /**
     * Catálogo inicializado desde una lista externa (por ejemplo, cargada desde BD).
     */
    public GestorPaneles(List<PanelSolar> paneles) {
        this.catalogo = new ArrayList<>();
        this.contadorId = 1;
        if (paneles != null && !paneles.isEmpty()) {
            this.catalogo.addAll(paneles);
            this.contadorId = paneles.stream()
                    .mapToInt(PanelSolar::getId)
                    .max().orElse(0) + 1;
        }
    }

    // ----------------------------------------------------------------
    //  CRUD (Lógica Pura de Negocio)
    // ----------------------------------------------------------------

    public PanelSolar agregarPanel(PanelSolar panel) throws IllegalArgumentException {
        validadorePanelSolar.validarPanel(panel);
        panel.setId(contadorId++);
        catalogo.add(panel);
        return panel;
    }

    public void agregarPanelConId(PanelSolar panel) throws IllegalArgumentException {
        validadorePanelSolar.validarPanel(panel);
        if (panel.getId() >= contadorId) {
            contadorId = panel.getId() + 1;
        }
        catalogo.add(panel);
    }

    public PanelSolar modificarPanel(int id, PanelSolar panelNuevo) throws IllegalArgumentException {
        validadorePanelSolar.validarPanel(panelNuevo);
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

    public boolean eliminarPanel(int id) throws IllegalArgumentException {
        PanelSolar panel = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe ningún panel con id " + id + "."));
        catalogo.remove(panel);
        return true;
    }

    // ----------------------------------------------------------------
    //  Consultas
    // ----------------------------------------------------------------

    public List<PanelSolar> listarPaneles() {
        return new ArrayList<>(catalogo);
    }

    public Optional<PanelSolar> buscarPorId(int id) {
        return catalogo.stream().filter(p -> p.getId() == id).findFirst();
    }

    public List<PanelSolar> buscarPorTipo(String tipo) {
        List<PanelSolar> resultado = new ArrayList<>();
        if (tipo == null) return resultado;
        
        for (PanelSolar p : catalogo) {
            if (p.getTipo() != null && p.getTipo().equalsIgnoreCase(tipo.trim())) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public List<PanelSolar> listarPorPrecioAscendente() {
        List<PanelSolar> ordenada = new ArrayList<>(catalogo);
        ordenada.sort((a, b) -> Double.compare(a.getCostoUnidad(), b.getCostoUnidad()));
        return ordenada;
    }
}