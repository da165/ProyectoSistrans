package uniandes.edu.co.proyecto.repositories;
import uniandes.edu.co.proyecto.entities.CiudadEntity;    
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CiudadRepository extends JpaRepository<CiudadEntity, Long> {   
    CiudadEntity findByNombre(String nombre);
}
