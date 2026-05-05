package sistemapanelessolares.validadores;
import sistemapanelessolares.dominio.Usuario;
import java.util.regex.Pattern;
public class validadorUsuario {


    // Expresión regular básica para validar correos electrónicos
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";

    public static boolean validarRegistro(Usuario usuario) throws IllegalArgumentException {
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        
        if (usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío.");
        }

        if (usuario.getCorreo() == null || !Pattern.matches(EMAIL_PATTERN, usuario.getCorreo())) {
            throw new IllegalArgumentException("El formato del correo electrónico es inválido.");
        }

        if (usuario.getContrasena() == null || usuario.getContrasena().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }

        return true;
    }

    }