package uniandes.edu.co.proyecto.controllers;
import uniandes.edu.co.proyecto.services.*;
import uniandes.edu.co.proyecto.entities.ServicioEntity;
import uniandes.edu.co.proyecto.controllers.DTO.*; // Importar los DTOs
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/alpescab/consulta")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    // ---------------------- RFC1: HISTÓRICO DE SERVICIOS POR USUARIO ----------------------
    @GetMapping("/historico/usuario/{clienteId}")
    // Se cambia el tipo de retorno específico a ResponseEntity<List<ServicioEntity>>
    public ResponseEntity<List<ServicioEntity>> getHistoricoServicios(@PathVariable Long clienteId) {
        try {
            return new ResponseEntity<>(consultaService.consultarHistoricoUsuario(clienteId), HttpStatus.OK);
        } catch (Exception e) {
            // Manejo de error con un objeto de tipo String
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND); 
        }
    }
    
    // ---------------------- RFC1: PRUEBAS DE AISLAMIENTO (Punto 3 de la entrega) ----------------------
    
    // Escenario de prueba SERIALIZABLE
    @GetMapping("/historico/usuario/{clienteId}/serializable")
    public ResponseEntity<List<ServicioEntity>> getHistorico_Serializable(@PathVariable Long clienteId) {
        try {
            List<ServicioEntity> resultado = consultaService.consultarHistoricoUsuario_Serializable(clienteId);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    // Escenario de prueba READ_COMMITTED
    @GetMapping("/historico/usuario/{clienteId}/read-committed")
    public ResponseEntity<List<ServicioEntity>> getHistorico_ReadCommitted(@PathVariable Long clienteId) {
        try {
            List<ServicioEntity> resultado = consultaService.consultarHistoricoUsuario_ReadCommitted(clienteId);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ---------------------- RFC2: TOP 20 CONDUCTORES ----------------------
    // ¡Ajustado para devolver el DTO específico!
    @GetMapping("/top/conductores")
    public ResponseEntity<List<TopConductorDTO>> getTop20Conductores() {
        return new ResponseEntity<>(consultaService.findTop20Conductores(), HttpStatus.OK);
    }

    // ---------------------- RFC3: GANANCIAS CONDUCTOR ----------------------
    // ¡Ajustado para devolver el DTO específico!
    @GetMapping("/ganancias/conductor/{conductorId}")
    public ResponseEntity<List<GananciaConductorDTO>> getGananciasConductor(@PathVariable Long conductorId) {
        return new ResponseEntity<>(consultaService.findGananciasConductor(conductorId), HttpStatus.OK);
    }

    // ---------------------- RFC4: UTILIZACIÓN DE SERVICIOS EN CIUDAD Y RANGO ----------------------
    // ¡Ajustado para devolver el DTO específico!
    @GetMapping("/utilizacion/{ciudadNombre}")
    public ResponseEntity<List<UtilizacionServiciosDTO>> getUsoServicios(
            @PathVariable String ciudadNombre,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd") Date fechaInicio,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd") Date fechaFin) {
        
        return new ResponseEntity<>(consultaService.findUsoServicios(ciudadNombre, fechaInicio, fechaFin), HttpStatus.OK);
    }
}
