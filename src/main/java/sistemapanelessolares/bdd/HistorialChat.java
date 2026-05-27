package sistemapanelessolares.bdd;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos para la tabla {@code historial_chat}.
 */

public class HistorialChat {
    // ----------------------------------------------------------------
    //  CREATE
    // ----------------------------------------------------------------

    /**
     * Guarda una pregunta y su respuesta asociada a un usuario.
     */
    public void guardar(int idUsuario, String pregunta, String respuesta) throws SQLException {
        String sql = "INSERT INTO historial_chat (pregunta, respuesta, id_usuario) VALUES (?, ?, ?)";

        try (Connection con = conexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pregunta);
            ps.setString(2, respuesta);
            ps.setInt(3, idUsuario);
            ps.executeUpdate();
        }
    }

    // ----------------------------------------------------------------
    //  READ
    // ----------------------------------------------------------------

    /**
     * Devuelve el historial de un usuario ordenado del más reciente al más antiguo.
     * Cada elemento es un String[] con [0]=fecha, [1]=pregunta, [2]=respuesta.
     */
    public List<String[]> listarPorUsuario(int idUsuario) throws SQLException {
        String sql = "SELECT fecha_consulta, pregunta, respuesta FROM historial_chat " +
                     "WHERE id_usuario = ? ORDER BY fecha_consulta DESC";
        List<String[]> lista = new ArrayList<>();

        try (Connection con = conexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new String[]{
                            rs.getTimestamp("fecha_consulta").toString(),
                            rs.getString("pregunta"),
                            rs.getString("respuesta")
                    });
                }
            }
        }
        return lista;
    }

    // ----------------------------------------------------------------
    //  DELETE
    // ----------------------------------------------------------------

    /** Elimina todo el historial de un usuario. */
    public void eliminarPorUsuario(int idUsuario) throws SQLException {
        String sql = "DELETE FROM historial_chat WHERE id_usuario = ?";
        try (Connection con = conexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        }
    }
}

