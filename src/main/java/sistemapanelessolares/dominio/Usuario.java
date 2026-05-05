package sistemapanelessolares.dominio;
import java.util.ArrayList;
import java.util.List;

public class Usuario {


    private int idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
    private List<Casa> casas = new ArrayList<>();
    private PanelSolar panelSeleccionado;

    public Usuario() {}

    public Usuario(int idUsuario, String nombre, String apellido, String correo, String contrasena) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public void agregarCasa(Casa casa) {
        this.casas.add(casa);
    }

    // Getters y Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public List<Casa> getCasas() { return casas; }
    public void setCasas(List<Casa> casas) { this.casas = casas; }
    public PanelSolar getPanelSeleccionado() { return panelSeleccionado; }
    public void setPanelSeleccionado(PanelSolar panelSeleccionado) { this.panelSeleccionado = panelSeleccionado; }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + idUsuario + ", nombre='" + nombre + " " + apellido + '\'' + 
               ", correo='" + correo + '\'' + ", casas=" + casas.size() + '}';
    }
}

