package uniandes.edu.co.proyecto.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uniandes.edu.co.proyecto.modelo.Usuario;

import java.util.Collection;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // RF2 - REGISTRAR USUARIO DE SERVICIOS 
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO usuario_servicio (id_user, nombre, correo, celular, cedula) VALUES (alpescab_sequence.nextval, :nombre, :correo, :celular, :cedula)", nativeQuery = true)
    void registrarUsuarioServicio(@Param("nombre") String nombre,
                                 @Param("correo") String correo,
                                 @Param("celular") String celular,
                                 @Param("cedula") String cedula);

    // Obtener último ID insertado
    @Query(value = "SELECT alpescab_sequence.currval FROM dual", nativeQuery = true)
    Long getLastUserId();

    // Verificar si ya existe usuario con misma cédula
    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE u.cedula = :cedula")
    boolean existeUsuarioConCedula(@Param("cedula") String cedula);

    // RFC1-  CONSULTAR EL HISTÓRICO DE TODOS LOS SERVICIOS PEDIDOS POR UN USUARIO(ANTES ESTABA SOLO EN SQL)

    @Query(value = "SELECT s.ID_SERVICE, s.TIPO_SERVICE, s.DISTANCIA, s.DURACION, s.COSTO, s.HORARIO_INICIO, s.HORA_FIN, pg.DIRECCION AS PUNTO, pg.CIUDAD, u_conductor.NOMBRE AS CONDUCTOR FROM SERVICIO s JOIN USUARIO u_pasajero ON s.ID_PASAJERO = u_pasajero.ID_USER JOIN USUARIO u_conductor ON s.ID_USER = u_conductor.ID_USER JOIN SERVICIO_PUNTO sp ON s.ID_SERVICE = sp.ID_SERVICE JOIN PUNTO_GEO pg ON sp.ID_PUNTO = pg.ID_PUNTO WHERE u_pasajero.CEDULA = :cedula ORDER BY s.HORARIO_INICIO DESC", nativeQuery = true)
    Collection<Object[]> darServiciosUsuario(@Param("cedula") String cedula);

    
}