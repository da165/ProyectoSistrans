package uniandes.edu.co.proyecto.entities;
import jakarta.persistence.*;
@Entity
@Table(name = "REVISION") 
public class RevisionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CALIFICACION", nullable = false)
    private Integer calificacion;// Entre 0 y 5

    @Column(name = "COMENTARIO")
    private String comentario; // Comentario de texto
    
    // Relación: La revisión siempre se hace sobre un servicio prestado.
    @OneToOne(fetch = FetchType.LAZY)
    
    @JoinColumn(name = "ID_VIAJE", nullable = false, unique = true) // Una revisión por servicio
    private ServicioEntity servicio;

    // Quien hace la revisión: Puede ser Cliente o Conductor
    @ManyToOne
    @JoinColumn(name = "ID_REVISOR", nullable = false)
    private UsuarioEntity revisor;

    // A quien se le hace la revisión: Puede ser Conductor o Cliente
    @ManyToOne
    @JoinColumn(name = "ID_REVISADO", nullable = false)
    private UsuarioEntity revisado;

    // Constructor vacío (JPA)
    public RevisionEntity() {}

    // Constructor para inicialización
    public RevisionEntity(Integer calificacion, String comentario, ServicioEntity servicio, UsuarioEntity revisor, UsuarioEntity revisado) {
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.servicio = servicio;
        this.revisor = revisor;
        this.revisado = revisado;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Integer getCalificacion() {
        return calificacion;
    }
    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }
    public String getComentario() {
        return comentario;
    }
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    public ServicioEntity getServicio() {
        return servicio;
    }
    public void setServicio(ServicioEntity servicio) {
        this.servicio = servicio;
    }
    public UsuarioEntity getRevisor() {
        return revisor;
    }
    public void setRevisor(UsuarioEntity revisor) {
        this.revisor = revisor;
    }
    public UsuarioEntity getRevisado() {
        return revisado;
    }
    public void setRevisado(UsuarioEntity revisado) {
        this.revisado = revisado;
    }
}