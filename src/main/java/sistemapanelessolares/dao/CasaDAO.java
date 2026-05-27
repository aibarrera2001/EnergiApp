/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemapanelessolares.dao;

import sistemapanelessolares.dominio.Casa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CasaDAO {

    public void guardar(Casa casa, int idUsuario) {
        String sql = "INSERT INTO casas (direccion, ciudad, consumo_mensual, latitud, longitud, id_usuario) "
                   + "VALUES (?, ?, ?, ?, ?, ?) RETURNING id_casa";
        Connection conn = ConexionDB.conectar();
        if (conn == null) {
            System.err.println("ERROR: No hay conexion a Supabase");
            return;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, casa.getDireccion());
            ps.setString(2, casa.getCiudad());
            ps.setDouble(3, casa.getConsumoMensualKWh());
            ps.setDouble(4, casa.getLatitud());
            ps.setDouble(5, casa.getLongitud());
            ps.setInt(6, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) System.out.println("Casa guardada con ID: " + rs.getInt("id_casa"));
        } catch (Exception e) {
            System.err.println("Error al guardar casa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Casa> listarPorUsuario(int idUsuario) {
        List<Casa> lista = new ArrayList<>();
        String sql = "SELECT * FROM casas WHERE id_usuario = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (Exception e) {
            System.err.println("Error al listar casas: " + e.getMessage());
        }
        return lista;
    }

    public boolean eliminar(int idCasa) {
        String sql = "DELETE FROM casas WHERE id_casa = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCasa);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error al eliminar casa: " + e.getMessage());
            return false;
        }
    }

    private Casa mapear(ResultSet rs) throws SQLException {
        return new Casa(
            rs.getString("direccion"),
            rs.getString("ciudad"),
            rs.getDouble("consumo_mensual"),
            rs.getDouble("latitud"),
            rs.getDouble("longitud")
        );
    }
}