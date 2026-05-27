/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemapanelessolares.dao;

/**
 *
 * @author Andrés
 */


import sistemapanelessolares.dominio.Administrativo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministrativoDAO {

    public void guardar(Administrativo admin) {
        String sql = "INSERT INTO administrativos (nombre, apellido, telefono, rol, departamento) "
                   + "VALUES (?, ?, ?, ?, ?) RETURNING id";
        Connection conn = ConexionDB.conectar();
        if (conn == null) {
            System.err.println("ERROR: No hay conexion a Supabase");
            return;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, admin.getNombre());
            ps.setString(2, admin.getApellido());
            ps.setString(3, admin.getTelefono());
            ps.setString(4, admin.getRol());
            ps.setString(5, admin.getDepartamento());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) System.out.println("Administrativo guardado con ID: " + rs.getInt("id"));
        } catch (Exception e) {
            System.err.println("Error al guardar administrativo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Administrativo> listarTodos() {
        List<Administrativo> lista = new ArrayList<>();
        String sql = "SELECT * FROM administrativos";
        try (Connection conn = ConexionDB.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (Exception e) {
            System.err.println("Error al listar administrativos: " + e.getMessage());
        }
        return lista;
    }

    public Administrativo buscarPorId(int id) {
        String sql = "SELECT * FROM administrativos WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (Exception e) {
            System.err.println("Error al buscar administrativo: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizar(Administrativo admin) {
        String sql = "UPDATE administrativos SET nombre=?, apellido=?, telefono=?, rol=?, departamento=? WHERE id=?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, admin.getNombre());
            ps.setString(2, admin.getApellido());
            ps.setString(3, admin.getTelefono());
            ps.setString(4, admin.getRol());
            ps.setString(5, admin.getDepartamento());
            ps.setInt(6, admin.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error al actualizar administrativo: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM administrativos WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error al eliminar administrativo: " + e.getMessage());
            return false;
        }
    }

    private Administrativo mapear(ResultSet rs) throws SQLException {
        return new Administrativo(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("apellido"),
            rs.getString("telefono"),
            rs.getString("rol"),
            rs.getString("departamento")
        );
    }
}