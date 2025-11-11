package uniandes.edu.co.proyecto.controllers; // Paquete del controlador de operaciones de registro
import uniandes.edu.co.proyecto.entities.*; // Entidades persistentes usadas como @RequestBody/@ResponseBody
import uniandes.edu.co.proyecto.services.*; // Servicios de dominio para registrar entidades
import org.springframework.beans.factory.annotation.Autowired;     // Inyección de dependencias
import org.springframework.http.HttpStatus;  // Códigos HTTP
import org.springframework.http.ResponseEntity; // Respuestas HTTP tipadas
import org.springframework.web.bind.annotation.*; // Anotaciones REST (@PostMapping, @PutMapping, etc.)

@RestController // Controlador REST
@RequestMapping("/api/alpescab/registro") // Prefijo base de rutas de registro
public class RegistroController {

    @Autowired // Servicio que implementa la lógica de registro
    private RegistroService registroService;

    //  RF1: REGISTRAR CIUDAD 
    @PostMapping("/ciudad") // POST /api/alpescab/registro/ciudad
    public ResponseEntity<?> registrarCiudad(@RequestBody CiudadEntity ciudad) {  // Body: JSON de CiudadEntity
        try {
            CiudadEntity nuevaCiudad = registroService.registrarCiudad(ciudad);   // Invoca al servicio de registro
            return new ResponseEntity<>(nuevaCiudad, HttpStatus.CREATED); // 201 con la ciudad creada
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 con mensaje
        }
    }

    //  RF2/RF3: REGISTRAR USUARIOS 
    @PostMapping("/usuario/cliente") // POST /api/alpescab/registro/usuario/cliente
    public ResponseEntity<?> registrarUsuarioDeServicio(@RequestBody UsuarioServicioEntity cliente) {
        try {
            UsuarioServicioEntity nuevoCliente = registroService.registrarUsuarioDeServicio(cliente);
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED); // 201 con el cliente creado
        } catch (Exception e) {
            return new ResponseEntity<>( // 500 con mensaje específico para cliente
                "Error al registrar cliente: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/usuario/conductor") // POST /api/alpescab/registro/usuario/conductor
    public ResponseEntity<?> registrarUsuarioConductor(@RequestBody UsuarioConductorEntity conductor) {
        try {
            UsuarioConductorEntity nuevoConductor = registroService.registrarUsuarioConductor(conductor);
            return new ResponseEntity<>(nuevoConductor, HttpStatus.CREATED); // 201 con el conductor creado
        } catch (Exception e) {
            return new ResponseEntity<>( // 500 con mensaje específico para conductor
                "Error al registrar conductor: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // RF4: REGISTRAR VEHÍCULO 
    @PostMapping("/vehiculo") // POST /api/alpescab/registro/vehiculo
    public ResponseEntity<?> registrarVehiculo(@RequestBody VehiculoEntity vehiculo) {
        try {
            VehiculoEntity nuevoVehiculo = registroService.registrarVehiculo(vehiculo);
            return new ResponseEntity<>(nuevoVehiculo, HttpStatus.CREATED); // 201 con el vehículo creado
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);  // 400 si faltan datos o reglas
        }
    }

    // RF5: REGISTRAR DISPONIBILIDAD 
    @PostMapping("/disponibilidad") // POST /api/alpescab/registro/disponibilidad
    public ResponseEntity<?> registrarDisponibilidad(@RequestBody DisponibilidadEntity disponibilidad) {
        try {
            DisponibilidadEntity nuevaDisponibilidad = registroService.registrarDisponibilidad(disponibilidad);
            return new ResponseEntity<>(nuevaDisponibilidad, HttpStatus.CREATED); // 201 con disponibilidad
        } catch (Exception e) {
            // RF5: No es posible tener disponibilidades que se superpongan.
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);  // 400 por solapamiento/regla
        }
    }

    // RF6: MODIFICAR DISPONIBILIDAD 
    @PutMapping("/disponibilidad/{id}") // PUT /api/alpescab/registro/disponibilidad/{id}
    public ResponseEntity<?> modificarDisponibilidad(@PathVariable Long id, @RequestBody DisponibilidadEntity disponibilidad) {
        try {
            // Utiliza los campos de la entidad recibida para actualizar el rango horario
            registroService.modificarDisponibilidad(id, disponibilidad.getHoraInicio(), disponibilidad.getHoraFin());
            return new ResponseEntity<>("Disponibilidad modificada con éxito.", HttpStatus.OK); // 200 texto OK
        } catch (Exception e) {
            // RF6: No debe ser posible cambiar si se superpone con otras disponibilidades
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);  // 400 con explicación
        }
    }

    // RF7: REGISTRAR PUNTO GEOGRÁFICO 
    @PostMapping("/punto") // POST /api/alpescab/registro/punto
    public ResponseEntity<?> registrarPuntoGeografico(@RequestBody PuntoGeoEntity punto) {
        try {
            PuntoGeoEntity nuevoPunto = registroService.registrarPuntoGeografico(punto);
            return new ResponseEntity<>(nuevoPunto, HttpStatus.CREATED); // 201 con el punto creado
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 genérico
        }
    }
    
    // RF10/RF11: REGISTRAR REVISIÓN 
    @PostMapping("/revision") // POST /api/alpescab/registro/revision
    public ResponseEntity<?> registrarRevision(@RequestBody RevisionEntity revision) {
        try {
            RevisionEntity nuevaRevision = registroService.registrarRevision(revision);
            return new ResponseEntity<>(nuevaRevision, HttpStatus.CREATED); // 201 con la revisión creada
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);  // 400 si no cumple regla de negocio
        }
    }
}
