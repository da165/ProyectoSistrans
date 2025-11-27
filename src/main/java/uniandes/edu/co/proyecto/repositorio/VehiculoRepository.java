package uniandes.edu.co.proyecto.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uniandes.edu.co.proyecto.modelo.Vehiculo;

import java.util.Collection;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {

    // RF4 - REGISTRAR UN VEHÍCULO PARA UN USUARIO CONDUCTOR
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO vehiculo (id_vehiculo, tipo, marca, modelo, color, placa, ciudad_exp, capacidad, id_conductor) VALUES (alpescab_sequence.nextval, :tipo, :marca, :modelo, :color, :placa, :ciudadExp, :capacidad, :idConductor)", nativeQuery = true)
    void registrarVehiculo(@Param("tipo") String tipo, @Param("marca") String marca, 
                          @Param("modelo") String modelo, @Param("color") String color, 
                          @Param("placa") String placa, @Param("ciudadExp") String ciudadExp, 
                          @Param("capacidad") int capacidad, @Param("idConductor") long idConductor);

    @Query(value = "SELECT * FROM vehiculo WHERE id_conductor = :idConductor", nativeQuery = true)
    Collection<Vehiculo> darVehiculosPorConductor(@Param("idConductor") long idConductor);

    @Query(value = "SELECT * FROM vehiculo WHERE id_vehiculo = :id", nativeQuery = true)
    Vehiculo darVehiculo(@Param("id") long id);

    @Query(value = "SELECT * FROM vehiculo", nativeQuery = true)
    Collection<Vehiculo> darVehiculos();
}
