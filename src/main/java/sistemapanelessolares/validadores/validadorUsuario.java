package sistemapanelessolares.validadores;
import sistemapanelessolares.dominio.Usuario;
import java.util.regex.Pattern;
public class validadorUsuario {


    // Expresión regular básica para validar correos electrónicos
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";

    public static void validarNombre(String nombre) throws IllegalArgumentException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
    }

    public static void validarApellido(String apellido) throws IllegalArgumentException {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío.");
        }
    }

    public static void validarTelefono(String telefono) throws IllegalArgumentException {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío.");
        }
        if (!telefono.matches("\\d+")) {
            throw new IllegalArgumentException("El teléfono debe contener solo dígitos.");
        }
    }

    public static void validarCorreo(String correo) throws IllegalArgumentException {
        if (correo == null || !Pattern.matches(EMAIL_PATTERN, correo)) {
            throw new IllegalArgumentException("El formato del correo electrónico es inválido.");
        }
    }

    public static void validarContrasena(String contrasena) throws IllegalArgumentException {
        if (contrasena == null || contrasena.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }
    }

    }