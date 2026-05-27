package sistemapanelessolares.logica;

import sistemapanelessolares.dao.UsuarioDAO;
import sistemapanelessolares.dominio.Usuario;

public class UsuarioService {

    private UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario registrar(Usuario usuario) {
        if (usuarioDAO.buscarPorCorreo(usuario.getCorreo()) != null) {
            throw new IllegalArgumentException("Ya existe un usuario con el correo: " + usuario.getCorreo());
        }
        usuarioDAO.guardar(usuario);
        if (usuario.getIdUsuario() == 0) {
            System.err.println("ERROR: No se guardo en Supabase");
        } else {
            System.out.println("Guardado en Supabase con ID: " + usuario.getIdUsuario());
        }
        return usuario;
    }

    public Usuario autenticar(String correo, String contrasena) {
        Usuario usuario = usuarioDAO.buscarPorCorreo(correo);
        if (usuario != null && usuario.getContrasena().equals(contrasena)) {
            return usuario;
        }
        return null;
    }

    public Usuario buscarPorId(int id) {
        return usuarioDAO.buscarPorId(id);
    }

    public boolean actualizar(Usuario usuario) {
        return usuarioDAO.actualizar(usuario);
    }
}