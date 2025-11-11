package uniandes.edu.co.proyecto.controllers.DTO;

// DTO de salida para RFC4 (Utilización de Servicios en Ciudad y Rango)
public class UtilizacionServiciosDTO {
    private String tipoServicio;
    private Long numeroServicios;
    private Double porcentajeUso;

    public UtilizacionServiciosDTO(String tipoServicio, Long numeroServicios, Double porcentajeUso) {
        this.tipoServicio = tipoServicio;
        this.numeroServicios = numeroServicios;
        this.porcentajeUso = porcentajeUso;
    }

    // Getters y Setters
    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }
    public Long getNumeroServicios() { return numeroServicios; }
    public void setNumeroServicios(Long numeroServicios) { this.numeroServicios = numeroServicios; }
    public Double getPorcentajeUso() { return porcentajeUso; }
    public void setPorcentajeUso(Double porcentajeUso) { this.porcentajeUso = porcentajeUso; }
}