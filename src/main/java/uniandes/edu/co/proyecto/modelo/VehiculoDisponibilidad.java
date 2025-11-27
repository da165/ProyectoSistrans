package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "VEHICULO_DISPONIBILIDAD")
public class VehiculoDisponibilidad {
    
    @EmbeddedId
    private VehiculoDisponibilidadPK pk;
    
    @ManyToOne
    @JoinColumn(name = "ID_VEHICULO", insertable = false, updatable = false)
    private Vehiculo vehiculo;
    
    @ManyToOne
    @JoinColumn(name = "ID_DISPONIBILIDAD", insertable = false, updatable = false)
    private Disponibilidad disponibilidad;
    
    // Constructores
    public VehiculoDisponibilidad() {
        this.pk = new VehiculoDisponibilidadPK();
    }
    
    public VehiculoDisponibilidad(Vehiculo vehiculo, Disponibilidad disponibilidad) {
        this.pk = new VehiculoDisponibilidadPK(vehiculo.getIdVehiculo(), disponibilidad.getIdDisponibilidad());
        this.vehiculo = vehiculo;
        this.disponibilidad = disponibilidad;
    }
    
    // Getters y Setters
    public VehiculoDisponibilidadPK getPk() { return pk; }
    public void setPk(VehiculoDisponibilidadPK pk) { this.pk = pk; }
    
    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }
    
    public Disponibilidad getDisponibilidad() { return disponibilidad; }
    public void setDisponibilidad(Disponibilidad disponibilidad) { this.disponibilidad = disponibilidad; }
}
