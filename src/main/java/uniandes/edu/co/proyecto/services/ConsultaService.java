package uniandes.edu.co.proyecto.services;
import uniandes.edu.co.proyecto.entities.*;
import uniandes.edu.co.proyecto.repositories.*;
import uniandes.edu.co.proyecto.controllers.DTO.*; // Nueva importación de DTOs

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors; // Necesario para el mapeo a DTOs

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

    // ---------------------- RFC1 con Nivel de Aislamiento SERIALIZABLE (Punto 3) ----------------------
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<ServicioEntity> consultarHistoricoUsuario_Serializable(Long clienteId) throws Exception {
        // Primera consulta (El nivel SERIALIZABLE debería prevenir la "lectura fantasma")
        List<ServicioEntity> primeraConsulta = consultarHistoricoUsuario(clienteId);
        
        // Temporizador de 30 segundos
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Segunda consulta: si una transacción RF8 intenta insertar un nuevo servicio aquí,
        // SERIALIZABLE la forzará a esperar o fallar (dependiendo de la BD).
        List<ServicioEntity> segundaConsulta = consultarHistoricoUsuario(clienteId);
        
        return segundaConsulta; 
    }

    // ---------------------- RFC1 con Nivel de Aislamiento READ_COMMITTED (Punto 3) ----------------------
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<ServicioEntity> consultarHistoricoUsuario_ReadCommitted(Long clienteId) throws Exception {
        // Primera consulta (Lee solo lo commiteado)
        List<ServicioEntity> primeraConsulta = consultarHistoricoUsuario(clienteId);
        
        // Temporizador de 30 segundos
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Segunda consulta (Si una transacción RF8 se COMMITTED durante la espera, la verá aquí)
        List<ServicioEntity> segundaConsulta = consultarHistoricoUsuario(clienteId);
        
        return segundaConsulta; // Retorna el resultado de la segunda consulta
    }

    // ---------------------- RFC2: TOP 20 CONDUCTORES (Retorna DTO) ----------------------
    public List<TopConductorDTO> findTop20Conductores() {
        // El repositorio devuelve List<Object[]>. Se mapea a DTO.
        List<Object[]> resultados = servicioRepository.findTop20Conductores();
        
        return resultados.stream()
            .map(result -> new TopConductorDTO(
                ((Number) result[0]).longValue(),  // conductorId
                ((Number) result[1]).longValue()   // numeroServicios
            ))
            .collect(Collectors.toList());
    }

    // ---------------------- RFC3: GANANCIAS CONDUCTOR (Retorna DTO) ----------------------
    public List<GananciaConductorDTO> findGananciasConductor(Long conductorId) {
        // El repositorio devuelve List<Object[]>. Se mapea a DTO.
        List<Object[]> resultados = servicioRepository.findGananciasConductorPorVehiculoYServicio(conductorId);
        
        return resultados.stream()
            .map(result -> new GananciaConductorDTO(
                (String) result[0],                      // placaVehiculo
                (String) result[1],                      // tipoServicio
                ((Number) result[2]).doubleValue()       // gananciasTotales (Se usa Number para manejo flexible de tipos numéricos de la BD)
            ))
            .collect(Collectors.toList());
    }

    // ---------------------- RFC4: UTILIZACIÓN DE SERVICIOS EN CIUDAD (Retorna DTO) ----------------------
    public List<UtilizacionServiciosDTO> findUsoServicios(String ciudadNombre, Date fechaInicio, Date fechaFin) {
         // El repositorio devuelve List<Object[]>. Se mapea a DTO.
        List<Object[]> resultados = servicioRepository.findUsoServiciosPorCiudadYRango(ciudadNombre, fechaInicio, fechaFin);
        
        return resultados.stream()
            .map(result -> new UtilizacionServiciosDTO(
                (String) result[0],                      // tipoServicio
                ((Number) result[1]).longValue(),        // numeroServicios
                ((Number) result[2]).doubleValue()       // porcentajeUso
            ))
            .collect(Collectors.toList());
    }
}