package uniandes.edu.co.proyecto.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uniandes.edu.co.proyecto.modelo.VehiculoDisponibilidad;
import uniandes.edu.co.proyecto.modelo.VehiculoDisponibilidadPK;

public interface VehiculoDisponibilidadRepository extends JpaRepository<VehiculoDisponibilidad, VehiculoDisponibilidadPK> {

    // RF5 - Asignar disponibilidad a vehículo
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO vehiculo_disponibilidad (id_vehiculo, id_disponibilidad) VALUES (:idVehiculo, :idDisponibilidad)", nativeQuery = true)
    void asignarDisponibilidadVehiculo(@Param("idVehiculo") long idVehiculo, 
                                      @Param("idDisponibilidad") long idDisponibilidad);

    @Query(value = "SELECT * FROM vehiculo_disponibilidad WHERE id_vehiculo = :idVehiculo", nativeQuery = true)
    java.util.Collection<VehiculoDisponibilidad> darDisponibilidadesPorVehiculo(@Param("idVehiculo") long idVehiculo);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM vehiculo_disponibilidad WHERE id_vehiculo = :idVehiculo AND id_disponibilidad = :idDisponibilidad", nativeQuery = true)
    void eliminarVehiculoDisponibilidad(@Param("idVehiculo") long idVehiculo, 
                                       @Param("idDisponibilidad") long idDisponibilidad);
}
