/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Energic_dominio;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author USUARIO
 */
public class ChatSession {
    

    private int idSesion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    // FK -> Usuario
    private int idUsuario;
    private Usuario usuario;

    // Relación 1 ChatSesion -> N ChatMensajes
    private List<ChatMensaje> mensajes = new ArrayList<>();

    // Constructor vacío
    public ChatSession() {}

    // Constructor completo
    public ChatSession(int idSesion, int idUsuario) {
        this.idSesion = idSesion;
        this.idUsuario = idUsuario;
        this.fechaInicio = LocalDateTime.now();
    }

    public void cerrarSesion() {
        this.fechaFin = LocalDateTime.now();
    }

    public int getTotalMensajes() {
        return mensajes.size();
    }

    // Getters y Setters
    public int getIdSesion() { return idSesion; }
    public void setIdSesion(int idSesion) { this.idSesion = idSesion; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<ChatMensaje> getMensajes() { return mensajes; }
    public void setMensajes(List<ChatMensaje> mensajes) { this.mensajes = mensajes; }
    public void agregarMensaje(ChatMensaje mensaje) { this.mensajes.add(mensaje); }

    @Override
    public String toString() {
        return "ChatSesion{" +
                "idSesion=" + idSesion +
                ", fechaInicio=" + fechaInicio +
                ", totalMensajes=" + getTotalMensajes() +
                '}';
    }
}
