package sistemapanelessolares.bdd;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import sistemapanelessolares.dominio.Casa;

public class casaDAO {
    private final Connection conexionDB;

    public casaDAO(Connection conexionDB) {
        this.conexionDB = conexionDB;
    }

    // Método que inserta la casa amarrada al id_usuario de pgAdmin
    public boolean guardarCasa(Casa casa, int idUsuario) {
        String sql = "INSERT INTO casa (direccion, ciudad, consumo_mensual, latitud, longitud, id_usuario) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conexionDB.prepareStatement(sql)) {
            pstmt.setString(1, casa.getDireccion());
            pstmt.setString(2, casa.getCiudad());
            pstmt.setDouble(3, casa.getConsumoMensualKWh()); 
            pstmt.setDouble(4, casa.getLatitud());
            pstmt.setDouble(5, casa.getLongitud());
            pstmt.setInt(6, idUsuario);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(" Error en CasaDAO (guardarCasa): " + e.getMessage());
            return false;
        }
    }
}

