package uniandes.edu.co.proyecto.entities;
import jakarta.persistence.*;
@Entity
@DiscriminatorValue("CLIENTE")
public class UsuarioServicioEntity extends UsuarioEntity {
    public UsuarioServicioEntity() {
        super();
    }
    public UsuarioServicioEntity(String nombre, String correoElectronico, String numeroCelular, String numeroCedula) {
        super(nombre, correoElectronico, numeroCelular, numeroCedula);
    }
}