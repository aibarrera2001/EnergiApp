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
public class Costo {
  
/**
 * Entidad débil: depende de Consumo para existir.
 * Representa el costo económico calculado a partir del kWh consumido
 * y la tarifa energética aplicada.
 */

    private int idCosto;
    private double valor;
    private double tarifaCopKwh;
    private LocalDate fechaCalculo;

    // FK -> Consumo (clave parcial: identidad depende de Consumo)
    private int idConsumo;
    private Consumo consumo;

    // Constructor vacío
    public Costo() {}

    // Constructor completo
    public Costo(int idCosto, double tarifaCopKwh, int idConsumo) {
        this.idCosto = idCosto;
        this.tarifaCopKwh = tarifaCopKwh;
        this.idConsumo = idConsumo;
        this.fechaCalculo = LocalDate.now();
    }

    /**
     * Calcula el valor del costo en COP a partir del consumo en kWh y la tarifa.
     * @param kwh          consumo en kilovatios-hora
     * @param tarifaCopKwh tarifa en COP por kWh
     * @return costo total en COP
     */
    public static double calcularCosto(double kwh, double tarifaCopKwh) {
        return kwh * tarifaCopKwh;
    }
    /**
     * Recalcula y asigna el valor usando el consumo asociado.
     */
    public void recalcularValor() {
        if (consumo != null) {
            this.valor = calcularCosto(consumo.getKwh(), this.tarifaCopKwh);
        }
    }

    // Getters y Setters
    public int getIdCosto() { return idCosto; }
    public void setIdCosto(int idCosto) { this.idCosto = idCosto; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public double getTarifaCopKwh() { return tarifaCopKwh; }
    public void setTarifaCopKwh(double tarifaCopKwh) { this.tarifaCopKwh = tarifaCopKwh; }

    public LocalDate getFechaCalculo() { return fechaCalculo; }
    public void setFechaCalculo(LocalDate fechaCalculo) { this.fechaCalculo = fechaCalculo; }

    public int getIdConsumo() { return idConsumo; }
    public void setIdConsumo(int idConsumo) { this.idConsumo = idConsumo; }

    public Consumo getConsumo() { return consumo; }
    public void setConsumo(Consumo consumo) { this.consumo = consumo; }

    @Override
    public String toString() {
        return "Costo{" +
                "idCosto=" + idCosto +
                ", valor=" + valor +
                ", tarifaCopKwh=" + tarifaCopKwh +
                ", fechaCalculo=" + fechaCalculo +
                ", idConsumo=" + idConsumo +
                '}';
    }
}

