package uniandes.edu.co.proyecto.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uniandes.edu.co.proyecto.modelo.ServicioPunto;
import uniandes.edu.co.proyecto.modelo.ServicioPuntoPK;

public interface ServicioPuntoRepository extends JpaRepository<ServicioPunto, ServicioPuntoPK> {

    // Para RF8 - Asignar puntos al servicio
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO servicio_punto (id_service, id_punto) VALUES (:idService, :idPunto)", nativeQuery = true)
    void asignarPuntoServicio(@Param("idService") long idService, @Param("idPunto") long idPunto);

    // Consultas adicionales útiles
    @Query(value = "SELECT * FROM servicio_punto WHERE id_service = :idService", nativeQuery = true)
    java.util.Collection<ServicioPunto> darPuntosPorServicio(@Param("idService") long idService);

    @Query(value = "SELECT * FROM servicio_punto WHERE id_punto = :idPunto", nativeQuery = true)
    java.util.Collection<ServicioPunto> darServiciosPorPunto(@Param("idPunto") long idPunto);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM servicio_punto WHERE id_service = :idService AND id_punto = :idPunto", nativeQuery = true)
    void eliminarPuntoServicio(@Param("idService") long idService, @Param("idPunto") long idPunto);
}