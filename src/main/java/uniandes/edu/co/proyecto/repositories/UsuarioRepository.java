package uniandes.edu.co.proyecto.repositories;
import uniandes.edu.co.proyecto.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    // Método para buscar un usuario por su cédula (RF2, RF3)
    UsuarioEntity findByNumeroCedula(String numeroCedula);
    
    // Método para buscar un Conductor disponible y cerca (Esencial para RF8).
    // Esta consulta es compleja y a menudo requiere una consulta nativa o JPQL detallada.
    // Usamos JPQL (o HQL) para encontrar conductores que:
    // 1. Sean conductores
    // 2. Estén marcados como disponibles (estadoDisponible = true)
    // 3. (La lógica de 'cerca' se manejará mejor en la capa de Servicio, ya que requiere cálculo geográfico)
    
    @Query("SELECT c FROM UsuarioConductor c WHERE c.estadoDisponible = true")
    // Nota: findByEstadoDisponibleAndTipoUsuario solo funciona si no usas la herencia de Single Table
    // Con la herencia, filtramos por la subclase UsuarioConductor
    UsuarioConductorEntity findConductorDisponible(); 
}

