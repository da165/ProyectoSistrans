package uniandes.edu.co.proyecto.modelo;

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

@Entity
@Table(name = "VEHICULO")
public class Vehiculo {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String tipo;

    private String marca;

    private String modelo;

    private String color;

    private String placa;

    private String ciudadExpo;

    private Integer capacidad;

    @ManyToOne
    @JoinColumn(name = "ID_CONDUCTOR", nullable = false)
    private Conductor conductor;

    @ManyToMany
    @JoinTable(
        name = "VEHICULO_DISPONIBILIDAD",
        joinColumns = @JoinColumn(name = "ID_VEHICULO"),
        inverseJoinColumns = @JoinColumn(name = "ID_DISPONIBILIDAD")
    )
    private List<Disponibilidad> disponibilidades;

    public Vehiculo()
    {}

    public Vehiculo(String tipo, String marca, String modelo, String color, String placa, String ciudadExpo, Integer capacidad, Conductor conductor) {
        this.tipo = tipo;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.placa = placa;
        this.ciudadExpo = ciudadExpo;
        this.capacidad = capacidad;
        this.conductor = conductor;
    }

    public Long getIdVehiculo() { return id; }
    public void setIdVehiculo(Long idVehiculo) { this.id = idVehiculo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getCiudadExpo() { return ciudadExpo; }
    public void setCiudadExpo(String ciudadExpo) { this.ciudadExpo = ciudadExpo; }

    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }
    
    public Conductor getConductor() { return conductor; }
    public void setConductor(Conductor conductor) { this.conductor = conductor; }
    
    public List<Disponibilidad> getDisponibilidades() { return disponibilidades; }
    public void setDisponibilidades(List<Disponibilidad> disponibilidades) { this.disponibilidades = disponibilidades; }
        
    
}
