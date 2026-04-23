/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Energic_dominio;
    

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author USUARIO
 */
public class Uso {
 




    private int idUso;
    private double horasPorDia;
    private int diasPorSemana;
    private LocalDate fechaRegistro;

    // FK -> Articulo
    private int idArticulo;
    private Articulo articulo;

    // Relación 1 Uso -> N Consumos
    private List<Consumo> consumos = new ArrayList<>();

    // Constructor vacío
    public Uso() {}

    // Constructor completo
    public Uso(int idUso, double horasPorDia, int diasPorSemana, int idArticulo) {
        this.idUso = idUso;
        this.horasPorDia = horasPorDia;
        this.diasPorSemana = diasPorSemana;
        this.idArticulo = idArticulo;
        this.fechaRegistro = LocalDate.now();
    }

    /**
     * Calcula las horas de uso por semana
     */
    public double getHorasPorSemana() {
        return horasPorDia * diasPorSemana;
    }

    /**
     * Calcula las horas de uso por mes (aproximado: 4.33 semanas/mes)
     */
    public double getHorasPorMes() {
        return getHorasPorSemana() * 4.33;
    }

    // Getters y Setters
    public int getIdUso() { return idUso; }
    public void setIdUso(int idUso) { this.idUso = idUso; }

    public double getHorasPorDia() { return horasPorDia; }
    public void setHorasPorDia(double horasPorDia) { this.horasPorDia = horasPorDia; }

    public int getDiasPorSemana() { return diasPorSemana; }
    public void setDiasPorSemana(int diasPorSemana) { this.diasPorSemana = diasPorSemana; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public int getIdArticulo() { return idArticulo; }
    public void setIdArticulo(int idArticulo) { this.idArticulo = idArticulo; }

    public Articulo getArticulo() { return articulo; }
    public void setArticulo(Articulo articulo) { this.articulo = articulo; }

    public List<Consumo> getConsumos() { return consumos; }
    public void setConsumos(List<Consumo> consumos) { this.consumos = consumos; }
    public void agregarConsumo(Consumo consumo) { this.consumos.add(consumo); }

    @Override
    public String toString() {
        return "Uso{" +
                "idUso=" + idUso +
                ", horasPorDia=" + horasPorDia +
                ", diasPorSemana=" + diasPorSemana +
                ", horasPorMes=" + getHorasPorMes() +
                ", idArticulo=" + idArticulo +
                '}';
    }
}

