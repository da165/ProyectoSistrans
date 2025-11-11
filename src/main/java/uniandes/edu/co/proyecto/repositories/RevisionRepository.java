package uniandes.edu.co.proyecto.repositories;
import uniandes.edu.co.proyecto.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevisionRepository extends JpaRepository<RevisionEntity, Long> {
    // Buscar si ya existe una revisión para un servicio (solo puede haber una)   
    RevisionEntity findByServicio_Id(Long servicioId); 
}
