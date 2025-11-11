package uniandes.edu.co.proyecto.services;
import uniandes.edu.co.proyecto.entities.*;
import uniandes.edu.co.proyecto.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class ConsultaService {

    @Autowired private ServicioRepository servicioRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    // ---------------------- RFC1: CONSULTAR HISTÓRICO (Versión por defecto) ----------------------
    public List<ServicioEntity> consultarHistoricoUsuario(Long clienteId) throws Exception {
        Optional<UsuarioEntity> userOpt = usuarioRepository.findById(clienteId);
        if (userOpt.isEmpty() || !(userOpt.get() instanceof UsuarioServicioEntity)) {
            throw new Exception("Cliente no encontrado.");
        }
        UsuarioServicioEntity cliente = (UsuarioServicioEntity) userOpt.get();
        return servicioRepository.findByUsuarioCliente(cliente);
    }

    // ---------------------- RFC1 con Nivel de Aislamiento SERIALIZABLE (Punto 3) ----------------------
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<ServicioEntity> consultarHistoricoUsuario_Serializable(Long clienteId) throws Exception {
        // Primera consulta (para observar el efecto de SERIALIZABLE en el escenario de prueba)

        // Temporizador de 30 segundos (para la prueba de concurrencia con RF8)
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Segunda consulta (para observar si la orden de servicio de RF8 aparece) 
        List<ServicioEntity> segundaConsulta = consultarHistoricoUsuario(clienteId);
        
        // En este nivel (SERIALIZABLE), la segunda consulta debería ser idéntica a la primera,
        // sin ver cambios hechos por transacciones concurrentes.
        return segundaConsulta; // Retorna el resultado de la segunda consulta
    }

    // ---------------------- RFC1 con Nivel de Aislamiento READ_COMMITTED (Punto 3) ----------------------
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<ServicioEntity> consultarHistoricoUsuario_ReadCommitted(Long clienteId) throws Exception {
        // Primera consulta
        List<ServicioEntity> primeraConsulta = consultarHistoricoUsuario(clienteId);
        
        // Temporizador de 30 segundos 
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Segunda consulta (para observar el efecto de READ_COMMITTED en el escenario de prueba) 
        List<ServicioEntity> segundaConsulta = consultarHistoricoUsuario(clienteId);
        
        // En este nivel (READ_COMMITTED), la segunda consulta podría mostrar el resultado de una 
        // transacción RF8 que se haya COMMITTED durante la espera.
        return segundaConsulta; // Retorna el resultado de la segunda consulta
    }

    // ---------------------- RFC2: TOP 20 CONDUCTORES ----------------------
    public List<Object[]> findTop20Conductores() {
        return servicioRepository.findTop20Conductores();
    }

    // ---------------------- RFC3: GANANCIAS CONDUCTOR ----------------------
    public List<Object[]> findGananciasConductor(Long conductorId) {
        return servicioRepository.findGananciasConductorPorVehiculoYServicio(conductorId);
    }

    // ---------------------- RFC4: UTILIZACIÓN DE SERVICIOS EN CIUDAD ----------------------
    public List<Object[]> findUsoServicios(String ciudadNombre, Date fechaInicio, Date fechaFin) {
        return servicioRepository.findUsoServiciosPorCiudadYRango(ciudadNombre, fechaInicio, fechaFin);
    }
}