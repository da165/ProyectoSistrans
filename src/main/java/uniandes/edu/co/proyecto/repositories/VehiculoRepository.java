package uniandes.edu.co.proyecto.repositories;
import uniandes.edu.co.proyecto.entities.VehiculoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoRepository extends JpaRepository<VehiculoEntity, Long> {
    VehiculoEntity findByPlaca(String placa);
}
