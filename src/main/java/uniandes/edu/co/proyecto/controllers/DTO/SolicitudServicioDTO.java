package uniandes.edu.co.proyecto.controllers.DTO;
import java.util.List;
// Este DTO contiene solo la información necesaria que el cliente debe enviar.
public class SolicitudServicioDTO {

    private Long clienteId;
    private String tipoServicio;
    private Long puntoPartidaId;
    private List<Long> puntosLlegadaIds;
    private Double costoEstimado;

    // Getters y Setters (Necesarios para que Spring pueda deserializar el JSON)
    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public Long getPuntoPartidaId() {
        return puntoPartidaId;
    }

    public void setPuntoPartidaId(Long puntoPartidaId) {
        this.puntoPartidaId = puntoPartidaId;
    }

    public List<Long> getPuntosLlegadaIds() {
        return puntosLlegadaIds;
    }

    public void setPuntosLlegadaIds(List<Long> puntosLlegadaIds) {
        this.puntosLlegadaIds = puntosLlegadaIds;
    }

    public Double getCostoEstimado() {
        return costoEstimado;
    }

    public void setCostoEstimado(Double costoEstimado) {
        this.costoEstimado = costoEstimado;
    }
}
