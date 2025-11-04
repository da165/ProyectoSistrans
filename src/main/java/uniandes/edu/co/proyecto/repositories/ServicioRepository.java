package uniandes.edu.co.proyecto.repositories;
import uniandes.edu.co.proyecto.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ServicioRepository extends JpaRepository<ServicioEntity, Long> {

    // --- RFC1: Consultar el histórico de todos los servicios pedidos por un usuario
    List<ServicioEntity> findByUsuarioCliente(UsuarioServicioEntity usuarioCliente);

    // --- RFC2: Mostrar los 20 usuarios conductores que más servicios han prestado
    // Retorna una lista de arreglos [conductorId, numeroServicios] o se mapea a un DTO.
    @Query(value = "SELECT s.ID_CONDUCTOR, COUNT(s.ID) AS num_servicios " +
                   "FROM SERVICIOS s " +
                   "GROUP BY s.ID_CONDUCTOR " +
                   "ORDER BY num_servicios DESC " +
                   "FETCH FIRST 20 ROWS ONLY", // Sintaxis de Oracle 12c para LIMIT/TOP
           nativeQuery = true)
    List<Object[]> findTop20Conductores();


    // --- RFC3: Dinero obtenido por usuarios conductores por vehículo y tipo de servicio
    // Se debe restar la comisión de ALPESCAB (40% para el conductor = 60% del valor total [cite: 37])
    @Query(value = "SELECT v.PLACA, s.TIPO_SERVICIO, SUM(s.COSTO_TOTAL * 0.60) AS ganancias " +
                   "FROM SERVICIOS s JOIN VEHICULOS v ON s.ID_VEHICULO = v.ID " +
                   "WHERE v.ID_CONDUCTOR = :conductorId " +
                   "GROUP BY v.PLACA, s.TIPO_SERVICIO",
           nativeQuery = true)
    List<Object[]> findGananciasConductorPorVehiculoYServicio(@Param("conductorId") Long conductorId);

    // --- RFC4: Utilización de servicios en una ciudad durante un rango de fechas
    @Query(value = "SELECT s.TIPO_SERVICIO, COUNT(s.ID) AS num_servicios_ciudad, " +
                   "       (CAST(COUNT(s.ID) AS DECIMAL) * 100 / (SELECT COUNT(ID) FROM SERVICIOS WHERE HORA_INICIO BETWEEN :fechaInicio AND :fechaFin)) AS porcentaje_uso " +
                   "FROM SERVICIOS s JOIN PUNTOS_GEOGRAFICOS pg ON s.ID_PUNTO_PARTIDA = pg.ID " +
                   "JOIN CIUDADES c ON pg.ID_CIUDAD = c.ID " +
                   "WHERE c.NOMBRE = :ciudadNombre AND s.HORA_INICIO BETWEEN :fechaInicio AND :fechaFin " +
                   "GROUP BY s.TIPO_SERVICIO " +
                   "ORDER BY num_servicios_ciudad DESC",
           nativeQuery = true)
    List<Object[]> findUsoServiciosPorCiudadYRango(
            @Param("ciudadNombre") String ciudadNombre,
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin);
}