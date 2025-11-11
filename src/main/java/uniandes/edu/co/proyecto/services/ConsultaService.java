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

    // RFC1: CONSULTAR HISTÓRICO (Versión por defecto)
    public List<ServicioEntity> consultarHistoricoUsuario(Long clienteId) throws Exception {
        Optional<UsuarioEntity> userOpt = usuarioRepository.findById(clienteId);
        if (userOpt.isEmpty() || !(userOpt.get() instanceof UsuarioServicioEntity)) {
            throw new Exception("Cliente no encontrado o no es un usuario de servicio.");
        }
        UsuarioServicioEntity cliente = (UsuarioServicioEntity) userOpt.get();
        return servicioRepository.findByUsuarioCliente(cliente);
    }

    //  RFC1 con Nivel de Aislamiento SERIALIZABLE (Punto 3) 
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

    //  RFC1 con Nivel de Aislamiento READ_COMMITTED (Punto 3)
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

    // RFC2: TOP 20 CONDUCTORES
    public List<Object[]> findTop20Conductores() {
        return servicioRepository.findTop20Conductores();
    }

    //  RFC3: GANANCIAS CONDUCTOR
    public List<Object[]> findGananciasConductor(Long conductorId) {
        return servicioRepository.findGananciasConductorPorVehiculoYServicio(conductorId);
    }

    // RFC4: UTILIZACIÓN DE SERVICIOS EN CIUDAD
    public List<Object[]> findUsoServicios(String ciudadNombre, Date fechaInicio, Date fechaFin) {
        return servicioRepository.findUsoServiciosPorCiudadYRango(ciudadNombre, fechaInicio, fechaFin);
    }
}