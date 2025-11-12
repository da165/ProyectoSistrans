package uniandes.edu.co.proyecto.services;
import uniandes.edu.co.proyecto.entities.*;
import uniandes.edu.co.proyecto.repositories.*;
import uniandes.edu.co.proyecto.controllers.DTO.*; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors; 

@Service
public class ConsultaService {

    @Autowired private ServicioRepository servicioRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    // ---------------------- RFC1: CONSULTAR HISTÓRICO (Versión por defecto) ----------------------
    public List<ServicioEntity> consultarHistoricoUsuario(Long clienteId) throws Exception {
        Optional<UsuarioEntity> userOpt = usuarioRepository.findById(clienteId);
        if (userOpt.isEmpty() || !(userOpt.get() instanceof UsuarioServicioEntity)) {
            throw new Exception("Cliente no encontrado o no es un usuario de servicio.");
        }
        UsuarioServicioEntity cliente = (UsuarioServicioEntity) userOpt.get();
        return servicioRepository.findByUsuarioCliente(cliente);
    }

    // ---------------------- RFC1 con Nivel de Aislamiento SERIALIZABLE ----------------------
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<ServicioEntity> consultarHistoricoUsuario_Serializable(Long clienteId) throws Exception {
        List<ServicioEntity> primeraConsulta = consultarHistoricoUsuario(clienteId);
        
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        List<ServicioEntity> segundaConsulta = consultarHistoricoUsuario(clienteId);
        
        return segundaConsulta; 
    }

    // ---------------------- RFC1 con Nivel de Aislamiento READ_COMMITTED ----------------------
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<ServicioEntity> consultarHistoricoUsuario_ReadCommitted(Long clienteId) throws Exception {
        List<ServicioEntity> primeraConsulta = consultarHistoricoUsuario(clienteId);
        
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        List<ServicioEntity> segundaConsulta = consultarHistoricoUsuario(clienteId);
        
        return segundaConsulta; 
    }

    // ---------------------- RFC2: TOP 20 CONDUCTORES (Retorna DTO) ----------------------
    public List<TopConductorDTO> findTop20Conductores() {
        List<Object[]> resultados = servicioRepository.findTop20Conductores();
        
        return resultados.stream()
            .map(result -> new TopConductorDTO(
                ((Number) result[0]).longValue(), 
                ((Number) result[1]).longValue()   
            ))
            .collect(Collectors.toList());
    }

    // ---------------------- RFC3: GANANCIAS CONDUCTOR (Retorna DTO) ----------------------
    public List<GananciaConductorDTO> findGananciasConductor(Long conductorId) {
        List<Object[]> resultados = servicioRepository.findGananciasConductorPorVehiculoYServicio(conductorId);
        
        return resultados.stream()
            .map(result -> new GananciaConductorDTO(
                (String) result[0],                      // placaVehiculo (String)
                (String) result[1],                      // tipoServicio (String)
                ((Number) result[2]).doubleValue()       
            ))
            .collect(Collectors.toList());
    }

    // ---------------------- RFC4: UTILIZACIÓN DE SERVICIOS EN CIUDAD (Retorna DTO) ----------------------
    public List<UtilizacionServiciosDTO> findUsoServicios(String ciudadNombre, Date fechaInicio, Date fechaFin) {
         List<Object[]> resultados = servicioRepository.findUsoServiciosPorCiudadYRango(ciudadNombre, fechaInicio, fechaFin);
         
         
         return resultados.stream()
             .map(result -> {
                 // Manejo de valores nulos o tipos inesperados
                 Long numServicios = result[1] instanceof Number ? ((Number) result[1]).longValue() : 0L;
                 Double porcentaje = result[2] instanceof Number ? ((Number) result[2]).doubleValue() : 0.0;
                 
                 return new UtilizacionServiciosDTO(
                     (String) result[0],                      // tipoServicio (String)
                     numServicios,                            // numeroServicios
                     porcentaje                               // porcentajeUso
                 );
             })
             .collect(Collectors.toList());
    }
}