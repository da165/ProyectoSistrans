package uniandes.edu.co.proyecto.entities;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MEDIOS_DE_PAGO")
public class MediosPagoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Número de tarjeta (debería ser encriptado en un proyecto real, aquí solo se registra)
    @Column(name = "NUMERO_TARJETA", nullable = false)
    private String numeroTarjeta;

    @Column(name = "NOMBRE_EN_TARJETA", nullable = false)
    private String nombreEnTarjeta; // Nombre en la tarjeta 

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_VENCIMIENTO", nullable = false)
    private Date fechaVencimiento; // Fecha de vencimiento 

    @Column(name = "CODIGO_SEGURIDAD", nullable = false)
    private String codigoSeguridad; // Código de seguridad (CVV) 

    @Column(name = "TIPO_PAGO", nullable = false)
    private String tipoPago; // e.g., "Tarjeta de Crédito", "Débito"
    
    // Relación: Un medio de pago pertenece a un usuario de servicio
    @ManyToOne
    @JoinColumn(name = "ID_USUARIO_SERVICIO", nullable = false)
    private UsuarioServicioEntity usuarioDeServicio;

    // Constructor vacío (JPA)
    public MediosPagoEntity() {}

    // Constructor para inicialización
    public MediosPagoEntity(String numeroTarjeta, String nombreEnTarjeta, Date fechaVencimiento, String codigoSeguridad, String tipoPago, UsuarioServicioEntity usuarioDeServicio) {
        this.numeroTarjeta = numeroTarjeta;
        this.nombreEnTarjeta = nombreEnTarjeta;
        this.fechaVencimiento = fechaVencimiento;
        this.codigoSeguridad = codigoSeguridad;
        this.tipoPago = tipoPago;
        this.usuarioDeServicio = usuarioDeServicio;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }
    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }
    public String getNombreEnTarjeta() {
        return nombreEnTarjeta;
    }
    public void setNombreEnTarjeta(String nombreEnTarjeta) {
        this.nombreEnTarjeta = nombreEnTarjeta;
    }
    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }
    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    public String getCodigoSeguridad() {
        return codigoSeguridad;
    }
    public void setCodigoSeguridad(String codigoSeguridad) {
        this.codigoSeguridad = codigoSeguridad;
    }
    public String getTipoPago() {
        return tipoPago;
    }
    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }
    public UsuarioServicioEntity getUsuarioDeServicio() {
        return usuarioDeServicio;
    }
    public void setUsuarioDeServicio(UsuarioServicioEntity usuarioDeServicio) {
        this.usuarioDeServicio = usuarioDeServicio;
    }    
}