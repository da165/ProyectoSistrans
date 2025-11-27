package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="REVISION")
public class Revision {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long idRevision;
    
    private Integer evaluacion;
    
    private String comentario;
    
    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;
    
    // Constructores
    public Revision() {}
    
    public Revision(Integer evaluacion, String comentario, Usuario usuario) {
        this.evaluacion = evaluacion;
        this.comentario = comentario;
        this.usuario = usuario;
    }
    
    // Getters y Setters
    public Long getIdRevision() { return idRevision; }
    public void setIdRevision(Long idRevision) { this.idRevision = idRevision; }
    
    public Integer getEvaluacion() { return evaluacion; }
    public void setEvaluacion(Integer evaluacion) { this.evaluacion = evaluacion; }
    
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
