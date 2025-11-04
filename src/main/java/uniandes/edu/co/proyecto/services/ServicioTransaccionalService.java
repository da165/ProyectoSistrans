package uniandes.edu.co.proyecto.services;
import uniandes.edu.co.proyecto.entities.*;
import uniandes.edu.co.proyecto.repositories.*;
import uniandes.edu.co.proyecto.controllers.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
//import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServicioTransaccionalService {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ServicioRepository servicioRepository;
    @Autowired private MediosPagoRepository medioDePagoRepository;
    @Autowired private PuntoGeoRepository puntoGeograficoRepository;

    // ---------------------- RF8: SOLICITAR UN SERVICIO (TRANSACCIONAL) ----------------------
    // Debe ser atómica. Si una falla, todas fallan (rollback).
    @Transactional
    public ServicioEntity solicitarServicio(SolicitudServicioDTO solicitud) throws Exception {
        
        // 1. Mapeo: Obtener las entidades a partir de los IDs del DTO.
        
        // Obtener Cliente y validar tipo de usuario
        UsuarioEntity clienteGenerico = usuarioRepository.findById(solicitud.getClienteId())
                .orElseThrow(() -> new Exception("RF8 Fallido: Cliente con ID " + solicitud.getClienteId() + " no encontrado."));
        
        if (!(clienteGenerico instanceof UsuarioServicioEntity cliente)) {
            throw new Exception("RF8 Fallido: El ID proporcionado no pertenece a un Usuario de Servicio.");
        }
        
        // Obtener Punto de Partida
        PuntoGeoEntity partida = puntoGeograficoRepository.findById(solicitud.getPuntoPartidaId())
                .orElseThrow(() -> new Exception("RF8 Fallido: Punto de partida no encontrado."));

        // Obtener Puntos de Llegada
        List<PuntoGeoEntity> llegadas = solicitud.getPuntosLlegadaIds().stream()
                .map(id -> puntoGeograficoRepository.findById(id).orElse(null))
                .filter(p -> p != null)
                .collect(Collectors.toList());

        if (llegadas.isEmpty()) {
             throw new Exception("RF8 Fallido: Se requiere al menos un punto de llegada válido.");
        }
        
        // 2. Lógica de Negocio y Consistencia
        
        // Verificar que el usuario tiene un medio de pago registrado.
        List<MediosPagoEntity> pagos = medioDePagoRepository.findByUsuarioDeServicio(cliente);
        if (pagos.isEmpty()) {
            throw new Exception("RF8 Fallido: El usuario no tiene un medio de pago registrado disponible.");
        }

        // Buscar un conductor disponible (Implementación simplificada)
        UsuarioConductorEntity conductorDisponible = usuarioRepository.findConductorDisponible();
        
        if (conductorDisponible == null || conductorDisponible.getVehiculos().isEmpty()) {
            throw new Exception("RF8 Fallido: No se encontró un conductor o vehículo disponible en este momento.");
        }
        
        // 3. Modificaciones Atómicas
        
        // Actualizar el estado del conductor (Importante para la atomicidad)
        conductorDisponible.setEstadoDisponible(false); // Marcar como NO disponible
        usuarioRepository.save(conductorDisponible);

        // Asignar el vehículo (se asume el primer vehículo para el ejemplo)
        VehiculoEntity vehiculoAsignado = conductorDisponible.getVehiculos().get(0); 
        
        // 4. Registrar el inicio del viaje.
        ServicioEntity nuevoServicio = new ServicioEntity(
            solicitud.getTipoServicio(), 
            solicitud.getCostoEstimado(), 
            new Date(), // Hora de inicio actual
            conductorDisponible, 
            cliente, 
            vehiculoAsignado, 
            partida, 
            llegadas
        );
        
        // Se guarda el registro del servicio (Si esto falla, todo el RF8 hace Rollback)
        return servicioRepository.save(nuevoServicio);
    }
    
    // ---------------------- RF9: REGISTRAR EL FINAL DE UN VIAJE (TRANSACCIONAL) ----------------------
    @Transactional
    public ServicioEntity finalizarServicio(Long servicioId, Double longitudTrayecto) throws Exception {
        ServicioEntity servicio = servicioRepository.findById(servicioId)
            .orElseThrow(() -> new Exception("RF9 Fallido: Servicio con ID " + servicioId + " no encontrado."));
        
        Date horaFin = new Date();
        
        // 1. Actualizar el registro del viaje
        servicio.setHoraFin(horaFin);
        servicio.setLongitudTrayecto(longitudTrayecto);
        long duracionMs = horaFin.getTime() - servicio.getHoraInicio().getTime();
        servicio.setDuracionMinutos(duracionMs / (1000 * 60)); // Duración en minutos
        
        // 2. Marcar el conductor disponible nuevamente (Transaccional)
        UsuarioConductorEntity conductor = servicio.getConductor();
        conductor.setEstadoDisponible(true);
        usuarioRepository.save(conductor);
        
        // 3. Guardar el servicio actualizado
        return servicioRepository.save(servicio);
    }
}