package uniandes.edu.co.proyecto.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uniandes.edu.co.proyecto.modelo.Conductor;

import java.util.Collection;

public interface ConductorRepository extends JpaRepository<Conductor, Integer> {

    // RF3 - REGISTRAR UN USUARIO CONDUCTOR
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO conductor (id_user) VALUES (:idUsuario)", nativeQuery = true)
    void registrarConductor(@Param("idUsuario") long idUsuario);

    @Query(value = "SELECT c.* FROM conductor c JOIN usuario u ON c.id_user = u.id_user", nativeQuery = true)
    Collection<Conductor> darConductores();

    @Query(value = "SELECT c.* FROM conductor c JOIN usuario u ON c.id_user = u.id_user WHERE c.id_user = :id", nativeQuery = true)
    Conductor darConductor(@Param("id") long id);

    //RFC2 - MOSTRAR LOS 20 USUARIOS CONDUCTORES QUE MÁS SERVICIOS HAN PRESTADO EN LA APLICACIÓN(ANTES SOLO ESTABA EN SQL)
    @Query(value = "SELECT u.NOMBRE AS CONDUCTOR, COUNT(s.ID_SERVICE) AS TOTAL_SERVICIOS FROM CONDUCTOR c JOIN USUARIO u ON c.ID_USER = u.ID_USER JOIN SERVICIO s ON c.ID_USER = s.ID_USER GROUP BY u.NOMBRE, c.ID_USER ORDER BY TOTAL_SERVICIOS DESC, u.NOMBRE FETCH FIRST 20 ROWS ONLY", nativeQuery = true)
    Collection<Object[]> darTopConductores();

    //RFC3 - MOSTRAR EL TOTAL DE DINERO OBTENIDO POR USUARIOS CONDUCTORES PARA CADA UNO DE SUSVEHÍCULOS, DISCRIMINADO POR SERVICIOS(ANTES SOLO ESTABA EN SQL)
    @Query(value = "SELECT u.NOMBRE AS CONDUCTOR, u.CEDULA, v.PLACA, s.TIPO_SERVICE, SUM(s.COSTO * 0.6) AS GANANCIA_CONDUCTOR FROM CONDUCTOR c JOIN USUARIO u ON c.ID_USER = u.ID_USER JOIN VEHICULO v ON c.ID_USER = v.ID_CONDUCTOR JOIN SERVICIO s ON c.ID_USER = s.ID_USER GROUP BY u.NOMBRE, u.CEDULA, v.PLACA, s.TIPO_SERVICE ORDER BY u.NOMBRE, v.PLACA, s.TIPO_SERVICE", nativeQuery = true)
    Collection<Object[]> darGananciasConductores();
}