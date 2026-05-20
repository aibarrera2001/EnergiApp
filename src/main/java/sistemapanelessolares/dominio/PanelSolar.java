package sistemapanelessolares.dominio;

public class PanelSolar {

private int id;
    private String nombre;
    private String tipo;           // Monocristalino, Policristalino, Thin-Film, etc.
    private double potenciaWatts;  // Potencia pico en vatios (Wp)
    private double eficiencia;     // Porcentaje de eficiencia (0-100)
    private double costoUnidad;    // Costo por panel en $
    private double costoInstalacion; // Costo de instalación adicional por panel
    private String garantiaAnios;  // Garantía en años
    private String descripcion;    // Descripción adicional del panel
 
    public PanelSolar() {}
 
    public PanelSolar(int id, String nombre, String tipo, double potenciaWatts,
                      double eficiencia, double costoUnidad, double costoInstalacion,
                      String garantiaAnios, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.potenciaWatts = potenciaWatts;
        this.eficiencia = eficiencia;
        this.costoUnidad = costoUnidad;
        this.costoInstalacion = costoInstalacion;
        this.garantiaAnios = garantiaAnios;
        this.descripcion = descripcion;
    }
 
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
 
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
 
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
 
    public double getPotenciaWatts() { return potenciaWatts; }
    public void setPotenciaWatts(double potenciaWatts) { this.potenciaWatts = potenciaWatts; }
 
    public double getEficiencia() { return eficiencia; }
    public void setEficiencia(double eficiencia) { this.eficiencia = eficiencia; }
 
    public double getCostoUnidad() { return costoUnidad; }
    public void setCostoUnidad(double costoUnidad) { this.costoUnidad = costoUnidad; }
 
    public double getCostoInstalacion() { return costoInstalacion; }
    public void setCostoInstalacion(double costoInstalacion) { this.costoInstalacion = costoInstalacion; }
 
    public String getGarantiaAnios() { return garantiaAnios; }
    public void setGarantiaAnios(String garantiaAnios) { this.garantiaAnios = garantiaAnios; }
 
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
 
    @Override
    public String toString() {
        return "PanelSolar{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", potencia=" + potenciaWatts + "W" +
                ", eficiencia=" + eficiencia + "%" +
                ", costoUnidad=$" + String.format("%.2f", costoUnidad) +
                ", costoInstalacion=$" + String.format("%.2f", costoInstalacion) +
                ", garantia='" + garantiaAnios + " años'" +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
    }
    