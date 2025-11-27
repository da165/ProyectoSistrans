package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ServicioPuntoPK implements Serializable {
    
    private Long id_service;
    private Long id_punto;
    
    // Constructores, getters, setters, equals, hashCode
    public ServicioPuntoPK() {}
    
    public ServicioPuntoPK(Long id_service, Long id_punto) {
        this.id_service = id_service;
        this.id_punto = id_punto;
    }
    
    // Getters y Setters
    public Long getId_service() { return id_service; }
    public void setId_service(Long id_service) { this.id_service = id_service; }
    
    public Long getId_punto() { return id_punto; }
    public void setId_punto(Long id_punto) { this.id_punto = id_punto; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServicioPuntoPK)) return false;
        ServicioPuntoPK that = (ServicioPuntoPK) o;
        return id_service.equals(that.id_service) && id_punto.equals(that.id_punto);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id_service, id_punto);
    }
}
