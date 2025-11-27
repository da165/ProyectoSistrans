package uniandes.edu.co.proyecto.repositorio;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import uniandes.edu.co.proyecto.modelo.Servicio;

public interface ServicioRepository extends JpaRepository<Servicio, Integer> {

 // RF8 - SOLICITAR UN SERVICIO  - pequeño cambio q asegura que ya fue asignado
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO servicio (id_service, tipo_service, distancia, duracion, costo, horario_inicio, hora_fin, id_user, id_pasajero, estado) VALUES (alpescab_sequence.nextval, :tipoService, :distancia, :duracion, :costo, :horarioInicio, :horaFin, :idConductor, :idPasajero, 'ASIGNADO')", nativeQuery = true)
    void solicitarServicio(@Param("tipoService") String tipoService, 
                          @Param("distancia") double distancia, 
                          @Param("duracion") int duracion, 
                          @Param("costo") double costo, 
                          @Param("horarioInicio") Date horarioInicio, 
                          @Param("horaFin") Date horaFin, 
                          @Param("idConductor") long idConductor,
                          @Param("idPasajero") long idPasajero);

    // RF9 - REGISTRAR UN VIAJE 
    @Modifying
    @Transactional
    @Query(value = "UPDATE servicio SET distancia = :distancia, duracion = :duracion, costo = :costo, hora_fin = :horaFin WHERE id_service = :idService", nativeQuery = true)
    void registrarViajeCompletado(@Param("idService") long idService, 
                                 @Param("distancia") double distancia, 
                                 @Param("duracion") int duracion, 
                                 @Param("costo") double costo, 
                                 @Param("horaFin") Date horaFin);

    // Para RF8 - 
    // Validar medio de pago para el pasajero - nueva cosa
     @Query(value = "SELECT COUNT(*) FROM usuario_servicio WHERE id_user = :usuarioId AND numero_car IS NOT NULL AND fecha_venci > SYSDATE", nativeQuery = true)
    int tieneMedioPagoValido(@Param("usuarioId") Long usuarioId);
    // Encontrar conductor disponible
    @Query(value = "SELECT v.id_conductor FROM vehiculo v " +
                   "JOIN vehiculo_disponibilidad vd ON v.id_vehiculo = vd.id_vehiculo " +
                   "JOIN disponibilidad d ON vd.id_disponibilidad = d.id_disponibilidad " +
                   "WHERE d.fecha_disp = :fecha " +
                   "AND :hora BETWEEN d.hora_inicio AND d.hora_fin " +
                   "AND v.id_conductor NOT IN (" +
                   "    SELECT s.id_user FROM servicio s " +
                   "    WHERE s.horario_inicio <= :hora AND s.hora_fin >= :hora" +
                   ") FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
    Long encontrarConductorDisponible(@Param("fecha") Date fecha, @Param("hora") Date hora);

       // Actualizar estado del conductor 
    @Modifying
    @Transactional
    @Query(value = "UPDATE conductor SET disponible = 0 WHERE id = :conductorId", nativeQuery = true)
    void marcarConductorOcupado(@Param("conductorId") Long conductorId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE conductor SET disponible = 1 WHERE id = :conductorId", nativeQuery = true)
    void liberarConductor(@Param("conductorId") Long conductorId);

    // Método para obtener el ID del conductor del servicio
    @Query(value = "SELECT id_user FROM servicio WHERE id_service = :idService", nativeQuery = true)
    Long obtenerConductorDelServicio(@Param("idService") long idService);

    @Query(value = "SELECT * FROM servicio", nativeQuery = true)
    Collection<Servicio> darServicios();

    @Query(value = "SELECT * FROM servicio WHERE id_service = :id", nativeQuery = true)
    Servicio darServicio(@Param("id") long id);

    @Query(value = "SELECT * FROM servicio WHERE id_pasajero = :idPasajero ORDER BY horario_inicio DESC", nativeQuery = true)
    Collection<Servicio> darServiciosPorPasajero(@Param("idPasajero") long idPasajero);

    // Consulta para historial de servicios de un conductor
    @Query(value = "SELECT * FROM servicio WHERE id_user = :idConductor ORDER BY horario_inicio DESC", nativeQuery = true)
    Collection<Servicio> darServiciosPorConductor(@Param("idConductor") long idConductor);

    // RFC4 - UTILIZACIÓN DE SERVICIOS POR CIUDAD USANDO RANGO DE FECHAS
    @Query(value = "SELECT s.TIPO_SERVICE, " +
                   "COUNT(*) AS NUM_SERVICIOS, " +
                   "ROUND(COUNT() * 100.0 / SUM(COUNT()) OVER (), 2) AS PORCENTAJE " +
                   "FROM SERVICIO s " +
                   "JOIN SERVICIO_PUNTO sp ON s.ID_SERVICE = sp.ID_SERVICE " +
                   "JOIN PUNTO_GEO pg ON sp.ID_PUNTO = pg.ID_PUNTO " +
                   "WHERE pg.CIUDAD = :ciudad " +
                   "AND s.HORARIO_INICIO BETWEEN TO_DATE(:fechaInicio, 'DD-MM-YYYY') AND TO_DATE(:fechaFin, 'DD-MM-YYYY') " +
                   "GROUP BY s.TIPO_SERVICE " +
                   "ORDER BY NUM_SERVICIOS DESC", nativeQuery = true)
    Collection<Object[]> utilizacionServiciosPorCiudad(
            @Param("ciudad") String ciudad,
            @Param("fechaInicio") String fechaInicio,
            @Param("fechaFin") String fechaFin);
}