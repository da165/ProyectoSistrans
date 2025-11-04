package uniandes.edu.co.proyecto.controllers;
import uniandes.edu.co.proyecto.entities.*;
import uniandes.edu.co.proyecto.services.*;
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
    // Se recibe un mapa o un DTO complejo con toda la información necesaria para el servicio.
    public ResponseEntity<?> solicitarServicio(@RequestBody Map<String, Object> solicitud) {
        try {
            // NOTA: En una implementación real, se debe deserializar el Map a un DTO
            // que contenga cliente, tipoServicio, puntos, y costo. 
            // Aquí se simula la llamada al servicio transaccional.
            
            // Ejemplo de llamada simulada:
            // Servicio nuevoServicio = servicioTransaccionalService.solicitarServicio(cliente, tipo, partida, llegadas, costo);

            return new ResponseEntity<>("Servicio solicitado con éxito. El conductor ha sido asignado. (Se requiere DTO para completar)", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            // RF8: Si alguna de las operaciones falla, la transacción debe abortar.
            return new ResponseEntity<>("RF8 Fallido (Transacción Abortada): " + e.getMessage(), HttpStatus.CONFLICT); 
        }
    }

    // ---------------------- RF9: REGISTRAR FINAL DE VIAJE (TRANSACCIONAL) ----------------------
    @PutMapping("/finalizar/{servicioId}")
    public ResponseEntity<?> finalizarServicio(@PathVariable Long servicioId, @RequestBody Map<String, Double> datosFin) {
        try {
            // Se asume que el cuerpo de la petición contiene la longitud del trayecto (longitudTrayecto)
            Double longitud = datosFin.get("longitudTrayecto");
            if (longitud == null) {
                 return new ResponseEntity<>("Falta el campo 'longitudTrayecto'.", HttpStatus.BAD_REQUEST);
            }
            
            ServicioEntity servicioFinalizado = servicioTransaccionalService.finalizarServicio(servicioId, longitud);
            return new ResponseEntity<>(servicioFinalizado, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("RF9 Fallido: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}