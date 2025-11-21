package uniandes.edu.co.proyecto.controllers;

import uniandes.edu.co.proyecto.services.ConsultaService;
import uniandes.edu.co.proyecto.entities.ServicioEntity;
import uniandes.edu.co.proyecto.controllers.DTO.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    // ---------------------- RFC1: HISTÓRICO DE SERVICIOS POR USUARIO ----------------------
    @GetMapping("/usuario/{clienteId}")
    public ResponseEntity<?> getHistoricoServicios(@PathVariable Long clienteId) {
        try {
            List<ServicioEntity> historico = consultaService.consultarHistoricoUsuario(clienteId);
            return ResponseEntity.ok(historico);
        } catch (Exception e) {
            // Cliente no encontrado o no es UsuarioServicioEntity
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ---------------- RFC1: PRUEBAS DE AISLAMIENTO ----------------

    // Escenario SERIALIZABLE
    @GetMapping("/historico/usuario/{clienteId}/serializable")
    public ResponseEntity<?> getHistorico_Serializable(@PathVariable Long clienteId) {
        try {
            List<ServicioEntity> resultado = consultaService.consultarHistoricoUsuario_Serializable(clienteId);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // Escenario READ_COMMITTED
    @GetMapping("/historico/usuario/{clienteId}/read-committed")
    public ResponseEntity<?> getHistorico_ReadCommitted(@PathVariable Long clienteId) {
        try {
            List<ServicioEntity> resultado = consultaService.consultarHistoricoUsuario_ReadCommitted(clienteId);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // ---------------------- RFC2: TOP 20 CONDUCTORES ----------------------
    @GetMapping("/top/conductores")
    public ResponseEntity<List<TopConductorDTO>> getTop20Conductores() {
        return ResponseEntity.ok(consultaService.findTop20Conductores());
    }

    // ---------------------- RFC3: GANANCIAS CONDUCTOR ----------------------
    @GetMapping("/ganancias/conductor/{conductorId}")
    public ResponseEntity<List<GananciaConductorDTO>> getGananciasConductor(@PathVariable Long conductorId) {
        return ResponseEntity.ok(consultaService.findGananciasConductor(conductorId));
    }

    // ---------------------- RFC4: UTILIZACIÓN DE SERVICIOS ----------------------
    @GetMapping("/utilizacion/{ciudadNombre}")
    public ResponseEntity<List<UtilizacionServiciosDTO>> getUsoServicios(
            @PathVariable String ciudadNombre,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin) {

        return ResponseEntity.ok(
                consultaService.findUsoServicios(ciudadNombre, fechaInicio, fechaFin)
        );
    }
}
