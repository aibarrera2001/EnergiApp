package sistemapanelessolares.dominio;
import java.util.ArrayList;
import java.util.List;

public class Usuario extends Persona {



    private String correo;
    private String contrasena;
    private List<Casa> casas = new ArrayList<>();
    private PanelSolar panelSeleccionado;

    public Usuario() {}

    public Usuario(int id, String nombre, String apellido, String telefono, String correo, String contrasena) {
        super(id, nombre, apellido, telefono);
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public void agregarCasa(Casa casa) {
        this.casas.add(casa);
    }

    // Getters y Setters
  
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
        return "Usuario{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() + '\'' +
                ", telefono=" + getTelefono() +
                ", correo='" + correo + '\'' +
                '}';
    }
}

