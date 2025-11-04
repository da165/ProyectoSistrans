package uniandes.edu.co.proyecto.entities;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
@Entity
@DiscriminatorValue("CONDUCTOR")
public class UsuarioConductorEntity extends UsuarioEntity {   
    @OneToMany(mappedBy = "conductor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VehiculoEntity> vehiculos = new ArrayList<>();
    @Column(name = "ESTADO_DISPONIBLE", nullable = false)
    private boolean estadoDisponible = true; 
    public UsuarioConductorEntity() {
        super();
    }
    public UsuarioConductorEntity(String nombre, String correoElectronico, String numeroCelular, String numeroCedula) {
        super(nombre, correoElectronico, numeroCelular, numeroCedula);
        this.estadoDisponible = true;
    }
    public List<VehiculoEntity> getVehiculos() {
        return vehiculos;
    }
    public void setVehiculos(List<VehiculoEntity> vehiculos) {
        this.vehiculos = vehiculos;
    }
    public boolean isEstadoDisponible() {
        return estadoDisponible;
    }
    public void setEstadoDisponible(boolean estadoDisponible) {
        this.estadoDisponible = estadoDisponible;
    }
}