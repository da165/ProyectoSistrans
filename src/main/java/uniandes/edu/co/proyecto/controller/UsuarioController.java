package uniandes.edu.co.proyecto.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.web.bind.annotation.*;

import uniandes.edu.co.proyecto.modelo.Usuario;
import uniandes.edu.co.proyecto.repositorio.UsuarioRepository;



import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // RF2 - REGISTRAR UN USUARIO DE SERVICIOS
    @PostMapping("/servicio/registrar")
    @Transactional
    public ResponseEntity<Map<String, Object>> registrarUsuarioServicio(
            @RequestParam String nombre,
            @RequestParam String correo,
            @RequestParam String celular,
            @RequestParam String cedula) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Validar que no exista usuario con misma cédula
            if (usuarioRepository.existeUsuarioConCedula(cedula)) {
                response.put("success", false);
                response.put("message", "Ya existe un usuario registrado con esta cédula");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Registrar el usuario de servicios
            usuarioRepository.registrarUsuarioServicio(nombre, correo, celular, cedula);
            
            // Obtener el ID del usuario recién creado
            Long userId = usuarioRepository.getLastUserId();

            response.put("success", true);
            response.put("message", "Usuario de servicios registrado exitosamente SIN datos de pago");
            response.put("idUsuario", userId);
            response.put("tipo", "USUARIO_SERVICIO");
            response.put("datosPago", "NO_INCLUIDOS"); 
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al registrar usuario: " + e.getMessage());
            // El @Transactional hará rollback automáticamente
            throw new RuntimeException("Error en transacción RF2: " + e.getMessage(), e);
        }
    }

 
     
    @PostMapping("/new/save")
    @Transactional
    public ResponseEntity<String> usuarioGuardar(@RequestBody Usuario usuario) {
        try {
            // Validar cédula única
            if (usuarioRepository.existeUsuarioConCedula(usuario.getCedula())) {
                return new ResponseEntity<>("Ya existe un usuario con esta cédula", HttpStatus.BAD_REQUEST);
            }

            usuarioRepository.save(usuario);
            return new ResponseEntity<>("Usuario creado exitosamente", HttpStatus.CREATED);
            
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear el usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // RFC1 - VERSIÓN READ COMMITTED
    @GetMapping("/servicios/{cedula}/read-committed")
    @Transactional(isolation = Isolation.READ_COMMITTED, timeout = 30)
    public ResponseEntity<Map<String, Object>> serviciosPorUsuarioReadCommitted(@PathVariable("cedula") String cedula) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // PRIMERA CONSULTA - antes del temporizador
            Collection<Object[]> serviciosAntes = usuarioRepository.darServiciosUsuario(cedula);
            response.put("servicios_antes", serviciosAntes);
            response.put("cantidad_servicios_antes", serviciosAntes.size());
            
            // TEMPORIZADOR DE 30 SEGUNDOS (para pruebas de concurrencia)
            response.put("mensaje", "Iniciando temporizador de 30 segundos - READ COMMITTED");
            Thread.sleep(30000); // 30 segundos
            
            // SEGUNDA CONSULTA - después del temporizador  
            Collection<Object[]> serviciosDespues = usuarioRepository.darServiciosUsuario(cedula);
            response.put("servicios_despues", serviciosDespues);
            response.put("cantidad_servicios_despues", serviciosDespues.size());
            response.put("diferencia", serviciosDespues.size() - serviciosAntes.size());
            
            response.put("success", true);
            response.put("nivel_aislamiento", "READ_COMMITTED");
            response.put("tiempo_espera", "30 segundos");
            
            return ResponseEntity.ok(response);
            
        } catch (InterruptedException e) {
            response.put("success", false);
            response.put("message", "Transacción interrumpida: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error en transacción READ COMMITTED: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // RFC1 - VERSIÓN SERIALIZABLE
    @GetMapping("/servicios/{cedula}/serializable")
    @Transactional(isolation = Isolation.SERIALIZABLE, timeout = 30)
    public ResponseEntity<Map<String, Object>> serviciosPorUsuarioSerializable(@PathVariable("cedula") String cedula) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // PRIMERA CONSULTA - antes del temporizador
            Collection<Object[]> serviciosAntes = usuarioRepository.darServiciosUsuario(cedula);
            response.put("servicios_antes", serviciosAntes);
            response.put("cantidad_servicios_antes", serviciosAntes.size());
            
            // TEMPORIZADOR DE 30 SEGUNDOS (para pruebas de concurrencia)
            response.put("mensaje", "Iniciando temporizador de 30 segundos - SERIALIZABLE");
            Thread.sleep(30000); // 30 segundos
            
            // SEGUNDA CONSULTA - después del temporizador
            Collection<Object[]> serviciosDespues = usuarioRepository.darServiciosUsuario(cedula);
            response.put("servicios_despues", serviciosDespues);
            response.put("cantidad_servicios_despues", serviciosDespues.size());
            response.put("diferencia", serviciosDespues.size() - serviciosAntes.size());
            
            response.put("success", true);
            response.put("nivel_aislamiento", "SERIALIZABLE");
            response.put("tiempo_espera", "30 segundos");
            
            return ResponseEntity.ok(response);
            
        } catch (InterruptedException e) {
            response.put("success", false);
            response.put("message", "Transacción interrumpida: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error en transacción SERIALIZABLE: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    }
