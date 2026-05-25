package sistemapanelessolares.dominio;

public class Administrativo extends Persona {

    private String rol;
    private String departamento;

    public Administrativo() {}

    public Administrativo(int id, String nombre, String apellido, String telefono, String rol, String departamento) {
        super(id, nombre, apellido, telefono);
        this.rol = rol;
        this.departamento = departamento;
    }

    // Getters y Setters
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

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