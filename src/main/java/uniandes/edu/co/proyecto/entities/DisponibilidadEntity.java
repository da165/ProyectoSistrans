package uniandes.edu.co.proyecto.entities;
import jakarta.persistence.*;
import java.time.LocalTime;
import java.time.DayOfWeek;

@Entity
@Table(name = "DISPONIBILIDADES")
public class DisponibilidadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "DIA_SEMANA", nullable = false)
    private DayOfWeek diaSemana; // Día de la semana [cite: 40]
    
    @Column(name = "HORA_INICIO", nullable = false)
    private LocalTime horaInicio; // Franja horaria [cite: 40]
    
    @Column(name = "HORA_FIN", nullable = false)
    private LocalTime horaFin;

    @Column(name = "TIPO_SERVICIO", nullable = false)
    private String tipoServicio;// Transporte de pasajeros, Entrega de comida, Transporte de mercancías [cite: 77]

    // Vehículo asociado a esta disponibilidad
    @ManyToOne
    @JoinColumn(name = "ID_VEHICULO", nullable = false)
    private VehiculoEntity vehiculo;
    
    // El conductor se puede obtener a través del Vehículo
    
    // Constructor vacío (JPA)
    public DisponibilidadEntity() {}

    // Constructor para inicialización
    public DisponibilidadEntity(DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFin, String tipoServicio, VehiculoEntity vehiculo) {
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.tipoServicio = tipoServicio;
        this.vehiculo = vehiculo;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }
    public void setDiaSemana(DayOfWeek diaSemana) {
        this.diaSemana = diaSemana;
    }
    public LocalTime getHoraInicio() {
        return horaInicio;
    }
    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }
    public LocalTime getHoraFin() {
        return horaFin;
    }
    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }
    public String getTipoServicio() {
        return tipoServicio;
    }
    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }
    public VehiculoEntity getVehiculo() {
        return vehiculo;
    }
    public void setVehiculo(VehiculoEntity vehiculo) {
        this.vehiculo = vehiculo;
    }    
}
