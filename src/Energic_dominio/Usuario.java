package Energic_dominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Usuario {

    private int idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
    private String ciudad;
    private LocalDate fechaRegistro;

    // Relaciones (1 Usuario -> N Artículos, N Recomendaciones, etc.)
    private List<Articulo> articulos = new ArrayList<>();
    private List<Recomendaciones_usuario> recomendaciones = new ArrayList<>();
    private List<SolarEstimacion> estimacionesSolares = new ArrayList<>();
    private List<ChatSession> chatSesiones = new ArrayList<>();

    // Constructor vacío
    public Usuario() {}

    // Constructor completo
    public Usuario(int idUsuario, String nombre, String apellido,
                   String correo, String contrasena, String ciudad) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
        this.ciudad = ciudad;
        this.fechaRegistro = LocalDate.now();
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

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public List<Articulo> getArticulos() { return articulos; }
    public void setArticulos(List<Articulo> articulos) { this.articulos = articulos; }
    public void agregarArticulo(Articulo articulo) { this.articulos.add(articulo); }

    public List<Recomendaciones_usuario> getRecomendaciones() { return recomendaciones; }
    public void setRecomendaciones(List<Recomendaciones_usuario> recomendaciones) { this.recomendaciones = recomendaciones; }

    public List<SolarEstimacion> getEstimacionesSolares() { return estimacionesSolares; }
    public void setEstimacionesSolares(List<SolarEstimacion> estimacionesSolares) { this.estimacionesSolares = estimacionesSolares; }

    public List<ChatSession> getChatSesiones() { return chatSesiones; }
    public void setChatSesiones(List<ChatSession> chatSesiones) { this.chatSesiones = chatSesiones; }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", correo='" + correo + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}