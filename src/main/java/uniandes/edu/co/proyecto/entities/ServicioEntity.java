package uniandes.edu.co.proyecto.entities;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "SERVICIOS")
public class ServicioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TIPO_SERVICIO", nullable = false)
    private String tipoServicio; // e.g., "Transporte de pasajeros" [cite: 49]

    @Column(name = "COSTO_TOTAL", nullable = false)
    private Double costoTotal; // Calculado por distancia y tarifa [cite: 44, 64]

    // Histórico de tiempos
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "HORA_INICIO", nullable = false)
    private Date horaInicio; // [cite: 64]

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "HORA_FIN") // Se actualiza en RF9
    private Date horaFin; // [cite: 64]

    @Column(name = "DURACION_MINUTOS")
    private Long duracionMinutos; // [cite: 64]

    @Column(name = "LONGITUD_TRAYECTO") 
    private Double longitudTrayecto; // Distancia recorrida [cite: 64]

    // Relaciones
    @ManyToOne
    @JoinColumn(name = "ID_CONDUCTOR", nullable = false)
    private UsuarioConductorEntity conductor; // [cite: 64]

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO_CLIENTE", nullable = false)
    private UsuarioServicioEntity usuarioCliente; // Quién lo solicitó

    @ManyToOne
    @JoinColumn(name = "ID_VEHICULO", nullable = false)
    private VehiculoEntity vehiculo; // [cite: 64]

    @ManyToOne
    @JoinColumn(name = "ID_PUNTO_PARTIDA", nullable = false)
    private PuntoGeoEntity puntoPartida; // [cite: 46]
    
    // Asumiendo una tabla intermedia o una lista de PuntosGeograficos para múltiples llegadas
    // Aquí usamos una lista de puntos de llegada
    @ManyToMany
    @JoinTable(
        name = "SERVICIO_PUNTOS_LLEGADA",
        joinColumns = @JoinColumn(name = "ID_SERVICIO"),
        inverseJoinColumns = @JoinColumn(name = "ID_PUNTO_GEOGRAFICO")
    )
    private List<PuntoGeoEntity> puntosLlegada = new ArrayList<>();// Uno o varios puntos de llegada [cite: 46]

    // Constructor vacío (JPA)
    public ServicioEntity() {}

    // Constructor para RF8 (al iniciar el viaje)
    public ServicioEntity(String tipoServicio, Double costoTotal, Date horaInicio, UsuarioConductorEntity conductor, UsuarioServicioEntity usuarioCliente, VehiculoEntity vehiculo, PuntoGeoEntity puntoPartida, List<PuntoGeoEntity> puntosLlegada) {
        this.tipoServicio = tipoServicio;
        this.costoTotal = costoTotal;
        this.horaInicio = horaInicio;
        this.conductor = conductor;
        this.usuarioCliente = usuarioCliente;
        this.vehiculo = vehiculo;
        this.puntoPartida = puntoPartida;
        this.puntosLlegada = puntosLlegada;
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
    public List<PuntoGeoEntity> getPuntosLlegada() {
        return puntosLlegada;
    }
    public void setPuntosLlegada(List<PuntoGeoEntity> puntosLlegada) {
        this.puntosLlegada = puntosLlegada;
    }
}