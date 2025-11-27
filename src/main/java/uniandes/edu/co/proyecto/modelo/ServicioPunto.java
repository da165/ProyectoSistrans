package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "SERVICIO_PUNTO")
public class ServicioPunto {
    
    @EmbeddedId
    private ServicioPuntoPK pk;
    
    @ManyToOne
    @JoinColumn(name = "ID_SERVICE", insertable = false, updatable = false)
    private Servicio servicio;
    
    @ManyToOne
    @JoinColumn(name = "ID_PUNTO", insertable = false, updatable = false)
    private PuntoGeo punto;
    
    // Constructores
    public ServicioPunto() {
        this.pk = new ServicioPuntoPK();
    }
    
    public ServicioPunto(Servicio servicio, PuntoGeo punto) {
        this.pk = new ServicioPuntoPK(servicio.getIdService(), punto.getIdPunto());
        this.servicio = servicio;
        this.punto = punto;
    }
    
    // Getters y Setters
    public ServicioPuntoPK getPk() { return pk; }
    public void setPk(ServicioPuntoPK pk) { this.pk = pk; }
    
    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }
    
    public PuntoGeo getPunto() { return punto; }
    public void setPunto(PuntoGeo punto) { this.punto = punto; }
}