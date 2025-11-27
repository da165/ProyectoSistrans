package uniandes.edu.co.proyecto.modelo;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="CONDUCTOR")
public class Conductor extends Usuario {
   
    @OneToMany(mappedBy = "conductor", cascade = CascadeType.ALL)
    private List<Vehiculo> vehiculos;
    
    @OneToMany(mappedBy = "conductor")
    private List<Servicio> servicios;

    public Conductor()
    {;}

    public Conductor(String nombre,  String correo, String celular, String cedula)
    {
        super(nombre, correo, celular, cedula);
    }


    public List<Vehiculo> getVehiculos() { return vehiculos; }
    public void setVehiculos(List<Vehiculo> vehiculos) { this.vehiculos = vehiculos; }
    
    public List<Servicio> getServicios() { return servicios; }
    public void setServicios(List<Servicio> servicios) { this.servicios = servicios; }


}