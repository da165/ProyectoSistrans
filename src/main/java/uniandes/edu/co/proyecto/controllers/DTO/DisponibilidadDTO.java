package uniandes.edu.co.proyecto.controllers.DTO;

// DTO de entrada para RF5/RF6: registrar/modificar disponibilidad
// Notas de formato para que el JSON sea simple:
//  - diaSemana: "MONDAY", "TUESDAY", ... (enum DayOfWeek de Java)
//  - horaInicio/horaFin: "HH:mm" (ej. "08:00")
public class DisponibilidadDTO {

    private String diaSemana;    // DayOfWeek en texto (MONDAY, TUESDAY, ...)
    private String horaInicio;   // "HH:mm"
    private String horaFin;      // "HH:mm"
    private String tipoServicio; // Transporte pasajeros / comida / mercancías...
    private Long vehiculoId;     // FK del vehículo
//holasb
    public String getDiaSemana() {
         return diaSemana; 
        }
    public void setDiaSemana(String diaSemana) {
         this.diaSemana = diaSemana; 
        }

    public String getHoraInicio() {
         return horaInicio; 
        }
    public void setHoraInicio(String horaInicio) {
         this.horaInicio = horaInicio; 
        }

    public String getHoraFin() {
         return horaFin; 
        }
    public void setHoraFin(String horaFin) {
         this.horaFin = horaFin; 
        }

    public String getTipoServicio() {
         return tipoServicio; 
        }
    public void setTipoServicio(String tipoServicio) {
         this.tipoServicio = tipoServicio; 
        }

    public Long getVehiculoId() {
         return vehiculoId; 
        }
    public void setVehiculoId(Long vehiculoId) {
         this.vehiculoId = vehiculoId; 
        }
}
