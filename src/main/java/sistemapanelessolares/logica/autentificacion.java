
package sistemapanelessolares.logica;

import java.sql.Connection;
import java.util.Scanner;
import sistemapanelessolares.dominio.Usuario;
import sistemapanelessolares.bdd.usuarioDAO;

public class autentificacion {

    private final Scanner scanner;
    private final usuarioDAO usuarioDAO;

    /**
     * Constructor de la clase de inicio de sesión.
     * Recibe la conexión de la base de datos para inicializar el DAO correspondiente.
     */
    public autentificacion(Connection conexionDB) {
        this.scanner = new Scanner(System.in);
        this.usuarioDAO = new usuarioDAO(conexionDB);
    }

    /**
     * ÚNICA RESPONSABILIDAD: Gestionar el flujo interactivo de login por consola.
     * * @return El objeto Usuario con sus datos reales y su id_usuario de pgAdmin si tiene éxito; 
     * null si las credenciales son incorrectas.
     */
    public Usuario iniciarSesion() {
        System.out.println("\n--- INICIO DE SESIÓN ---");
        System.out.print("Correo electrónico: ");
        String correo = scanner.nextLine().trim();
        
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        // Le delegamos al DAO la consulta SELECT en pgAdmin
        Usuario usuarioLogueado = usuarioDAO.buscarPorCredenciales(correo, contrasena);

        if (usuarioLogueado != null) {
            System.out.println(" ¡Autenticación exitosa! Bienvenido de nuevo, " + usuarioLogueado.getNombre() + ".");
            return usuarioLogueado;
        } else {
            System.out.println(" Correo electrónico o contraseña incorrectos.");
            return null;
        }
    }
}
