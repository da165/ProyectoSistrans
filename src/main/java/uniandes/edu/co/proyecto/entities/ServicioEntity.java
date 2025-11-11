package uniandes.edu.co.proyecto.entities;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
//import java.util.ArrayList;

@Entity
@Table(name = "VIAJE") 
public class ServicioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TIPO_SERVICIO", nullable = false)
    private String tipoServicio; // e.g., "Transporte de pasajeros"

    @Column(name = "COSTO_TOTAL", nullable = false)
    private Double costoTotal; // Calculado por distancia y tarifa

    // Histórico de tiempos
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "HORA_INICIO", nullable = false)
    private Date horaInicio; //

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "HORA_FIN") // Se actualiza en RF9
    private Date horaFin; //

    @Column(name = "DURACION_MINUTOS")
    private Long duracionMinutos; //

    @Column(name = "LONGITUD_TRAYECTO") 
    private Double longitudTrayecto; // Distancia recorrida

    // Relaciones de muchos a uno

    // Conductor asignado
    @ManyToOne
    @JoinColumn(name = "ID_CONDUCTOR", nullable = false)
    private UsuarioConductorEntity conductor;

    // Usuario que solicita el servicio (Cliente)
    @ManyToOne
    @JoinColumn(name = "ID_USUARIO_SERVICIO", nullable = false)
    private UsuarioServicioEntity usuarioCliente;

    // Vehículo que realiza el servicio
    @ManyToOne
    @JoinColumn(name = "ID_VEHICULO", nullable = false)
    private VehiculoEntity vehiculo;

    // Punto de Partida
    @ManyToOne
    @JoinColumn(name = "ID_PUNTO_INICIO", nullable = false) 
    private PuntoGeoEntity puntoPartida;

    // Punto de Llegada Final
    @ManyToOne
   
    @JoinColumn(name = "ID_PUNTO_FIN") // Se permite NULL si es un viaje con múltiples paradas
    private PuntoGeoEntity puntoLlegadaFinal;
    
    // Una relación de uno a muchos (una revisión por servicio)
    // En RevisionEntity se usó @OneToOne, lo que es correcto.
    @OneToOne(mappedBy = "servicio", cascade = CascadeType.ALL, orphanRemoval = true)
    private RevisionEntity revision;


    // Constructor vacío (JPA)
    public ServicioEntity() {}

    // Constructor para RF8: Solicitud inicial
    public ServicioEntity(String tipoServicio, Double costoTotal, Date horaInicio, UsuarioConductorEntity conductor, UsuarioServicioEntity usuarioCliente, VehiculoEntity vehiculo, PuntoGeoEntity puntoPartida, List<PuntoGeoEntity> puntosLlegada) {
        this.tipoServicio = tipoServicio;
        this.costoTotal = costoTotal;
        this.horaInicio = horaInicio;
        this.conductor = conductor;
        this.usuarioCliente = usuarioCliente;
        this.vehiculo = vehiculo;
        this.puntoPartida = puntoPartida;
        
        // Asignar el último punto de la lista como punto de llegada final,
        // asumiendo que el campo ID_PUNTO_FIN en VIAJE es para el destino final.
        if (puntosLlegada != null && !puntosLlegada.isEmpty()) {
            this.puntoLlegadaFinal = puntosLlegada.get(puntosLlegada.size() - 1);
        } else {
             this.puntoLlegadaFinal = null;
        }

        // Inicializar otros campos a null/0
        this.horaFin = null;
        this.duracionMinutos = 0L;
        this.longitudTrayecto = 0.0;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTipoServicio() {
        return tipoServicio;
    }
    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }
    public Double getCostoTotal() {
        return costoTotal;
    }
    public void setCostoTotal(Double costoTotal) {
        this.costoTotal = costoTotal;
    }
    public Date getHoraInicio() {
        return horaInicio;
    }
    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }
    public Date getHoraFin() {
        return horaFin;
    }
    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }
    public Long getDuracionMinutos() {
        return duracionMinutos;
    }
    public void setDuracionMinutos(Long duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }
    public Double getLongitudTrayecto() {
        return longitudTrayecto;
    }
    public void setLongitudTrayecto(Double longitudTrayecto) {
        this.longitudTrayecto = longitudTrayecto;
    }
    public UsuarioConductorEntity getConductor() {
        return conductor;
    }
    public void setConductor(UsuarioConductorEntity conductor) {
        this.conductor = conductor;
    }
    public UsuarioServicioEntity getUsuarioCliente() {
        return usuarioCliente;
    }
    public void setUsuarioCliente(UsuarioServicioEntity usuarioCliente) {
        this.usuarioCliente = usuarioCliente;
    }
    public VehiculoEntity getVehiculo() {
        return vehiculo;
    }
    public void setVehiculo(VehiculoEntity vehiculo) {
        this.vehiculo = vehiculo;
    }
    public PuntoGeoEntity getPuntoPartida() {
        return puntoPartida;
    }
    public void setPuntoPartida(PuntoGeoEntity puntoPartida) {
        this.puntoPartida = puntoPartida;
    }
    public PuntoGeoEntity getPuntoLlegadaFinal() {
        return puntoLlegadaFinal;
    }
    public void setPuntoLlegadaFinal(PuntoGeoEntity puntoLlegadaFinal) {
        this.puntoLlegadaFinal = puntoLlegadaFinal;
    }
    public RevisionEntity getRevision() {
        return revision;
    }
    public void setRevision(RevisionEntity revision) {
        this.revision = revision;
    }
}