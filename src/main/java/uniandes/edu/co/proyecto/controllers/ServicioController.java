package uniandes.edu.co.proyecto.controllers;
import uniandes.edu.co.proyecto.entities.*;
import uniandes.edu.co.proyecto.services.*;
import uniandes.edu.co.proyecto.controllers.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/alpescab/servicio")
public class ServicioController {

    @Autowired
    private ServicioTransaccionalService servicioTransaccionalService;

    // ---------------------- RF8: SOLICITAR UN SERVICIO (TRANSACCIONAL) ----------------------
    @PostMapping("/solicitar")
    // Usa el DTO como el cuerpo de la petición (@RequestBody)
    public ResponseEntity<?> solicitarServicio(@RequestBody SolicitudServicioDTO solicitud) {
        try {
            // El servicio maneja toda la complejidad, asegurando que la transacción sea atómica.
            ServicioEntity nuevoServicio = servicioTransaccionalService.solicitarServicio(solicitud);
            
            // Retorna 201 Created y el objeto Servicio recién creado
            return new ResponseEntity<>(nuevoServicio, HttpStatus.CREATED);
        } catch (Exception e) {
            // Retorna un error 409 Conflict si la transacción falló (e.g., no hay conductor o pago inválido).
            return new ResponseEntity<>("RF8 Fallido (Transacción Abortada): " + e.getMessage(), HttpStatus.CONFLICT); 
        }
    }

    // ---------------------- RF9: REGISTRAR FINAL DE VIAJE (TRANSACCIONAL) ----------------------
    @PutMapping("/finalizar/{servicioId}")
    public ResponseEntity<?> finalizarServicio(@PathVariable Long servicioId, @RequestBody Map<String, Double> datosFin) {
        try {
            // Extrae el dato 'longitudTrayecto' del JSON de la petición
            Double longitud = datosFin.get("longitudTrayecto");
            if (longitud == null) {
                 return new ResponseEntity<>("RF9 Fallido: Falta el campo 'longitudTrayecto' en el cuerpo de la petición.", HttpStatus.BAD_REQUEST);
            }
            
            ServicioEntity servicioFinalizado = servicioTransaccionalService.finalizarServicio(servicioId, longitud);
            return new ResponseEntity<>(servicioFinalizado, HttpStatus.OK);
        } catch (Exception e) {
            // Puede fallar si el servicio no existe o si hay un error al actualizar el conductor.
            return new ResponseEntity<>("RF9 Fallido: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}