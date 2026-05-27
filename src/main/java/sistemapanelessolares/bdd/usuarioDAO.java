package sistemapanelessolares.bdd;

import sistemapanelessolares.dominio.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos para la tabla {@code usuario} en pgAdmin.
 */
public class usuarioDAO {
    
    private final Connection con;

    /**
     * Constructor que recibe la conexión activa del sistema.
     */
    public usuarioDAO(Connection conexionDB) {
        this.con = conexionDB;
    }  
    /**
     * Busca un usuario por su correo y contraseña directamente en la BD.
     * @return El usuario mapeado si coincide; null si las credenciales son incorrectas.
     */
    public Usuario buscarPorCredenciales(String correo, String contraseña) {
        String sql = "SELECT id_usuario, nombre, apellido, telefono, correo, contraseña " +
                     "FROM usuario WHERE correo = ? AND contraseña = ?";
                     
        if (this.con == null) return null;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo.trim());
            ps.setString(2, contraseña);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println(" Error al validar credenciales en la base de datos: " + e.getMessage());
        }
        return null;
    }

    // ----------------------------------------------------------------
    //  CREATE
    // ----------------------------------------------------------------

    /**
     * Inserta un nuevo usuario y asigna el id generado por la BD.
     */
    public Usuario guardar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nombre, apellido, correo, contraseña, telefono) " +
                     "VALUES (?, ?, ?, ?, ?) RETURNING id_usuario";

        if (this.con == null) {
            throw new SQLException("No hay una conexión activa con la base de datos.");
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getContraseña());
            ps.setString(5, usuario.getTelefono());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Cambiado 'setId' por 'setIdUsuario' para que coincida con tu modelo de Dominio
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                }
            }
        }
        return usuario;
    }

    // ----------------------------------------------------------------
    //  READ
    // ----------------------------------------------------------------

    public Optional<Usuario> buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        if (this.con == null) return Optional.empty();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    /** Busca usuario por correo — útil para validaciones de duplicados. */
    public Optional<Usuario> buscarPorCorreo(String correo) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE correo = ?";
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
        String sql = "SELECT * FROM usuario ORDER BY id_usuario";
        List<Usuario> lista = new ArrayList<>();
        if (this.con == null) return lista;

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ----------------------------------------------------------------
    //  UPDATE
    // ----------------------------------------------------------------

    public void actualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario SET nombre=?, apellido=?, correo=?, " +
                     "contraseña=?, telefono=? WHERE id_usuario=?";
        if (this.con == null) return;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getContraseña());
            ps.setString(5, usuario.getTelefono());
            ps.setInt(6, usuario.getIdUsuario());
            ps.executeUpdate();
        }
    }

    // ----------------------------------------------------------------
    //  DELETE
    // ----------------------------------------------------------------

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        if (this.con == null) return;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ----------------------------------------------------------------
    //  Mapper
    // ----------------------------------------------------------------

    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id_usuario"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("telefono"),
                rs.getString("correo"),
                rs.getString("contraseña")
        );
    }
}