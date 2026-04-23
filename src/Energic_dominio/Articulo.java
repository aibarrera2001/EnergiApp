package Energic_dominio;

import java.util.ArrayList;
import java.util.List;

public class Articulo {

    private int idArticulo;
    private String nombre;
    private double potenciaWatts;
    private String categoria;

    // FK -> Usuario
    private int idUsuario;
    private Usuario usuario;

    // Relación 1 Artículo -> N Usos
    private List<Uso> usos = new ArrayList<>();

    // Constructor vacío
    public Articulo() {}

    // Constructor completo
    public Articulo(int idArticulo, String nombre, double potenciaWatts,
                    String categoria, int idUsuario) {
        this.idArticulo = idArticulo;
        this.nombre = nombre;
        this.potenciaWatts = potenciaWatts;
        this.categoria = categoria;
        this.idUsuario = idUsuario;
    }

    // Getters y Setters
    public int getIdArticulo() { return idArticulo; }
    public void setIdArticulo(int idArticulo) { this.idArticulo = idArticulo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPotenciaWatts() { return potenciaWatts; }
    public void setPotenciaWatts(double potenciaWatts) { this.potenciaWatts = potenciaWatts; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<Uso> getUsos() { return usos; }
    public void setUsos(List<Uso> usos) { this.usos = usos; }
    public void agregarUso(Uso uso) { this.usos.add(uso); }

    @Override
    public String toString() {
        return "Articulo{" +
                "idArticulo=" + idArticulo +
                ", nombre='" + nombre + '\'' +
                ", potenciaWatts=" + potenciaWatts +
                ", categoria='" + categoria + '\'' +
                ", idUsuario=" + idUsuario +
                '}';
    }
}