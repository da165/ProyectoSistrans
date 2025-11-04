package uniandes.edu.co.proyecto.controllers;
import uniandes.edu.co.proyecto.entities.*;
import uniandes.edu.co.proyecto.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alpescab/registro")
public class RegistroController {

    @Autowired
    private RegistroService registroService;

    // ---------------------- RF1: REGISTRAR CIUDAD ----------------------
    @PostMapping("/ciudad")
    public ResponseEntity<?> registrarCiudad(@RequestBody CiudadEntity ciudad) {
        try {
            CiudadEntity nuevaCiudad = registroService.registrarCiudad(ciudad);
            return new ResponseEntity<>(nuevaCiudad, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ---------------------- RF2/RF3: REGISTRAR USUARIOS ----------------------
    @PostMapping("/usuario/cliente")
    public ResponseEntity<?> registrarUsuarioDeServicio(@RequestBody UsuarioServicioEntity cliente) {
        try {
            UsuarioServicioEntity nuevoCliente = registroService.registrarUsuarioDeServicio(cliente);
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al registrar cliente: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/usuario/conductor")
    public ResponseEntity<?> registrarUsuarioConductor(@RequestBody UsuarioConductorEntity conductor) {
        try {
            UsuarioConductorEntity nuevoConductor = registroService.registrarUsuarioConductor(conductor);
            return new ResponseEntity<>(nuevoConductor, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al registrar conductor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ---------------------- RF4: REGISTRAR VEHÍCULO ----------------------
    @PostMapping("/vehiculo")
    public ResponseEntity<?> registrarVehiculo(@RequestBody VehiculoEntity vehiculo) {
        try {
            VehiculoEntity nuevoVehiculo = registroService.registrarVehiculo(vehiculo);
            return new ResponseEntity<>(nuevoVehiculo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ---------------------- RF5: REGISTRAR DISPONIBILIDAD ----------------------
    @PostMapping("/disponibilidad")
    public ResponseEntity<?> registrarDisponibilidad(@RequestBody DisponibilidadEntity disponibilidad) {
        try {
            DisponibilidadEntity nuevaDisponibilidad = registroService.registrarDisponibilidad(disponibilidad);
            return new ResponseEntity<>(nuevaDisponibilidad, HttpStatus.CREATED);
        } catch (Exception e) {
            // RF5: No es posible tener disponibilidades que se superpongan.
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); 
        }
    }

    // ---------------------- RF6: MODIFICAR DISPONIBILIDAD ----------------------
    @PutMapping("/disponibilidad/{id}")
    public ResponseEntity<?> modificarDisponibilidad(@PathVariable Long id, @RequestBody DisponibilidadEntity disponibilidad) {
        try {
            // Aquí se usarían los campos de disponibilidad (horaInicio, horaFin) para la modificación
            registroService.modificarDisponibilidad(id, disponibilidad.getHoraInicio(), disponibilidad.getHoraFin());
            return new ResponseEntity<>("Disponibilidad modificada con éxito.", HttpStatus.OK);
        } catch (Exception e) {
            // RF6: No debe ser posible cambiar la disponibilidad si esta se superpone con otras.
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); 
        }
    }

    // ---------------------- RF7: REGISTRAR PUNTO GEOGRÁFICO ----------------------
    @PostMapping("/punto")
    public ResponseEntity<?> registrarPuntoGeografico(@RequestBody PuntoGeoEntity punto) {
        try {
            PuntoGeoEntity nuevoPunto = registroService.registrarPuntoGeografico(punto);
            return new ResponseEntity<>(nuevoPunto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // ---------------------- RF10/RF11: REGISTRAR REVISIÓN ----------------------
    @PostMapping("/revision")
    public ResponseEntity<?> registrarRevision(@RequestBody RevisionEntity revision) {
        try {
            RevisionEntity nuevaRevision = registroService.registrarRevision(revision);
            return new ResponseEntity<>(nuevaRevision, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}