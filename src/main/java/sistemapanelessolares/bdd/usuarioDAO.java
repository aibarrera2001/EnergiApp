package sistemapanelessolares.bdd;

import sistemapanelessolares.dominio.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class usuarioDAO {

    private final Connection con;

    public usuarioDAO(Connection conexionDB) {
        this.con = conexionDB;
    }

    // ----------------------------------------------------------------
    // LOGIN: Buscar por correo y contrasena
    // ----------------------------------------------------------------
    public Usuario buscarPorCredenciales(String correo, String contrasena) {
        String sql = "SELECT id_usuario, nombre, apellido, telefono, correo, contrasena "
                   + "FROM usuarios WHERE correo = ? AND contrasena = ?";
        if (this.con == null) return null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo.trim());
            ps.setString(2, contrasena);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al validar credenciales: " + e.getMessage());
        }
        return null;
    }

    // ----------------------------------------------------------------
    // CREATE
    // ----------------------------------------------------------------
    public Usuario guardar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, apellido, correo, contrasena, telefono) "
                   + "VALUES (?, ?, ?, ?, ?) RETURNING id_usuario";
        if (this.con == null) throw new SQLException("No hay conexion activa con la base de datos.");
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getContrasena());
            ps.setString(5, usuario.getTelefono());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    System.out.println("Usuario guardado en Supabase con ID: " + usuario.getIdUsuario());
                }
            }
        }
        return usuario;
    }

    // ----------------------------------------------------------------
    // READ
    // ----------------------------------------------------------------
    public Optional<Usuario> buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        if (this.con == null) return Optional.empty();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    public Optional<Usuario> buscarPorCorreo(String correo) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        if (this.con == null) return Optional.empty();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    public List<Usuario> listarTodos() throws SQLException {
        String sql = "SELECT * FROM usuarios ORDER BY id_usuario";
        List<Usuario> lista = new ArrayList<>();
        if (this.con == null) return lista;
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    // ----------------------------------------------------------------
    // UPDATE
    // ----------------------------------------------------------------
    public void actualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nombre=?, apellido=?, correo=?, "
                   + "contrasena=?, telefono=? WHERE id_usuario=?";
        if (this.con == null) return;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getContrasena());
            ps.setString(5, usuario.getTelefono());
            ps.setInt(6, usuario.getIdUsuario());
            ps.executeUpdate();
        }
    }

    // ----------------------------------------------------------------
    // DELETE
    // ----------------------------------------------------------------
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        if (this.con == null) return;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ----------------------------------------------------------------
    // Mapper
    // ----------------------------------------------------------------
    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id_usuario"),
            rs.getString("nombre"),
            rs.getString("apellido"),
            rs.getString("telefono"),
            rs.getString("correo"),
            rs.getString("contrasena")
        );
    }
}