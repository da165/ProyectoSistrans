package uniandes.edu.co.proyecto.controllers;// Paquete donde vive el controlador de consultas (lecturas y pruebas de aislamiento)

import uniandes.edu.co.proyecto.services.*;// Importa los servicios de la capa de negocio usados por este controlador
import org.springframework.beans.factory.annotation.Autowired;   // Soporte para inyección de dependencias
import org.springframework.http.HttpStatus;// Enum con los códigos de estado HTTP
import org.springframework.http.ResponseEntity;// Envoltura estándar de respuestas HTTP
import org.springframework.web.bind.annotation.*;// Anotaciones REST (@RestController, @GetMapping, etc.)
import java.util.Date;// Tipo Date para parámetros de fecha en query
import java.util.List;// Listas para respuestas con colecciones

@RestController // Marca esta clase como un controlador REST
@RequestMapping("/api/alpescab/consulta") // Prefijo base para todas las rutas de este controlador
public class ConsultaController {

    @Autowired // Inyección del servicio de consultas
    private ConsultaService consultaService;

    // RFC1: HISTÓRICO DE SERVICIOS POR USUARIO 
    @GetMapping("/historico/usuario/{clienteId}") // GET /api/alpescab/consulta/historico/usuario/{clienteId}
    public ResponseEntity<?> getHistoricoServicios(@PathVariable Long clienteId) { // Path variable clienteId
        try {
            return new ResponseEntity<>( // Respuesta 200 con el histórico
                consultaService.consultarHistoricoUsuario(clienteId),
                HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>( // Si no se encuentra o falla la consulta, 404 con mensaje
                e.getMessage(),
                HttpStatus.NOT_FOUND
            );
        }
    }
    
    // RFC1: PRUEBAS DE AISLAMIENTO     
    // Escenario de prueba SERIALIZABLE
    @GetMapping("/historico/usuario/{clienteId}/serializable")   // GET /api/alpescab/consulta/historico/usuario/{clienteId}/serializable
    public ResponseEntity<?> getHistorico_Serializable(@PathVariable Long clienteId) {
        try {
            // Simula la ejecución que bloquea o detecta el conflicto bajo nivel SERIALIZABLE
            List<?> resultado = consultaService.consultarHistoricoUsuario_Serializable(clienteId);
            return new ResponseEntity<>(resultado, HttpStatus.OK); // 200 con el resultado de la prueba
        } catch (Exception e) {
            return new ResponseEntity<>(// 500 si ocurre un error controlado en la prueba
                "Error en consulta SERIALIZABLE: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // Escenario de prueba READ_COMMITTED
    @GetMapping("/historico/usuario/{clienteId}/read-committed") // GET /api/alpescab/consulta/historico/usuario/{clienteId}/read-committed
    public ResponseEntity<?> getHistorico_ReadCommitted(@PathVariable Long clienteId) {
        try {
            // Simula la ejecución que puede observar/no observar cambios concurrentes en READ_COMMITTED
            List<?> resultado = consultaService.consultarHistoricoUsuario_ReadCommitted(clienteId);
            return new ResponseEntity<>(resultado, HttpStatus.OK); // 200 con el resultado de la prueba
        } catch (Exception e) {
            return new ResponseEntity<>( // 500 si ocurre un error controlado en la prueba
                "Error en consulta READ_COMMITTED: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // RFC2: TOP 20 CONDUCTORES 
    @GetMapping("/top/conductores") // GET /api/alpescab/consulta/top/conductores
    public ResponseEntity<List<Object[]>> getTop20Conductores() {
        // Retorna una lista de filas (Object[]) con las columnas definidas en el repositorio/servicio
        return new ResponseEntity<>(consultaService.findTop20Conductores(), HttpStatus.OK); // 200 con el top
    }

    //  RFC3: GANANCIAS CONDUCTOR
    @GetMapping("/ganancias/conductor/{conductorId}") // GET /api/alpescab/consulta/ganancias/conductor/{conductorId}
    public ResponseEntity<List<Object[]>> getGananciasConductor(@PathVariable Long conductorId) {
        return new ResponseEntity<>( // 200 con filas de ganancias (Object[])
            consultaService.findGananciasConductor(conductorId),
            HttpStatus.OK
        );
    }

    // RFC4: UTILIZACIÓN DE SERVICIOS EN CIUDAD Y RANGO
    @GetMapping("/utilizacion/{ciudadNombre}") // GET /api/alpescab/consulta/utilizacion/{ciudadNombre}?fechaInicio=yyyy-MM-dd&fechaFin=yyyy-MM-dd
    public ResponseEntity<List<Object[]>> getUsoServicios(
            @PathVariable String ciudadNombre, // Nombre de la ciudad como parte de la ruta
            @RequestParam // Query param fechaInicio en formato yyyy-MM-dd
            @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd")
            Date fechaInicio,
            @RequestParam  // Query param fechaFin en formato yyyy-MM-dd
            @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd")
            Date fechaFin) {
        
        return new ResponseEntity<>( // 200 con las filas que describen el uso
            consultaService.findUsoServicios(ciudadNombre, fechaInicio, fechaFin),
            HttpStatus.OK
        );
    }
}
