/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Energic_dominio;
import java.time.LocalDateTime;
/**
 *
 * @author USUARIO
 */
public class Recomendaciones_usuario {
    


    public enum Tipo {
        REDUCIR_USO,
        CAMBIAR_HABITO,
        DISPOSITIVO_EFICIENTE,
        ENERGIA_RENOVABLE
    }

    private int idRecomendacion;
    private String descripcion;
    private Tipo tipo;
    private double ahorroEstimado;
    private LocalDateTime fechaGeneracion;

    // FK -> Usuario
    private int idUsuario;
    private Usuario usuario;

    public Recomendaciones_usuario() {
    }

   

    // Constructor completo
    public Recomendaciones_usuario (int idRecomendacion, String descripcion, Tipo tipo,
                         double ahorroEstimado, int idUsuario) {
        this.idRecomendacion = idRecomendacion;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.ahorroEstimado = ahorroEstimado;
        this.idUsuario = idUsuario;
        this.fechaGeneracion = LocalDateTime.now();
    }

    // Getters y Setters
    public int getIdRecomendacion() { return idRecomendacion; }
    public void setIdRecomendacion(int idRecomendacion) { this.idRecomendacion = idRecomendacion; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Tipo getTipo() { return tipo; }
    public void setTipo(Tipo tipo) { this.tipo = tipo; }

    public double getAhorroEstimado() { return ahorroEstimado; }
    public void setAhorroEstimado(double ahorroEstimado) { this.ahorroEstimado = ahorroEstimado; }

    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    @Override
    public String toString() {
        return "Recomendacion{" +
                "idRecomendacion=" + idRecomendacion +
                ", tipo=" + tipo +
                ", ahorroEstimado=" + ahorroEstimado +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}

