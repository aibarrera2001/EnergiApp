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
public class ChatMensaje {
   

    public enum TipoEmisor {
        USUARIO,
        BOT
    }

    private int idMensaje;
    private String contenido;
    private TipoEmisor tipo;
    private LocalDateTime fechaHora;

    // FK -> ChatSesion
    private int idSesion;
    private ChatSession sesion;

    // Constructor vacío
    public ChatMensaje() {}

    // Constructor completo
    public ChatMensaje(int idMensaje, String contenido, TipoEmisor tipo, int idSesion) {
        this.idMensaje = idMensaje;
        this.contenido = contenido;
        this.tipo = tipo;
        this.idSesion = idSesion;
        this.fechaHora = LocalDateTime.now();
    }

    // Getters y Setters
    public int getIdMensaje() { return idMensaje; }
    public void setIdMensaje(int idMensaje) { this.idMensaje = idMensaje; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public TipoEmisor getTipo() { return tipo; }
    public void setTipo(TipoEmisor tipo) { this.tipo = tipo; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public int getIdSesion() { return idSesion; }
    public void setIdSesion(int idSesion) { this.idSesion = idSesion; }

    public ChatSession getSesion() { return sesion; }
    public void setSesion(ChatSession sesion) { this.sesion = sesion; }

    @Override
    public String toString() {
        return "ChatMensaje{" +
                "idMensaje=" + idMensaje +
                ", tipo=" + tipo +
                ", fechaHora=" + fechaHora +
                ", contenido='" + contenido + '\'' +
                '}';
    }
}

