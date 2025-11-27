package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class VehiculoDisponibilidadPK implements Serializable {
    
    private Long id_vehiculo;
    private Long id_disponibilidad;
    
    // Constructores
    public VehiculoDisponibilidadPK() {}
    
    public VehiculoDisponibilidadPK(Long id_vehiculo, Long id_disponibilidad) {
        this.id_vehiculo = id_vehiculo;
        this.id_disponibilidad = id_disponibilidad;
    }
    
    // Getters y Setters
    public Long getId_vehiculo() { return id_vehiculo; }
    public void setId_vehiculo(Long id_vehiculo) { this.id_vehiculo = id_vehiculo; }
    
    public Long getId_disponibilidad() { return id_disponibilidad; }
    public void setId_disponibilidad(Long id_disponibilidad) { this.id_disponibilidad = id_disponibilidad; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VehiculoDisponibilidadPK)) return false;
        VehiculoDisponibilidadPK that = (VehiculoDisponibilidadPK) o;
        return id_vehiculo.equals(that.id_vehiculo) && id_disponibilidad.equals(that.id_disponibilidad);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id_vehiculo, id_disponibilidad);
    }
}
