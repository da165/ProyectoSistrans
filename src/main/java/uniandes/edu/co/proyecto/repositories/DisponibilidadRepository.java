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

    // Método para verificar la superposición (RF5).
    @Query("SELECT d FROM DisponibilidadEntity d WHERE d.vehiculo = :vehiculo " +
        "AND d.diaSemana = :dia AND " +
        "(:horaInicio < d.horaFin AND :horaFin > d.horaInicio)")
    List<DisponibilidadEntity> findSuperposedDisponibilidad(VehiculoEntity vehiculo, DayOfWeek dia, LocalTime horaInicio, LocalTime horaFin);

    // Método para verificar la superposición (RF6), EXCLUYENDO el ID actual. (AÑADIDO)
    @Query("SELECT d FROM DisponibilidadEntity d WHERE d.id <> :idExcluir AND d.vehiculo = :vehiculo " +
        "AND d.diaSemana = :dia AND " +
        "(:horaInicio < d.horaFin AND :horaFin > d.horaInicio)")
    List<DisponibilidadEntity> findSuperposedDisponibilidadExcluyendoId(Long idExcluir, VehiculoEntity vehiculo, DayOfWeek dia, LocalTime horaInicio, LocalTime horaFin);

    // Método para buscar disponibilidad por vehículo, día y hora de inicio (útil para consultas)
    DisponibilidadEntity findByVehiculoAndDiaSemanaAndHoraInicio(VehiculoEntity vehiculo, DayOfWeek diaSemana, LocalTime horaInicio);
}