package uniandes.edu.co.proyecto.controllers.DTO;

// DTO de salida para RFC3 (Ganancias por Conductor, Vehículo y Tipo de Servicio)
public class GananciaConductorDTO {
    private String placaVehiculo;
    private String tipoServicio;
    private Double gananciasTotales;

    public GananciaConductorDTO(String placaVehiculo, String tipoServicio, Double gananciasTotales) {
        this.placaVehiculo = placaVehiculo;
        this.tipoServicio = tipoServicio;
        this.gananciasTotales = gananciasTotales;
    }

    // Getters y Setters
    public String getPlacaVehiculo() { return placaVehiculo; }
    public void setPlacaVehiculo(String placaVehiculo) { this.placaVehiculo = placaVehiculo; }
    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }
    public Double getGananciasTotales() { return gananciasTotales; }
    public void setGananciasTotales(Double gananciasTotales) { this.gananciasTotales = gananciasTotales; }
}