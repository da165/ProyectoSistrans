package uniandes.edu.co.proyecto.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uniandes.edu.co.proyecto.modelo.Revision;

import java.util.Collection;

public interface RevisionRepository extends JpaRepository<Revision, Integer> {

    // RF10 - REVISIÓN POR PASAJERO
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO revision (id_revision, evaluacion, comentario, id_usuario) VALUES (alpescab_sequence.nextval, :evaluacion, :comentario, :idUsuario)", nativeQuery = true)
    void dejarRevisionPasajero(@Param("evaluacion") int evaluacion, 
                              @Param("comentario") String comentario, 
                              @Param("idUsuario") long idUsuario);

    // RF11 - REVISIÓN POR CONDUCTOR
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO revision (id_revision, evaluacion, comentario, id_usuario) VALUES (alpescab_sequence.nextval, :evaluacion, :comentario, :idUsuario)", nativeQuery = true)
    void dejarRevisionConductor(@Param("evaluacion") int evaluacion, 
                               @Param("comentario") String comentario, 
                               @Param("idUsuario") long idUsuario);

    @Query(value = "SELECT * FROM revision WHERE id_usuario = :idUsuario", nativeQuery = true)
    Collection<Revision> darRevisionesPorUsuario(@Param("idUsuario") long idUsuario);
}
