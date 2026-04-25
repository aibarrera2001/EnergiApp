package sistemapanelessolares.dominio;

public class Casa {

    private String direccion;
    private String ciudad;
    private double consumoMensualKWh;
    private double latitud;
    private double longitud;

    public Casa(String direccion, String ciudad, double consumoMensualKWh, double latitud, double longitud) {
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.consumoMensualKWh = consumoMensualKWh;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public double getConsumoDiarioKWh() {
        int diasMes = 30;
        return consumoMensualKWh / diasMes;
    }

    public void setConsumoMensualKWh(double consumoMensualKWh) {
        this.consumoMensualKWh = consumoMensualKWh;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    @Override
    public String toString() {
        return "Casa en " + ciudad + " - " + direccion + ", consumo mensual: " + consumoMensualKWh + " kWh";
    }
}
