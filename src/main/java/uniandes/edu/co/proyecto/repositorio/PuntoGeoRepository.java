package uniandes.edu.co.proyecto.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uniandes.edu.co.proyecto.modelo.PuntoGeo;

import java.util.Collection;

public interface PuntoGeoRepository extends JpaRepository<PuntoGeo, Integer> {

    // RF7 - REGISTRAR UN PUNTO GEOGRÁFICO (incluye ciudad)
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO punto_geo (id_punto, coordenadas, direccion, ciudad) VALUES (alpescab_sequence.nextval, :coordenadas, :direccion, :ciudad)", nativeQuery = true)
    void registrarPuntoGeo(@Param("coordenadas") String coordenadas, 
                          @Param("direccion") String direccion, 
                          @Param("ciudad") String ciudad);

    // Consultas para ciudades
    @Query(value = "SELECT DISTINCT ciudad FROM punto_geo ORDER BY ciudad", nativeQuery = true)
    Collection<String> darCiudades();

    @Query(value = "SELECT * FROM punto_geo WHERE ciudad = :ciudad", nativeQuery = true)
    Collection<PuntoGeo> darPuntosPorCiudad(@Param("ciudad") String ciudad);

    // RF1 - REGISTRAR UNA CIUDAD (implícito al crear un punto en una ciudad nueva)
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO punto_geo (id_punto, coordenadas, direccion, ciudad) VALUES (alpescab_sequence.nextval, '0,0', 'Centro de :ciudad', :ciudad)", nativeQuery = true)
    void registrarCiudad(@Param("ciudad") String ciudad);

    // Verificar si una ciudad existe
    @Query(value = "SELECT COUNT(*) FROM punto_geo WHERE ciudad = :ciudad", nativeQuery = true)
    int existeCiudad(@Param("ciudad") String ciudad);
}
