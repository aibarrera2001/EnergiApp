package sistemapanelessolares.bdd;
import sistemapanelessolares.dominio.PanelSolar;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos para la tabla {@code panel_solar}.
 * La BD guarda: modelo, potencia_w, eficiencia, costo_panel, costo_instalacion.
 * Los campos extra del dominio (tipo, garantia, descripcion) se almacenan
 * en la columna "modelo" concatenados con separador "|" para no alterar el schema.
 * Si prefieres ampliar la tabla, añade las columnas y ajusta los métodos.
 */public class panelSolarDAO {
    // ----------------------------------------------------------------
    //  CREATE
    // ----------------------------------------------------------------

    public PanelSolar guardar(PanelSolar panel) throws SQLException {
        String sql = "INSERT INTO panel_solar (modelo, potencia_w, eficiencia, costo_panel, costo_instalacion) " +
                     "VALUES (?, ?, ?, ?, ?) RETURNING id_panel";

        try (Connection con = conexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, encoderModelo(panel));
            ps.setBigDecimal(2, new java.math.BigDecimal(panel.getPotenciaWatts()));
            ps.setBigDecimal(3, new java.math.BigDecimal(panel.getEficiencia()));
            ps.setBigDecimal(4, new java.math.BigDecimal(panel.getCostoUnidad()));
            ps.setBigDecimal(5, new java.math.BigDecimal(panel.getCostoInstalacion()));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) panel.setId(rs.getInt("id_panel"));
            }
        }
        return panel;
    }

    // ----------------------------------------------------------------
    //  READ
    // ----------------------------------------------------------------

    public Optional<PanelSolar> buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM panel_solar WHERE id_panel = ?";
        try (Connection con = conexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    public List<PanelSolar> listarTodos() throws SQLException {
        String sql = "SELECT * FROM panel_solar ORDER BY costo_panel";
        List<PanelSolar> lista = new ArrayList<>();
        try (Connection con = conexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    // ----------------------------------------------------------------
    //  UPDATE
    // ----------------------------------------------------------------

    public void actualizar(PanelSolar panel) throws SQLException {
        String sql = "UPDATE panel_solar SET modelo=?, potencia_w=?, eficiencia=?, " +
                     "costo_panel=?, costo_instalacion=? WHERE id_panel=?";

        try (Connection con = conexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, encoderModelo(panel));
            ps.setBigDecimal(2, new java.math.BigDecimal(panel.getPotenciaWatts()));
            ps.setBigDecimal(3, new java.math.BigDecimal(panel.getEficiencia()));
            ps.setBigDecimal(4, new java.math.BigDecimal(panel.getCostoUnidad()));
            ps.setBigDecimal(5, new java.math.BigDecimal(panel.getCostoInstalacion()));
            ps.setInt(6, panel.getId());
            ps.executeUpdate();
        }
    }

    // ----------------------------------------------------------------
    //  DELETE
    // ----------------------------------------------------------------

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM panel_solar WHERE id_panel = ?";
        try (Connection con = conexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ----------------------------------------------------------------
    //  Encoder / Decoder  (nombre|tipo|garantia|descripcion → modelo)
    // ----------------------------------------------------------------

    /**
     * Empaqueta los campos extra del dominio en la columna "modelo".
     * Formato: "nombre|tipo|garantia|descripcion"
     */
    private String encoderModelo(PanelSolar p) {
        return String.join("|",
                safe(p.getNombre()),
                safe(p.getTipo()),
                safe(p.getGarantiaAnios()),
                safe(p.getDescripcion()));
    }

    private String safe(String s) {
        return s == null ? "" : s.replace("|", "-");
    }

    private PanelSolar mapear(ResultSet rs) throws SQLException {
        String modelo = rs.getString("modelo");
        String[] partes = modelo.split("\\|", -1);

        String nombre      = partes.length > 0 ? partes[0] : modelo;
        String tipo        = partes.length > 1 ? partes[1] : "";
        String garantia    = partes.length > 2 ? partes[2] : "";
        String descripcion = partes.length > 3 ? partes[3] : "";

        return new PanelSolar(
                rs.getInt("id_panel"),
                nombre,
                tipo,
                rs.getDouble("potencia_w"),
                rs.getDouble("eficiencia"),
                rs.getDouble("costo_panel"),
                rs.getDouble("costo_instalacion"),
                garantia,
                descripcion
        );
    }
}

