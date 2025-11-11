package uniandes.edu.co.proyecto.services;
import uniandes.edu.co.proyecto.entities.*;
import uniandes.edu.co.proyecto.repositories.*;
import uniandes.edu.co.proyecto.controllers.DTO.DisponibilidadDTO; 
import uniandes.edu.co.proyecto.controllers.DTO.RevisionDTO;      
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RegistroService {

    @Autowired private CiudadRepository ciudadRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private VehiculoRepository vehiculoRepository;
    @Autowired private DisponibilidadRepository disponibilidadRepository;
    @Autowired private PuntoGeoRepository puntoGeograficoRepository;
    @Autowired private RevisionRepository revisionRepository;
    @Autowired private MediosPagoRepository medioDePagoRepository;
    @Autowired private ServicioRepository servicioRepository; // NECESARIO para RF10/RF11

    // ---------------------- RF1: REGISTRAR CIUDAD ----------------------
    public CiudadEntity registrarCiudad(CiudadEntity ciudad) {
        return ciudadRepository.save(ciudad);
    }

    // ---------------------- RF2/RF3: REGISTRAR USUARIOS ----------------------
    public UsuarioServicioEntity registrarUsuarioDeServicio(UsuarioServicioEntity cliente) {
        return usuarioRepository.save(cliente);
    }
    
    public UsuarioConductorEntity registrarUsuarioConductor(UsuarioConductorEntity conductor) {
        return usuarioRepository.save(conductor);
    }

    // ---------------------- RF4: REGISTRAR VEHÍCULO ----------------------
    public VehiculoEntity registrarVehiculo(VehiculoEntity vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }

    // ---------------------- RF5: REGISTRAR DISPONIBILIDAD (CORREGIDO) ----------------------
    public DisponibilidadEntity registrarDisponibilidad(DisponibilidadDTO disponibilidadDTO) throws Exception {
        
        // 1. Obtener Vehículo (CRÍTICO: Causa del error 500)
        VehiculoEntity vehiculo = vehiculoRepository.findById(disponibilidadDTO.getVehiculoId())
            .orElseThrow(() -> new Exception("RF5 Fallido: Vehículo con ID " + disponibilidadDTO.getVehiculoId() + " no encontrado."));

        // 2. Mapear DTO a Entity y validar formatos
        DayOfWeek diaSemana;
        LocalTime horaInicio;
        LocalTime horaFin;
        try {
            diaSemana = DayOfWeek.valueOf(disponibilidadDTO.getDiaSemana());
            horaInicio = LocalTime.parse(disponibilidadDTO.getHoraInicio());
            horaFin = LocalTime.parse(disponibilidadDTO.getHoraFin());
        } catch (IllegalArgumentException e) {
            throw new Exception("RF5 Fallido: Formato de día o tiempo inválido.");
        }

        // 3. Verificar superposición (Lógica de negocio RF5)
        List<DisponibilidadEntity> superpuestas = disponibilidadRepository.findSuperposedDisponibilidad(
            vehiculo, 
            diaSemana, 
            horaInicio, 
            horaFin
        );

        if (!superpuestas.isEmpty()) {
            throw new Exception("RF5 Fallido: La nueva disponibilidad se superpone con una franja existente.");
        }
        
        // 4. Crear y Guardar la entidad
        DisponibilidadEntity nuevaDisponibilidad = new DisponibilidadEntity(
            diaSemana, 
            horaInicio, 
            horaFin, 
            disponibilidadDTO.getTipoServicio(),
            vehiculo 
        );

        return disponibilidadRepository.save(nuevaDisponibilidad);
    }

    // ---------------------- RF6: MODIFICAR DISPONIBILIDAD (CORREGIDO) ----------------------
    public void modificarDisponibilidad(Long id, String nuevaHoraInicioStr, String nuevaHoraFinStr) throws Exception {
        DisponibilidadEntity actual = disponibilidadRepository.findById(id)
            .orElseThrow(() -> new Exception("RF6 Fallido: Disponibilidad con ID " + id + " no encontrada."));
        
        LocalTime nuevaHoraInicio;
        LocalTime nuevaHoraFin;
        try {
            nuevaHoraInicio = LocalTime.parse(nuevaHoraInicioStr);
            nuevaHoraFin = LocalTime.parse(nuevaHoraFinStr);
        } catch (Exception e) {
            throw new Exception("RF6 Fallido: Formato de hora inválido.");
        }

        
        VehiculoEntity vehiculo = actual.getVehiculo();
        DayOfWeek diaSemana = actual.getDiaSemana();
        
        List<DisponibilidadEntity> superpuestas = disponibilidadRepository.findSuperposedDisponibilidadExcluyendoId(
            id, 
            vehiculo, 
            diaSemana, 
            nuevaHoraInicio, 
            nuevaHoraFin
        );
        
        if (!superpuestas.isEmpty()) {
            throw new Exception("RF6 Fallido: La modificación se superpone con otra disponibilidad existente.");
        }
        
        actual.setHoraInicio(nuevaHoraInicio);
        actual.setHoraFin(nuevaHoraFin);
        disponibilidadRepository.save(actual);
    }

    // ---------------------- RF7: REGISTRAR PUNTO GEOGRÁFICO ----------------------
    public PuntoGeoEntity registrarPuntoGeografico(PuntoGeoEntity punto) {
        return puntoGeograficoRepository.save(punto);
    }

    // ---------------------- RF10/RF11: REGISTRAR REVISIÓN (CORREGIDO) ----------------------
    public RevisionEntity registrarRevision(RevisionDTO revisionDTO) throws Exception {
        
        // 1. Obtener Servicio asociado a la revisión
        ServicioEntity servicio = servicioRepository.findById(revisionDTO.getServicioId())
            .orElseThrow(() -> new Exception("RF10/RF11 Fallido: Servicio con ID " + revisionDTO.getServicioId() + " no encontrado."));

        // 2. Determinar revisor (Cliente) y revisado (Conductor) a partir del servicio
        UsuarioServicioEntity clienteRevisor = servicio.getUsuarioCliente();
        UsuarioConductorEntity conductorRevisado = servicio.getConductor();

        if (clienteRevisor == null || conductorRevisado == null) {
            throw new Exception("RF10/RF11 Fallido: El servicio no tiene un cliente o conductor asociado, no se puede registrar la revisión.");
        }
        
        // 3. Lógica de negocio: Solo puede haber una revisión por servicio
        if (revisionRepository.findByServicio_Id(servicio.getId()) != null) {
            throw new Exception("RF10/RF11 Fallido: Ya existe una revisión registrada para este servicio.");
        }
        
        // 4. Validar calificación
        Integer calificacion = revisionDTO.getCalificacion();
        if (calificacion == null || calificacion < 0 || calificacion > 5) {
            throw new Exception("RF10/RF11 Fallido: La calificación debe estar entre 0 y 5.");
        }
        
        // 5. Crear la entidad
        RevisionEntity revision = new RevisionEntity(
            calificacion, 
            revisionDTO.getComentario(), 
            servicio,                 
            clienteRevisor,           
            conductorRevisado         
        );
        
        return revisionRepository.save(revision);
    }
    
    // ---------------------- GESTIÓN DE MEDIOS DE PAGO (Apoyo al RF8) ----------------------
    public MediosPagoEntity registrarMedioDePago(MediosPagoEntity medioDePago) {
        return medioDePagoRepository.save(medioDePago);
    }
}
