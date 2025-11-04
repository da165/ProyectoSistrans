package uniandes.edu.co.proyecto.entities;
import jakarta.persistence.*;

@Entity
@Table(name = "PUNTOS_GEOGRAFICOS")
public class PuntoGeoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre; // Para establecimientos particulares [cite: 83]

    @Column(name = "DIRECCION", nullable = false)
    private String direccion; // Dirección aproximada [cite: 47]

    @Column(name = "LATITUD", nullable = false)
    private Double latitud; // Coordenadas geográficas [cite: 47]

    @Column(name = "LONGITUD", nullable = false)
    private Double longitud; // Coordenadas geográficas [cite: 47]

    // Relación: Ciudad en la que se encuentra [cite: 47]
    @ManyToOne
    @JoinColumn(name = "ID_CIUDAD", nullable = false)
    private CiudadEntity ciudad;

    // Constructor vacío (JPA)
    public PuntoGeoEntity() {}

    // Constructor para inicialización
    public PuntoGeoEntity(String nombre, String direccion, Double latitud, Double longitud, CiudadEntity ciudad) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.ciudad = ciudad;
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
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public Double getLatitud() {
        return latitud;
    }
    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }
    public Double getLongitud() {
        return longitud;
    }
    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
    public CiudadEntity getCiudad() {
        return ciudad;
    }
    public void setCiudad(CiudadEntity ciudad) {
        this.ciudad = ciudad;
    }        
}