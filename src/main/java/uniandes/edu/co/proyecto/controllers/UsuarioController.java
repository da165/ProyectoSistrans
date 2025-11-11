package uniandes.edu.co.proyecto.controllers;                      // Paquete donde vive el controlador de usuarios

import uniandes.edu.co.proyecto.entities.*;                        // Entidades de dominio: cliente y conductor
import uniandes.edu.co.proyecto.services.*;                        // Servicio de usuarios (capa de negocio)
import org.springframework.beans.factory.annotation.Autowired;     // Inyección de dependencias
import org.springframework.http.HttpStatus;                        // Códigos de estado HTTP
import org.springframework.http.ResponseEntity;                    // Envoltura de respuestas HTTP
import org.springframework.web.bind.annotation.*;                  // Anotaciones REST (@RestController, @GetMapping, etc.)
import java.util.List;                                             // Listas para respuestas con colecciones

@RestController                                                     // Marca la clase como controlador REST
@RequestMapping("/api/alpescab/usuario")                           // Ruta base para todas las operaciones de usuario
public class UsuarioController {
    
    @Autowired                                                     // Inyección del servicio de usuarios
    private UsuarioService usuarioService;

    // ----------------------------- CLIENTES -----------------------------

    @GetMapping("/cliente")                                        // GET /api/alpescab/usuario/cliente
    public ResponseEntity<?> listarClientes() {                    // Lista todos los clientes
        try {
            List<UsuarioServicioEntity> data = usuarioService.listarClientes();   // Llama al servicio
            return new ResponseEntity<>(data, HttpStatus.OK);      // 200 OK con la lista
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 con el mensaje
        }
    }

    @GetMapping("/cliente/{id}")                                   // GET /api/alpescab/usuario/cliente/{id}
    public ResponseEntity<?> getCliente(@PathVariable Long id) {   // Obtiene un cliente por id
        try {
            UsuarioServicioEntity cliente = usuarioService.getCliente(id);        // Llama al servicio
            if (cliente == null) {                                  // Si no existe, 404
                return new ResponseEntity<>("Cliente no encontrado.", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(cliente, HttpStatus.OK);    // 200 con el objeto
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 controlado
        }
    }

    @PutMapping("/cliente/{id}")                                   // PUT /api/alpescab/usuario/cliente/{id}
    public ResponseEntity<?> actualizarCliente(                    // Actualiza datos básicos del cliente
            @PathVariable Long id,
            @RequestBody UsuarioServicioEntity datos) {            // Body: JSON con campos a actualizar
        try {
            UsuarioServicioEntity actualizado = usuarioService.actualizarCliente(id, datos); // Lógica en el servicio
            return new ResponseEntity<>(actualizado, HttpStatus.OK); // 200 con el recurso actualizado
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);  // 400 si incumple regla/validación
        }
    }

    // ----------------------------- CONDUCTORES -----------------------------

    @GetMapping("/conductor")                                      // GET /api/alpescab/usuario/conductor
    public ResponseEntity<?> listarConductores() {                 // Lista todos los conductores
        try {
            List<UsuarioConductorEntity> data = usuarioService.listarConductores(); // Llama al servicio
            return new ResponseEntity<>(data, HttpStatus.OK);      // 200 OK con la lista
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }

    @GetMapping("/conductor/{id}")                                 // GET /api/alpescab/usuario/conductor/{id}
    public ResponseEntity<?> getConductor(@PathVariable Long id) { // Obtiene un conductor por id
        try {
            UsuarioConductorEntity conductor = usuarioService.getConductor(id);   // Llama al servicio
            if (conductor == null) {                                 // Si no existe, 404
                return new ResponseEntity<>("Conductor no encontrado.", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(conductor, HttpStatus.OK);   // 200 con el objeto
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 controlado
        }
    }

    @PutMapping("/conductor/{id}")                                 // PUT /api/alpescab/usuario/conductor/{id}
    public ResponseEntity<?> actualizarConductor(                  // Actualiza datos básicos del conductor
            @PathVariable Long id,
            @RequestBody UsuarioConductorEntity datos) {           // Body: JSON con campos a actualizar
        try {
            UsuarioConductorEntity actualizado = usuarioService.actualizarConductor(id, datos); // Lógica en el servicio
            return new ResponseEntity<>(actualizado, HttpStatus.OK); // 200 con el recurso actualizado
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);  // 400 si incumple regla/validación
        }
    }
}
