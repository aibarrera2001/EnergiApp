package sistemapanelessolares.logica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sistemapanelessolares.dominio.Casa;
import sistemapanelessolares.dominio.ChatBoot;
import sistemapanelessolares.dominio.PanelSolar;
import sistemapanelessolares.dominio.Usuario;
import sistemapanelessolares.bdd.HistorialChat;
import sistemapanelessolares.bdd.panelSolarDAO;
import sistemapanelessolares.view.Registro;

public class SolarService {

    private final GestorPaneles gestorPaneles;
    private final Registro registro;
    private final autentificacion autenticacion;
    private final ChatBoot chatBoot;
    private final HistorialChat historialRepo;

    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent";
    private static final String GEMINI_KEY =
            "AIzaSyDzqWnEJ88hcwYFglpoOjWgos0drWCff30";

    // ----------------------------------------------------------------
    //  Constructor sin BD — catálogo en memoria con paneles por defecto
    // ----------------------------------------------------------------

    public SolarService() {
        this.gestorPaneles = new GestorPaneles();
        cargarPanelesSemilla(this.gestorPaneles);
        
        this.registro      = new Registro(null);
        this.autenticacion = new autentificacion(null);
        this.chatBoot      = new ChatBoot(GEMINI_URL, GEMINI_KEY);
        this.historialRepo = new HistorialChat();
    }

    // ----------------------------------------------------------------
    //  Constructor con BD — catálogo cargado desde panel_solar
    // ----------------------------------------------------------------

    public SolarService(Connection conexionDB) {
        this.registro      = new Registro(conexionDB);
        this.autenticacion = new autentificacion(conexionDB);
        this.chatBoot      = new ChatBoot(GEMINI_URL, GEMINI_KEY);
        this.historialRepo = new HistorialChat();

        GestorPaneles gp = new GestorPaneles();
        try {
            panelSolarDAO repo = new panelSolarDAO();
            List<PanelSolar> panelesBD = repo.listarTodos();
            if (panelesBD != null && !panelesBD.isEmpty()) {
                gp = new GestorPaneles(panelesBD);
            } else {
                cargarPanelesSemilla(gp);
            }
        } catch (SQLException e) {
            // Manejo silencioso: si cae la BD, respaldamos con la semilla en memoria
            cargarPanelesSemilla(gp);
        }
        this.gestorPaneles = gp;
    }

    // ----------------------------------------------------------------
    //  Lógica de Catálogo
    // ----------------------------------------------------------------

    public List<PanelSolar> obtenerPanelesParaCatalogo() {
        return gestorPaneles.listarPorPrecioAscendente();
    }

    public PanelSolar buscarPanelPorId(int idSeleccionado) {
        return gestorPaneles.buscarPorId(idSeleccionado).orElse(null);
    }

    private void cargarPanelesSemilla(GestorPaneles gp) {
        try {
            gp.agregarPanel(new PanelSolar(0, "SunPower Maxeon 3", "Monocristalino", 400, 22.6, 350.00, 80.00, "25", "Panel premium alta eficiencia"));
            gp.agregarPanel(new PanelSolar(0, "Canadian Solar HiKu", "Policristalino", 370, 18.9, 210.00, 60.00, "10", "Relación costo-beneficio óptima"));
            gp.agregarPanel(new PanelSolar(0, "First Solar Series 6", "Thin-Film", 420, 19.0, 275.00, 70.00, "10", "Ideal para grandes superficies planas"));
            gp.agregarPanel(new PanelSolar(0, "LONGi Hi-MO 5", "PERC", 500, 21.3, 290.00, 65.00, "12", "Alta potencia por módulo"));
            gp.agregarPanel(new PanelSolar(0, "Trina Vertex S+", "Bifacial", 445, 21.8, 320.00, 75.00, "15", "Captación por ambas caras del panel"));
        } catch (IllegalArgumentException e) {
            // Manejo interno por si los datos semilla no pasan tus validadores estricto de paneles
        }
    }

    // ----------------------------------------------------------------
    //  Resúmenes solares
    // ----------------------------------------------------------------

    public String generarResumenSolar(Usuario usuario, int indiceCasa) {
        if (usuario.getPanelSeleccionado() == null)
            return "Error: el usuario no tiene un panel solar seleccionado.";
        if (usuario.getCasas().isEmpty() || indiceCasa >= usuario.getCasas().size())
            return "Error: no existe una casa en el índice " + indiceCasa + ".";

        Casa casa = usuario.getCasas().get(indiceCasa);
        double costoInst = usuario.getPanelSeleccionado().getCostoInstalacion();
        return new CalculadoraPanels(casa, usuario.getPanelSeleccionado(), costoInst).generarResumen();
    }

    public String generarResumenTodasLasCasas(Usuario usuario) {
        if (usuario.getPanelSeleccionado() == null)
            return "Error: el usuario no tiene un panel solar seleccionado.";
        if (usuario.getCasas().isEmpty())
            return "El usuario no tiene casas registradas.";

        double costoInst = usuario.getPanelSeleccionado().getCostoInstalacion();

        StringBuilder sb = new StringBuilder();
        sb.append("=== Resúmenes solares de ")
          .append(usuario.getNombre()).append(" ").append(usuario.getApellido())
          .append(" ===\n\n");

        for (int i = 0; i < usuario.getCasas().size(); i++) {
            sb.append(">>> Casa ").append(i + 1).append(":\n");
            sb.append(new CalculadoraPanels(
                    usuario.getCasas().get(i),
                    usuario.getPanelSeleccionado(),
                    costoInst
            ).generarResumen()).append("\n\n");
        }
        return sb.toString();
    }

    public String generarResumenTodasLasCasas(Usuario usuario, double costoAdicional) {
        return generarResumenTodasLasCasas(usuario);
    }

    public double getConsumoTotalMensualKWh(Usuario usuario) {
        double total = 0;
        for (Casa casa : usuario.getCasas()) total += casa.getConsumoDiarioKWh() * 30;
        return total;
    }

    // ----------------------------------------------------------------
    //  Historial de chat AI
    // ----------------------------------------------------------------

    public void guardarHistorialChat(int idUsuario, String pregunta, String respuesta) throws SQLException {
        historialRepo.guardar(idUsuario, pregunta, respuesta);
    }

    public List<String[]> obtenerHistorialChat(int idUsuario) throws SQLException {
        return historialRepo.listarPorUsuario(idUsuario);
    }

    // ----------------------------------------------------------------
    //  Getters
    // ----------------------------------------------------------------

    public GestorPaneles   getGestorPaneles()  { return gestorPaneles; }
    public Registro        getRegistro()       { return registro; }
    public autentificacion getAutenticacion()  { return autenticacion; }
    public ChatBoot        getChatBoot()       { return chatBoot; }
}