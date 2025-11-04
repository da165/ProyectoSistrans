package uniandes.edu.co.proyecto.controllers.DTO;

// DTO de entrada para RF10/RF11: registrar revisión de un servicio
public class RevisionDTO {

    private Long servicioId;     // ID del servicio que se revisa
    private Integer calificacion; // 0..5 (según tu validación en RegistroService)
    private String comentario;   // opcional

    public Long getServicioId() {
         return servicioId; 
        }
    public void setServicioId(Long servicioId) {
         this.servicioId = servicioId; 
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
}
