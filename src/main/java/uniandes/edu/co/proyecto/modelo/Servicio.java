package uniandes.edu.co.proyecto.modelo;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name="SERVICIO")
public class Servicio {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long idService;
    
   
    private String tipoService;
    
   
    private Double distancia;
    

    private Integer duracion;
    
    
    private Double costo;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date horarioInicio;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaFin;
    
    @ManyToOne
    @JoinColumn(name = "ID_USER", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "conductor_id")
    private Conductor conductor;
    
    @ManyToMany
    @JoinTable(
        name = "SERVICIO_PUNTO",
        joinColumns = @JoinColumn(name = "ID_SERVICE"),
        inverseJoinColumns = @JoinColumn(name = "ID_PUNTO")
    )
    private List<PuntoGeo> puntos;
    
    // Constructores
    public Servicio() {}
    
    public Servicio(String tipoService, Double distancia, Integer duracion, 
                   Double costo, Date horarioInicio, Date horaFin, Usuario usuario, Conductor conductor) {
        this.tipoService = tipoService;
        this.distancia = distancia;
        this.duracion = duracion;
        this.costo = costo;
        this.horarioInicio = horarioInicio;
        this.usuario = usuario;
        this.horaFin = horaFin;
        this.conductor = conductor;
    }
    
    // Getters y Setters
    public Long getIdService() { return idService; }
    public void setIdService(Long idService) { this.idService = idService; }
    
    public String getTipoService() { return tipoService; }
    public void setTipoService(String tipoService) { this.tipoService = tipoService; }
    
    public Double getDistancia() { return distancia; }
    public void setDistancia(Double distancia) { this.distancia = distancia; }
    
    public Integer getDuracion() { return duracion; }
    public void setDuracion(Integer duracion) { this.duracion = duracion; }
    
    public Double getCosto() { return costo; }
    public void setCosto(Double costo) { this.costo = costo; }
    
    public Date getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(Date horarioInicio) { this.horarioInicio = horarioInicio; }
    
    public Date getHoraFin() { return horaFin; }
    public void setHoraFin(Date horaFin) { this.horaFin = horaFin; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public List<PuntoGeo> getPuntos() { return puntos; }
    public void setPuntos(List<PuntoGeo> puntos) { this.puntos = puntos; }
    
}
