package uniandes.edu.co.proyecto.repositories;
import uniandes.edu.co.proyecto.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    // Método para buscar un usuario por su cédula (RF2, RF3)
    UsuarioEntity findByNumeroCedula(String numeroCedula);
    @Query("SELECT c FROM UsuarioConductorEntity c WHERE c.estadoDisponible = true")
    // Con la herencia, filtramos por la subclase UsuarioConductor
    UsuarioConductorEntity findConductorDisponible(); 
}

