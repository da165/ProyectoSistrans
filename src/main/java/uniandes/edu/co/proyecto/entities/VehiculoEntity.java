package uniandes.edu.co.proyecto.entities;
import jakarta.persistence.*;

@Entity
@Table(name = "VEHICULOS")
public class VehiculoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TIPO", nullable = false)
    private String tipo; // carro, camioneta, motocicleta [cite: 39]

    @Column(name = "MARCA", nullable = false)
    private String marca;

    @Column(name = "MODELO", nullable = false)
    private String modelo;

    @Column(name = "COLOR", nullable = false)
    private String color;

    @Column(name = "PLACA", nullable = false, unique = true)
    private String placa;

    @Column(name = "CAPACIDAD_PASAJEROS", nullable = false)
    private Integer capacidadPasajeros; // [cite: 39]

    @Column(name = "NIVEL_ASIGNADO")
    private String nivelAsignado; // Estándar, Confort, Large (Determinado por modelo y capacidad) [cite: 41, 51]

    // Relación: Un vehículo pertenece a un conductor
    @ManyToOne
    @JoinColumn(name = "ID_CONDUCTOR", nullable = false)
    private UsuarioConductorEntity conductor;
    // Relación: Ciudad de expedición de la placa [cite: 39]
    @ManyToOne
    @JoinColumn(name = "ID_CIUDAD_EXPEDICION", nullable = false)
    private CiudadEntity ciudadExpedicion;
    
    // Constructor vacío (JPA)
    public VehiculoEntity() {}

    // Constructor para inicialización
    public VehiculoEntity(String tipo, String marca, String modelo, String color, String placa, Integer capacidadPasajeros, String nivelAsignado, CiudadEntity ciudadExpedicion, UsuarioConductorEntity conductor) {
        this.tipo = tipo;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.placa = placa;
        this.capacidadPasajeros = capacidadPasajeros;
        this.nivelAsignado = nivelAsignado;
        this.ciudadExpedicion = ciudadExpedicion;
        this.conductor = conductor;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getMarca() {
        return marca;
    
    }  
    public void setMarca(String marca) {
        this.marca = marca;
    }
    public String getModelo() {
        return modelo;
    }
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public String getPlaca() {
        return placa;
    }
    public void setPlaca(String placa) {
        this.placa = placa;
    }
    public Integer getCapacidadPasajeros() {
        return capacidadPasajeros;
    }
    public void setCapacidadPasajeros(Integer capacidadPasajeros) {
        this.capacidadPasajeros = capacidadPasajeros;
    }
    public String getNivelAsignado() {
        return nivelAsignado;
    }
    public void setNivelAsignado(String nivelAsignado) {
        this.nivelAsignado = nivelAsignado;
    }
    public UsuarioConductorEntity getConductor() {
        return conductor;
    }
    public void setConductor(UsuarioConductorEntity conductor) {
        this.conductor = conductor;
    }
    public CiudadEntity getCiudadExpedicion() {
        return ciudadExpedicion;
    }
    public void setCiudadExpedicion(CiudadEntity ciudadExpedicion) {
        this.ciudadExpedicion = ciudadExpedicion;
    }
}