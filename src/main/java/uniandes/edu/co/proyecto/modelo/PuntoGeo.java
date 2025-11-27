package uniandes.edu.co.proyecto.modelo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PUNTO_GEO")
public class PuntoGeo {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long idPunto;
    
    @Column(name = "COORDENADAS", nullable = false, length = 100)
    private String coordenadas;
    
    @Column(name = "DIRECCION", nullable = false, length = 200)
    private String direccion;
    
    @Column(name = "CIUDAD", nullable = false, length = 50)
    private String ciudad;
    
    // Constructores
    public PuntoGeo() {}
    
    public PuntoGeo(String coordenadas, String direccion, String ciudad) {
        this.coordenadas = coordenadas;
        this.direccion = direccion;
        this.ciudad = ciudad;
    }
    
    // Getters y Setters
    public Long getIdPunto() { return idPunto; }
    public void setIdPunto(Long idPunto) { this.idPunto = idPunto; }
    
    public String getCoordenadas() { return coordenadas; }
    public void setCoordenadas(String coordenadas) { this.coordenadas = coordenadas; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
}
