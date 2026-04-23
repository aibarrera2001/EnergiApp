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
public class SolarEstimacion {

    private int idEstimacion;
    private int numPaneles;
    private double potenciaPanelWatts;   // por panel, ej. 300W
    private double horasSolDia;
    private double kwhGeneradosMes;
    private double ahorroMensual;
    private double ahorroAnual;
    private double coberturaPorcentaje;
    private double co2EvitadoKg;
    private LocalDateTime fechaCalculo;

    // FK -> Usuario
    private int idUsuario;
    private Usuario usuario;

    // Constructor vacío
    public SolarEstimacion() {}

    // Constructor completo
    public SolarEstimacion(int idEstimacion, int numPaneles, double potenciaPanelWatts,
                           double horasSolDia, double tarifaCopKwh, int idUsuario) {
        this.idEstimacion = idEstimacion;
        this.numPaneles = numPaneles;
        this.potenciaPanelWatts = potenciaPanelWatts;
        this.horasSolDia = horasSolDia;
        this.idUsuario = idUsuario;
        this.fechaCalculo = LocalDateTime.now();
        calcular(tarifaCopKwh);
    }

    /**
     * Calcula la generación y ahorro según los parámetros actuales.
     * Fórmula kWh/mes = (paneles * potencia_W / 1000) * horas_sol * 30
     * @param tarifaCopKwh tarifa energética en COP/kWh
     */
    public void calcular(double tarifaCopKwh) {
        this.kwhGeneradosMes = (numPaneles * potenciaPanelWatts / 1000.0) * horasSolDia * 30;
        this.ahorroMensual = kwhGeneradosMes * tarifaCopKwh;
        this.ahorroAnual = ahorroMensual * 12;
        // Factor de emisión Colombia: 0.299 kg CO2/kWh (UPME 2023)
        this.co2EvitadoKg = kwhGeneradosMes * 0.299;
    }

    /**
     * Calcula el porcentaje de cobertura respecto al consumo total del usuario.
     * @param consumoTotalKwhMes consumo mensual total del usuario en kWh
     */
    public void calcularCobertura(double consumoTotalKwhMes) {
        if (consumoTotalKwhMes > 0) {
            this.coberturaPorcentaje = Math.min(100.0, (kwhGeneradosMes / consumoTotalKwhMes) * 100);
        }
    }

    // Getters y Setters
    public int getIdEstimacion() { return idEstimacion; }
    public void setIdEstimacion(int idEstimacion) { this.idEstimacion = idEstimacion; }

    public int getNumPaneles() { return numPaneles; }
    public void setNumPaneles(int numPaneles) { this.numPaneles = numPaneles; }

    public double getPotenciaPanelWatts() { return potenciaPanelWatts; }
    public void setPotenciaPanelWatts(double potenciaPanelWatts) { this.potenciaPanelWatts = potenciaPanelWatts; }

    public double getHorasSolDia() { return horasSolDia; }
    public void setHorasSolDia(double horasSolDia) { this.horasSolDia = horasSolDia; }

    public double getKwhGeneradosMes() { return kwhGeneradosMes; }
    public void setKwhGeneradosMes(double kwhGeneradosMes) { this.kwhGeneradosMes = kwhGeneradosMes; }

    public double getAhorroMensual() { return ahorroMensual; }
    public void setAhorroMensual(double ahorroMensual) { this.ahorroMensual = ahorroMensual; }

    public double getAhorroAnual() { return ahorroAnual; }
    public void setAhorroAnual(double ahorroAnual) { this.ahorroAnual = ahorroAnual; }

    public double getCoberturaPorcentaje() { return coberturaPorcentaje; }
    public void setCoberturaPorcentaje(double coberturaPorcentaje) { this.coberturaPorcentaje = coberturaPorcentaje; }

    public double getCo2EvitadoKg() { return co2EvitadoKg; }
    public void setCo2EvitadoKg(double co2EvitadoKg) { this.co2EvitadoKg = co2EvitadoKg; }

    public LocalDateTime getFechaCalculo() { return fechaCalculo; }
    public void setFechaCalculo(LocalDateTime fechaCalculo) { this.fechaCalculo = fechaCalculo; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    @Override
    public String toString() {
        return "SolarEstimacion{" +
                "idEstimacion=" + idEstimacion +
                ", numPaneles=" + numPaneles +
                ", kwhGeneradosMes=" + kwhGeneradosMes +
                ", ahorroMensual=" + ahorroMensual +
                ", coberturaPorcentaje=" + coberturaPorcentaje +
                ", co2EvitadoKg=" + co2EvitadoKg +
                '}';
    }
}

