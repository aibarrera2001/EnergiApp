/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemapanelessolares.dao;

import sistemapanelessolares.dominio.PanelSolar;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PanelSolarDAO {

    public List<PanelSolar> listarTodos() {
        List<PanelSolar> lista = new ArrayList<>();
        String sql = "SELECT * FROM paneles_solares";
        try (Connection conn = ConexionDB.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (Exception e) {
            System.err.println("Error al listar paneles: " + e.getMessage());
        }
        return lista;
    }

    public PanelSolar buscarPorId(int id) {
        String sql = "SELECT * FROM paneles_solares WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (Exception e) {
            System.err.println("Error al buscar panel: " + e.getMessage());
        }
        return null;
    }

    public void guardar(PanelSolar panel) {
        String sql = "INSERT INTO paneles_solares (modelo, potencia_w, eficiencia, precio, costo_instalacion) "
                   + "VALUES (?, ?, ?, ?, ?)";
        Connection conn = ConexionDB.conectar();
        if (conn == null) {
            System.err.println("ERROR: No hay conexion a Supabase");
            return;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, panel.getModelo());
            ps.setDouble(2, panel.getPotenciaW());
            ps.setDouble(3, panel.getEficiencia());
            ps.setDouble(4, panel.getCostoPorPanel());
            ps.setDouble(5, panel.getCostoInstalacion());
            ps.executeUpdate();
            System.out.println("Panel guardado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al guardar panel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM paneles_solares WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error al eliminar panel: " + e.getMessage());
            return false;
        }
    }

    private PanelSolar mapear(ResultSet rs) throws SQLException {
        return new PanelSolar(
            rs.getString("modelo"),
            rs.getDouble("potencia_w"),
            rs.getDouble("eficiencia"),
            rs.getDouble("precio"),
            rs.getDouble("costo_instalacion")
        );
    }
}