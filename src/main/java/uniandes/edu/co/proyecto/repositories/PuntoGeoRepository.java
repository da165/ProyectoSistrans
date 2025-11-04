package uniandes.edu.co.proyecto.repositories;
import uniandes.edu.co.proyecto.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PuntoGeoRepository extends JpaRepository<PuntoGeoEntity, Long> {
    // Si bien RF7 permite registrar puntos siempre, este repositorio es útil para buscar puntos existentes.
    PuntoGeoEntity findByDireccion(String direccion);
}
