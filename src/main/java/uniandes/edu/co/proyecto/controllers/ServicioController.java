package uniandes.edu.co.proyecto.controllers;// Paquete del controlador para operaciones transaccionales de servicio
import uniandes.edu.co.proyecto.entities.*;// Entidades (ServicioEntity) devueltas en las respuestas
import uniandes.edu.co.proyecto.services.*;// Servicio de dominio transaccional (coordinador)
import uniandes.edu.co.proyecto.controllers.DTO.*;// DTOs de entrada (SolicitudServicioDTO)
import org.springframework.beans.factory.annotation.Autowired;  // Inyección
import org.springframework.http.HttpStatus;// Códigos HTTP
import org.springframework.http.ResponseEntity;// Respuesta HTTP tipada
import org.springframework.web.bind.annotation.*;// Anotaciones REST
import java.util.Map;// Mapa para leer pares clave/valor del body (finalizar)

@RestController// Controlador REST
@RequestMapping("/api/alpescab/servicio")                       // Prefijo base de rutas del recurso Servicio
public class ServicioController {

    @Autowired// Servicio que encapsula la transacción (solicitar/finalizar)
    private ServicioTransaccionalService servicioTransaccionalService;

    //  RF8: SOLICITAR UN SERVICIO (TRANSACCIONAL) 
    @PostMapping("/solicitar")// POST /api/alpescab/servicio/solicitar
    // Usa el DTO como el cuerpo de la petición (@RequestBody)
    public ResponseEntity<?> solicitarServicio(@RequestBody SolicitudServicioDTO solicitud) {
        try {
            // El servicio maneja la transaccionalidad de punta a punta (asignación, cobro, persistencia)
            ServicioEntity nuevoServicio = servicioTransaccionalService.solicitarServicio(solicitud);
            
            // Retorna 201 Created y el objeto Servicio recién creado
            return new ResponseEntity<>(nuevoServicio, HttpStatus.CREATED);
        } catch (Exception e) {
            // Si falla la transacción (no hay conductor, pago inválido, etc.), responde 409 Conflict
            return new ResponseEntity<>(
                "RF8 Fallido (Transacción Abortada): " + e.getMessage(),
                HttpStatus.CONFLICT
            ); 
        }
    }

    //  RF9: REGISTRAR FINAL DE VIAJE (TRANSACCIONAL) 
    @PutMapping("/finalizar/{servicioId}")// PUT /api/alpescab/servicio/finalizar/{servicioId}
    public ResponseEntity<?> finalizarServicio(@PathVariable Long servicioId, @RequestBody Map<String, Double> datosFin) {
        try {
            // Extrae el dato 'longitudTrayecto' del JSON de la petición
            Double longitud = datosFin.get("longitudTrayecto");  // Body: { "longitudTrayecto": number }
            if (longitud == null) {
                 return new ResponseEntity<>(// 400 si el campo requerido no viene en el body
                    "RF9 Fallido: Falta el campo 'longitudTrayecto' en el cuerpo de la petición.",
                    HttpStatus.BAD_REQUEST
                 );
            }
            
            ServicioEntity servicioFinalizado = servicioTransaccionalService.finalizarServicio(servicioId, longitud);
            return new ResponseEntity<>(servicioFinalizado, HttpStatus.OK); // 200 con el servicio actualizado
        } catch (Exception e) {
            // Puede fallar si el servicio no existe o si hay un error en la actualización
            return new ResponseEntity<>(
                "RF9 Fallido: " + e.getMessage(),
                HttpStatus.BAD_REQUEST
            );
        }
    }
}
