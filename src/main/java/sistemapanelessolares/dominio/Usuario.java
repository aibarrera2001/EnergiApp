package sistemapanelessolares.dominio;

import java.util.ArrayList;
import java.util.List;

public class Usuario extends Persona {

    private String correo;
    private String contraseña;
    private List<Casa> casas = new ArrayList<>();
    private PanelSolar panelSeleccionado;

    /**
     * CONSTRUCTOR 1 (Sin ID): Utilizado para el registro de nuevos usuarios.
     * Envía un 0 provisional a la clase padre 'Persona'. pgAdmin asignará el ID real.
     */
    public Usuario(String nombre, String apellido, String telefono, String correo, String contraseña) {
        super(0, nombre, apellido, telefono);
        this.correo = correo;
        this.contraseña = contraseña;
    }

    /**
     * CONSTRUCTOR 2 (Con ID): Utilizado al validar las credenciales (Login).
     * Reconstruye al usuario con el identificador real obtenido de la base de datos.
     */
    public Usuario(int id, String nombre, String apellido, String telefono, String correo, String contraseña) {
        super(id, nombre, apellido, telefono);
        this.correo = correo;
        this.contraseña = contraseña;
    }

    // ----------------------------------------------------------------
    //  Métodos de comportamiento y Getters/Setters
    // ----------------------------------------------------------------

    public void agregarCasa(Casa casa) {
        this.casas.add(casa);
    }
    
    public int getIdUsuario() {
        return super.getId();
    }

    public void setIdUsuario(int idUsuario) {
        super.setId(idUsuario);
    }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }

    public List<Casa> getCasas() { return casas; }
    public void setCasas(List<Casa> casas) { this.casas = casas; }

    public PanelSolar getPanelSeleccionado() { return panelSeleccionado; }
    public void setPanelSeleccionado(PanelSolar panelSeleccionado) { this.panelSeleccionado = panelSeleccionado; }

    @Override
    public String toString() {
        return "Usuario{" +
                "id_usuario=" + getIdUsuario() +
                ", nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() + '\'' +
                ", telefono='" + getTelefono() + '\'' +
                ", correo='" + correo + '\'' +
                '}';
    }
}