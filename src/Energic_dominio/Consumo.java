/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Energic_dominio;

import java.time.LocalDate;
/**
 *
 * @author USUARIO
 */
public class Consumo {
    



    private int idConsumo;
    private double kwh;
    private LocalDate periodo;

    // FK -> Uso
    private int idUso;
    private Uso uso;

    // Relación 1 Consumo -> 1 Costo
    private Costo costo;

    // Constructor vacío
    public Consumo() {}

    // Constructor completo
    public Consumo(int idConsumo, double kwh, LocalDate periodo, int idUso) {
        this.idConsumo = idConsumo;
        this.kwh = kwh;
        this.periodo = periodo;
        this.idUso = idUso;
    }

    /**
     * Calcula el kWh mensual a partir de la potencia y el uso.
     * Formula: (Potencia_W / 1000) * horas_al_mes
     * @param potenciaWatts potencia del artículo en watts
     * @param horasPorMes   horas totales de uso en el mes
     * @return kWh consumidos en el mes
     */
    public static double calcularKwh(double potenciaWatts, double horasPorMes) {
        return (potenciaWatts / 1000.0) * horasPorMes;
    }

    // Getters y Setters
    public int getIdConsumo() { return idConsumo; }
    public void setIdConsumo(int idConsumo) { this.idConsumo = idConsumo; }

    public double getKwh() { return kwh; }
    public void setKwh(double kwh) { this.kwh = kwh; }

    public LocalDate getPeriodo() { return periodo; }
    public void setPeriodo(LocalDate periodo) { this.periodo = periodo; }

    public int getIdUso() { return idUso; }
    public void setIdUso(int idUso) { this.idUso = idUso; }

    public Uso getUso() { return uso; }
    public void setUso(Uso uso) { this.uso = uso; }

    public Costo getCosto() { return costo; }
    public void setCosto(Costo costo) { this.costo = costo; }

    @Override
    public String toString() {
        return "Consumo{" +
                "idConsumo=" + idConsumo +
                ", kwh=" + kwh +
                ", periodo=" + periodo +
                ", idUso=" + idUso +
                '}';
    }
}

