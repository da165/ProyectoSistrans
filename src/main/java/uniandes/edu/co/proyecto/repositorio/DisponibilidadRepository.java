package uniandes.edu.co.proyecto.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uniandes.edu.co.proyecto.modelo.Disponibilidad;
import java.util.Date;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Integer> {

    // RF5 - REGISTRAR LA DISPONIBILIDAD
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO disponibilidad (id_disponibilidad, fecha_disp, hora_inicio, hora_fin) VALUES (alpescab_sequence.nextval, :fechaDisp, :horaInicio, :horaFin)", nativeQuery = true)
    void registrarDisponibilidad(@Param("fechaDisp") Date fechaDisp, 
                                @Param("horaInicio") Date horaInicio, 
                                @Param("horaFin") Date horaFin);

    // RF6 - MODIFICAR LA DISPONIBILIDAD (con validación de superposición)
    @Modifying
    @Transactional
    @Query(value = "UPDATE disponibilidad SET fecha_disp = :fechaDisp, hora_inicio = :horaInicio, hora_fin = :horaFin WHERE id_disponibilidad = :id", nativeQuery = true)
    void actualizarDisponibilidad(@Param("id") long id, @Param("fechaDisp") Date fechaDisp, 
                                 @Param("horaInicio") Date horaInicio, @Param("horaFin") Date horaFin);

    // Validación para RF5 y RF6 - No superposición para mismo conductor
    @Query(value = "SELECT COUNT(*) FROM disponibilidad d " +
                   "JOIN vehiculo_disponibilidad vd ON d.id_disponibilidad = vd.id_disponibilidad " +
                   "WHERE vd.id_vehiculo IN (SELECT id_vehiculo FROM vehiculo WHERE id_conductor = :idConductor) " +
                   "AND d.fecha_disp = :fecha " +
                   "AND ((d.hora_inicio BETWEEN :horaInicio AND :horaFin) OR " +
                   "(d.hora_fin BETWEEN :horaInicio AND :horaFin))", nativeQuery = true)
    int validarSuperposicionConductor(@Param("idConductor") long idConductor, 
                                     @Param("fecha") Date fecha, 
                                     @Param("horaInicio") Date horaInicio, 
                                     @Param("horaFin") Date horaFin);
}



