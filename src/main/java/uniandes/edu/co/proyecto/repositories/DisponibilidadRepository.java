package uniandes.edu.co.proyecto.repositories;
import uniandes.edu.co.proyecto.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface DisponibilidadRepository extends JpaRepository<DisponibilidadEntity, Long> {

    // Método para verificar la superposición (RF5, RF6).
    // Busca disponibilidades existentes para un conductor (a través del vehículo) y un día específico,
    // cuya franja horaria se superponga con la nueva franja (horaInicio, horaFin).
    
    // Use the entity class name (DisponibilidadEntity) and parameter names that
    // match the method signature. The original query referenced a 'conductor'
    // parameter which did not exist on the method, causing startup failure.
    @Query("SELECT d FROM DisponibilidadEntity d WHERE d.vehiculo = :vehiculo " +
        "AND d.diaSemana = :dia AND " +
           // La lógica de superposición es:
           // (InicioA < FinB) AND (FinA > InicioB)
        "(:horaInicio < d.horaFin AND :horaFin > d.horaInicio)")
    List<DisponibilidadEntity> findSuperposedDisponibilidad(VehiculoEntity vehiculo, DayOfWeek dia, LocalTime horaInicio, LocalTime horaFin);

    // Método para buscar disponibilidades por vehículo
    List<DisponibilidadEntity> findByVehiculo(VehiculoEntity vehiculo);
}