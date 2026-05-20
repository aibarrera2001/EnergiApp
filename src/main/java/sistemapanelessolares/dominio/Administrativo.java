package sistemapanelessolares.dominio;

import sistemapanelessolares.logica.GestorPaneles;

public class Administrativo extends Persona {

  private String rol;
    private String departamento;
    private GestorPaneles gestorPaneles;
 
    public Administrativo() {
        this.gestorPaneles = new GestorPaneles();
    }
 
    public Administrativo(int id, String nombre, String apellido, String telefono,
                          String rol, String departamento) {
        super(id, nombre, apellido, telefono);
        this.rol = rol;
        this.departamento = departamento;
        this.gestorPaneles = new GestorPaneles();
    }
 
    // ----------------------------------------------------------------
    //  Gestión de paneles — punto de entrada desde la app
    // ----------------------------------------------------------------
 
    /**
     * Abre el menú interactivo de administración de paneles.
     * Llamar desde Ingreso.java cuando el usuario autenticado sea Administrativo.
     */
    public void abrirMenuGestion() {
        MenuAdministrador menu = new MenuAdministrador(this, gestorPaneles);
        menu.iniciar();
    }
 
    /**
     * Expone el gestor para que otros servicios (e.g. SolarService)
     * puedan leer el catálogo actualizado.
     */
    public GestorPaneles getGestorPaneles() {
        return gestorPaneles;
    }
 
    // ----------------------------------------------------------------
    //  Getters y Setters originales
    // ----------------------------------------------------------------
 
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
 
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
 
    /** Código único del administrativo: rol + id heredado de Persona. */
    public String getCodigo() {
        return rol + getId();
    }
 
    @Override
    public String toString() {
        return "Administrativo{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() + '\'' +
                ", telefono='" + getTelefono() + '\'' +
                ", rol='" + rol + '\'' +
                ", departamento='" + departamento + '\'' +
                '}';
    }
}
