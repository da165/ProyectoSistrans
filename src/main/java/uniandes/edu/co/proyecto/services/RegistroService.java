package uniandes.edu.co.proyecto.services;
import uniandes.edu.co.proyecto.entities.*;
import uniandes.edu.co.proyecto.repositories.*;
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

    // ---------------------- RF1: REGISTRAR CIUDAD ----------------------
    public CiudadEntity registrarCiudad(CiudadEntity ciudad) {
        // Validación básica, aunque en un proyecto real se podría buscar por nombre antes de guardar.
        return ciudadRepository.save(ciudad);
    }

    // ---------------------- RF2/RF3: REGISTRAR USUARIOS ----------------------
    // Usamos el constructor de la clase concreta (UsuarioDeServicio o UsuarioConductor)
    public UsuarioServicioEntity registrarUsuarioDeServicio(UsuarioServicioEntity cliente) throws Exception {
        // Validación: Cédula y Correo Único (ya en @Column(unique=true) y el findByNumeroCedula en repo)
        if (usuarioRepository.findByNumeroCedula(cliente.getNumeroCedula()) != null) {
            throw new Exception("Ya existe un usuario registrado con esa cédula.");
        }
        return usuarioRepository.save(cliente);
    }

    public UsuarioConductorEntity registrarUsuarioConductor(UsuarioConductorEntity conductor) throws Exception {
        // Validación: Cédula y Correo Único
        if (usuarioRepository.findByNumeroCedula(conductor.getNumeroCedula()) != null) {
            throw new Exception("Ya existe un usuario registrado con esa cédula.");
        }
        return usuarioRepository.save(conductor);
    }

    // ---------------------- RF4: REGISTRAR VEHÍCULO ----------------------
    public VehiculoEntity registrarVehiculo(VehiculoEntity vehiculo) throws Exception {
        // Validación: Placa Única
        if (vehiculoRepository.findByPlaca(vehiculo.getPlaca()) != null) {
            throw new Exception("Ya existe un vehículo registrado con esa placa.");
        }
        // Validación: Conductor existe y es de tipo Conductor
        Optional<UsuarioEntity> conductorOpt = usuarioRepository.findById(vehiculo.getConductor().getId());
        if (conductorOpt.isEmpty() || !(conductorOpt.get() instanceof UsuarioConductorEntity)) {
            throw new Exception("El ID del conductor no es válido.");
        }
        return vehiculoRepository.save(vehiculo);
    }
    
    // ---------------------- RF5: REGISTRAR DISPONIBILIDAD ----------------------
    public DisponibilidadEntity registrarDisponibilidad(DisponibilidadEntity disponibilidad) throws Exception {
        // Lógica de negocio: No debe superponerse con ninguna disponibilidad existente.
        List<DisponibilidadEntity> superpuestas = disponibilidadRepository.findSuperposedDisponibilidad(
            disponibilidad.getVehiculo(),
            disponibilidad.getDiaSemana(),
            disponibilidad.getHoraInicio(),
            disponibilidad.getHoraFin()
        );
        
        if (!superpuestas.isEmpty()) {
            throw new Exception("RF5 Fallido: La nueva disponibilidad se superpone con una franja horaria existente.");
        }
        
        return disponibilidadRepository.save(disponibilidad);
    }

    // ---------------------- RF6: MODIFICAR DISPONIBILIDAD ----------------------
    public void modificarDisponibilidad(Long id, LocalTime nuevaHoraInicio, LocalTime nuevaHoraFin) throws Exception {
        DisponibilidadEntity actual = disponibilidadRepository.findById(id)
            .orElseThrow(() -> new Exception("Disponibilidad con ID " + id + " no encontrada."));

        // Lógica de negocio: Modificar el horario solo si no se superpone con OTRAS franjas horarias.
        
        // 1. Consulta todas las disponibilidades superpuestas.
        List<DisponibilidadEntity> superpuestas = disponibilidadRepository.findSuperposedDisponibilidad(
            actual.getVehiculo(),
            actual.getDiaSemana(),
            nuevaHoraInicio,
            nuevaHoraFin
        );

        // 2. Filtra la lista para excluir el registro que se está modificando.        
        for (DisponibilidadEntity superpuesta : superpuestas) {
            if (!superpuesta.getId().equals(actual.getId())) {
                throw new Exception("RF6 Fallido: La nueva franja horaria se superpone con otra disponibilidad existente (ID: " + superpuesta.getId() + ").");
            }
        }
        
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
        if (revisionRepository.findByServicio_Id(revision.getServicio().getId()) != null) {
            throw new Exception("Ya existe una revisión registrada para este servicio.");
        }
        // Validar que la calificación esté entre 0 y 5.
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
