package uniandes.edu.co.proyecto.controllers.DTO;

// DTO de salida para RFC2 (Top 20 Conductores)
public class TopConductorDTO {
    private Long conductorId;
    private Long numeroServicios;

    public TopConductorDTO(Long conductorId, Long numeroServicios) {
        this.conductorId = conductorId;
        this.numeroServicios = numeroServicios;
    }

    // Getters y Setters
    public Long getConductorId() { return conductorId; }
    public void setConductorId(Long conductorId) { this.conductorId = conductorId; }
    public Long getNumeroServicios() { return numeroServicios; }
    public void setNumeroServicios(Long numeroServicios) { this.numeroServicios = numeroServicios; }
}
