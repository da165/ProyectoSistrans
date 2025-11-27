package uniandes.edu.co.proyecto.modelo;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "USUARIO_SERVICIO")
public class UsuarioServicio extends Usuario {
    private Long numeroCar;
    
    private String nombreCar;
    
    private Date fechaVenci;
    
    private Long cc;
    
    // Constructores
    public UsuarioServicio() {}
    
    public UsuarioServicio(String nombre, String correo, String celular, String cedula, 
                          Long numeroCar, String nombreCar, java.sql.Date fechaVenci, Long cc) {
        super(nombre, correo, celular, cedula);
        this.numeroCar = numeroCar;
        this.nombreCar = nombreCar;
        this.fechaVenci = fechaVenci;
        this.cc = cc;
    }
    
    // Getters y Setters
    public Long getNumeroCar() { return numeroCar; }
    public void setNumeroCar(Long numeroCar) { this.numeroCar = numeroCar; }
    
    public String getNombreCar() { return nombreCar; }
    public void setNombreCar(String nombreCar) { this.nombreCar = nombreCar; }
    
    public Date getFechaVenci() { return fechaVenci; }
    public void setFechaVenci(Date fechaVenci) { this.fechaVenci = fechaVenci; }
    
    public Long getCc() { return cc; }
    public void setCc(Long cc) { this.cc = cc; }
    
}
