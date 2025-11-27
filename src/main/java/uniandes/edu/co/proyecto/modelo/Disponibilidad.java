package uniandes.edu.co.proyecto.modelo;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="DISPONIBILIDAD")
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long idDisponibilidad;

    private Date fechaDisp;

    private Date horaInicio;

    private Date horaFin;

    @ManyToMany(mappedBy = "disponibilidades")
    private List<Vehiculo> vehiculos;

    public Disponibilidad(){}

    public Disponibilidad(Date fechaDisp, Date horaInicio, Date horaFin)
    {
        this.fechaDisp = fechaDisp;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public Long getIdDisponibilidad() { return idDisponibilidad; }
    public void setIdDisponibilidad(Long idDisponibilidad) { this.idDisponibilidad = idDisponibilidad; }
    
    public Date getFechaDisp() { return fechaDisp; }
    public void setFechaDisp(Date fechaDisp) { this.fechaDisp = fechaDisp; }
    
    public Date getHoraInicio() { return horaInicio; }
    public void setHoraInicio(Date horaInicio) { this.horaInicio = horaInicio; }
    
    public Date getHoraFin() { return horaFin; }
    public void setHoraFin(Date horaFin) { this.horaFin = horaFin; }
    
    public List<Vehiculo> getVehiculos() { return vehiculos; }
    public void setVehiculos(List<Vehiculo> vehiculos) { this.vehiculos = vehiculos; }
}

