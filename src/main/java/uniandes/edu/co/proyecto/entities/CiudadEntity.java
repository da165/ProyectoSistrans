package uniandes.edu.co.proyecto.entities;
import jakarta.persistence.*;
@Entity
@Table(name = "CIUDADES")
public class CiudadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NOMBRE", nullable = false, unique = true)
    private String nombre;
    
    public CiudadEntity() {
    }
    
    public CiudadEntity(String nombre) {
        this.nombre = nombre;
    }
    // Getters y Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
