package uniandes.edu.co.proyecto.services;
import uniandes.edu.co.proyecto.entities.*;
import uniandes.edu.co.proyecto.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioTransaccionalService {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ServicioRepository servicioRepository;
    @Autowired private MediosPagoRepository medioDePagoRepository;
    // Otros repositorios necesarios (VehiculoRepository, DisponibilidadRepository...)

    // ---------------------- RF8: SOLICITAR UN SERVICIO ----------------------
    // Debe ser atómica. Si una falla, todas fallan (rollback).
    @Transactional
    public ServicioEntity solicitarServicio(UsuarioServicioEntity cliente, String tipoServicio, PuntoGeoEntity partida, List<PuntoGeoEntity> llegadas, Double costoEstimado) throws Exception {
        
        // 1. Verificar que el usuario tiene un medio de pago registrado. [cite: 88]
        List<MediosPagoEntity> pagos = medioDePagoRepository.findByUsuarioDeServicio(cliente);
        if (pagos.isEmpty()) {
            throw new Exception("RF8 Fallido: El usuario no tiene un medio de pago registrado disponible.");
        }
        // En un proyecto real, se realizaría el cobro aquí (lógica externa).

        // 2. Buscar un conductor disponible para prestar el servicio solicitado. [cite: 89]
        // Nota: La búsqueda debe ser más sofisticada (cercanía, tipo de vehículo).
        // Aquí usamos el método simple del repositorio.
        UsuarioConductorEntity conductorDisponible = usuarioRepository.findConductorDisponible();
        
        if (conductorDisponible == null) {
            throw new Exception("RF8 Fallido: No se encontró un conductor disponible en este momento.");
        }
        
        // 3. Actualizar el estado del conductor. [cite: 90]
        conductorDisponible.setEstadoDisponible(false); // Marcar como NO disponible
        usuarioRepository.save(conductorDisponible);

        // 4. Registrar el inicio del viaje. [cite: 91]
        // Se debe obtener el vehículo que el conductor usará (basado en la disponibilidad, si la hay).
        // Para simplificar, asumimos que el primer vehículo es el asignado.
        VehiculoEntity vehiculoAsignado = conductorDisponible.getVehiculos().get(0); 
        
        ServicioEntity nuevoServicio = new ServicioEntity(
            tipoServicio, 
            costoEstimado, 
            new Date(), // Hora de inicio actual
            conductorDisponible, 
            cliente, 
            vehiculoAsignado, 
            partida, 
            llegadas
        );
        
        // Si todo lo anterior tiene éxito, se registra el servicio.
        return servicioRepository.save(nuevoServicio);
    }
    
    // ---------------------- RF9: REGISTRAR EL FINAL DE UN VIAJE ----------------------
    // También debe ser atómica.
    @Transactional
    public ServicioEntity finalizarServicio(Long servicioId, Double longitudTrayecto) throws Exception {
        Optional<ServicioEntity> servicioOpt = servicioRepository.findById(servicioId);
        
        if (servicioOpt.isEmpty()) {
            throw new Exception("RF9 Fallido: Servicio no encontrado.");
        }
        
        ServicioEntity servicio = servicioOpt.get();
        Date horaFin = new Date();
        
        // 1. Actualizar el registro del viaje del conductor (hora de finalización, longitud, duración). [cite: 93, 64]
        servicio.setHoraFin(horaFin);
        servicio.setLongitudTrayecto(longitudTrayecto);
        long duracionMs = horaFin.getTime() - servicio.getHoraInicio().getTime();
        servicio.setDuracionMinutos(duracionMs / (1000 * 60)); 
        
        // 2. Marcar el conductor disponible nuevamente. [cite: 93]
        UsuarioConductorEntity conductor = servicio.getConductor();
        conductor.setEstadoDisponible(true);
        usuarioRepository.save(conductor);
        
        // 3. Guardar el servicio actualizado
        return servicioRepository.save(servicio);
    }
}
