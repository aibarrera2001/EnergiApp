package sistemapanelessolares.logica;

import java.sql.Connection;
import java.util.Scanner;
import sistemapanelessolares.dominio.Usuario;
import sistemapanelessolares.dao.UsuarioDAO;

public class autentificacion {

    private final Scanner scanner;
    private final UsuarioDAO usuarioDAO;

    public autentificacion(Connection conexionDB) {
        this.scanner    = new Scanner(System.in);
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario iniciarSesion() {
        System.out.println("\n--- INICIO DE SESION ---");
        System.out.print("Correo: ");
        String correo    = scanner.nextLine().trim();
        System.out.print("Contrasena: ");
        String contrasena = scanner.nextLine();

        Usuario u = usuarioDAO.buscarPorCorreo(correo);
        if (u != null && u.getContrasena().equals(contrasena)) {
            System.out.println("Autenticacion exitosa. Bienvenido, " + u.getNombre());
            return u;
        }
        System.out.println("Correo o contrasena incorrectos.");
        return null;
    }
}