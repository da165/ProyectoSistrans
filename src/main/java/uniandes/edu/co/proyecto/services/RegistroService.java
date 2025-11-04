package uniandes.edu.co.proyecto.services;
import uniandes.edu.co.proyecto.entities.*;
import uniandes.edu.co.proyecto.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import java.time.DayOfWeek;
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

    // ---------------------- RF1: REGISTRAR CIUDAD ----------------------
    public CiudadEntity registrarCiudad(CiudadEntity ciudad) {
        // Validación básica, aunque en un proyecto real se podría buscar por nombre antes de guardar.
        return ciudadRepository.save(ciudad);
    }

    // ---------------------- RF2/RF3: REGISTRAR USUARIOS ----------------------
    // Usamos el constructor de la clase concreta (UsuarioDeServicio o UsuarioConductor)
    public UsuarioServicioEntity registrarUsuarioDeServicio(UsuarioServicioEntity cliente) {
        return usuarioRepository.save(cliente);
    }
    
    public UsuarioConductorEntity registrarUsuarioConductor(UsuarioConductorEntity conductor) {
        return usuarioRepository.save(conductor);
    }

    // ---------------------- RF4: REGISTRAR VEHÍCULO ----------------------
    public VehiculoEntity registrarVehiculo(VehiculoEntity vehiculo) throws Exception {
        // Lógica de negocio: Un conductor debe existir. La placa debe ser única.
        if (vehiculoRepository.findByPlaca(vehiculo.getPlaca()) != null) {
            throw new Exception("Ya existe un vehículo registrado con esta placa.");
        }
        
        // Se podría añadir lógica para determinar el 'nivelAsignado' (Estándar, Confort, Large) basado en el modelo y capacidad. [cite: 51, 52]
        // ...
        
        return vehiculoRepository.save(vehiculo);
    }

    // ---------------------- RF5/RF6: GESTIÓN DE DISPONIBILIDAD ----------------------
    public DisponibilidadEntity registrarDisponibilidad(DisponibilidadEntity nuevaDisponibilidad) throws Exception {
        // Lógica de negocio (RF5, RF6): No es posible tener disponibilidades que se superpongan para un mismo conductor. [cite: 77, 80]
        
        List<DisponibilidadEntity> superpuestas = disponibilidadRepository.findSuperposedDisponibilidad(
            nuevaDisponibilidad.getVehiculo(), 
            nuevaDisponibilidad.getDiaSemana(), 
            nuevaDisponibilidad.getHoraInicio(), 
            nuevaDisponibilidad.getHoraFin()
        );
        
        if (!superpuestas.isEmpty()) {
            throw new Exception("La disponibilidad se superpone con un horario ya registrado para este conductor.");
        }
        
        // Asumiendo que el vehículo ya tiene su 'nivelAsignado'
        return disponibilidadRepository.save(nuevaDisponibilidad);
    }
    
    public void modificarDisponibilidad(Long idDisponibilidad, LocalTime nuevaHoraInicio, LocalTime nuevaHoraFin) throws Exception {
        Optional<DisponibilidadEntity> dispOpt = disponibilidadRepository.findById(idDisponibilidad);
        if (dispOpt.isEmpty()) {
            throw new Exception("Disponibilidad no encontrada.");
        }

        DisponibilidadEntity actual = dispOpt.get();
        // Lógica de validación de superposición similar a RF5, excluyendo la disponibilidad que se está modificando
        // ... (Implementar la exclusión de la disponibilidad actual en la consulta si es necesario)
        
        actual.setHoraInicio(nuevaHoraInicio);
        actual.setHoraFin(nuevaHoraFin);
        disponibilidadRepository.save(actual);
    }

    // ---------------------- RF7: REGISTRAR PUNTO GEOGRÁFICO ----------------------
    public PuntoGeoEntity registrarPuntoGeografico(PuntoGeoEntity punto) {
        return puntoGeograficoRepository.save(punto);
    }

    // ---------------------- RF10/RF11: REGISTRAR REVISIÓN ----------------------
    public RevisionEntity registrarRevision(RevisionEntity revision) throws Exception {
        // Lógica de negocio: Solo puede haber una revisión por servicio
        if (revisionRepository.findByServicioId(revision.getServicio().getId()) != null) {
            throw new Exception("Ya existe una revisión registrada para este servicio.");
        }
        // Validar que la calificación esté entre 0 y 5. [cite: 59, 61]
        if (revision.getCalificacion() < 0 || revision.getCalificacion() > 5) {
            throw new Exception("La calificación debe estar entre 0 y 5.");
        }
        return revisionRepository.save(revision);
    }
    
    // ---------------------- GESTIÓN DE MEDIOS DE PAGO (Apoyo al RF8) ----------------------
    public MediosPagoEntity registrarMedioDePago(MediosPagoEntity medioDePago) {
        return medioDePagoRepository.save(medioDePago);
    }
}
