package sistemapanelessolares.validadores;

import sistemapanelessolares.dominio.Administrativo;

public class validadorAdministrativo {

    public static void validarRol(String rol) throws IllegalArgumentException {
        if (rol == null || rol.trim().isEmpty()) {
            throw new IllegalArgumentException("El rol no puede estar vacío.");
        }
    }

    public static void validarDepartamento(String departamento) throws IllegalArgumentException {
        if (departamento == null || departamento.trim().isEmpty()) {
            throw new IllegalArgumentException("El departamento no puede estar vacío.");
        }
    }

    public static void validarCodigo(String codigo) throws IllegalArgumentException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
    }

    public static boolean validarRegistro(Administrativo admin) throws IllegalArgumentException {
        // Validar campos heredados de Persona
        validadorUsuario.validarNombre(admin.getNombre());
        validadorUsuario.validarApellido(admin.getApellido());
        validadorUsuario.validarTelefono(admin.getTelefono());

        // Validar campos específicos de Administrativo
        validarRol(admin.getRol());
        validarDepartamento(admin.getDepartamento());
        validarCodigo(admin.getCodigo());

        return true;
    }
}