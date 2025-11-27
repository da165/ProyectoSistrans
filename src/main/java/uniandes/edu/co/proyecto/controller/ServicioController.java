package uniandes.edu.co.proyecto.controller;

import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import uniandes.edu.co.proyecto.modelo.Servicio;
import uniandes.edu.co.proyecto.repositorio.ServicioRepository;

@RestController
@RequestMapping("/servicios")
public class ServicioController {

    @Autowired
    private ServicioRepository servicioRepository;


     @PostMapping("/solicitar-transaccion")
    @Transactional
    public ResponseEntity<Map<String, Object>> solicitarServicioTransaccional(
            @RequestParam String tipoService,
            @RequestParam Double distancia,
            @RequestParam Integer duracion,
            @RequestParam Double costo,
            @RequestParam Date horarioInicio,
            @RequestParam Date horaFin,
            @RequestParam Long idPasajero,
            @RequestParam String puntoPartida,
            @RequestParam String puntoLlegada) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 1. VERIFICAR MEDIO DE PAGO
            int tieneMedioPago = servicioRepository.tieneMedioPagoValido(idPasajero);
            if (tieneMedioPago == 0) {
                response.put("success", false);
                response.put("message", "El usuario no tiene un medio de pago registrado disponible");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // 2. BUSCAR CONDUCTOR DISPONIBLE
            Date fechaServicio = new java.sql.Date(horarioInicio.getTime());
            Long idConductor = servicioRepository.encontrarConductorDisponible(fechaServicio, horarioInicio);
            
            if (idConductor == null) {
                response.put("success", false);
                response.put("message", "No hay conductores disponibles para el servicio solicitado");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // 3. ACTUALIZAR ESTADO DEL CONDUCTOR (Marcar como ocupado)
           
            servicioRepository.marcarConductorOcupado(idConductor);

            // 4. REGISTRAR EL SERVICIO COMPLETO
            servicioRepository.solicitarServicio(
                tipoService, distancia, duracion, costo, 
                horarioInicio, horaFin, idConductor, idPasajero
            );

            
            response.put("success", true);
            response.put("message", "Servicio solicitado exitosamente");
            response.put("idConductorAsignado", idConductor);
            response.put("estado", "ASIGNADO");
            response.put("costo", costo);
            response.put("puntoPartida", puntoPartida);
            response.put("puntoLlegada", puntoLlegada);

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
           
            response.put("success", false);
            response.put("message", "Error en la transacción: " + e.getMessage());
            response.put("transaccion", "ABORTADA");
            
           
            throw new RuntimeException("Transacción abortada: " + e.getMessage(), e);
        }
    }

    // RF8 - SOLICITAR UN SERVICIO
    @PostMapping("/new/save")
    @Transactional
    public ResponseEntity<String> servicioGuardar(@RequestBody Servicio servicio) {
        try {
            // 1. Verificar medio de pago
            Long idPasajero = servicio.getUsuario().getId();
            int medioPagoValido = servicioRepository.tieneMedioPagoValido(idPasajero);
            
            if (medioPagoValido == 0) {
                return new ResponseEntity<>("El usuario no tiene medio de pago válido", HttpStatus.BAD_REQUEST);
            }

            // 2. Buscar conductor disponible
            Date fecha = new java.sql.Date(servicio.getHorarioInicio().getTime());
            Long idConductorDisponible = servicioRepository.encontrarConductorDisponible(fecha, servicio.getHorarioInicio());
            
            if (idConductorDisponible == null) {
                return new ResponseEntity<>("No hay conductores disponibles", HttpStatus.BAD_REQUEST);
            }

            // 3. Actualizar conductor
            servicioRepository.marcarConductorOcupado(idConductorDisponible);

            // 4. Registrar servicio
            servicioRepository.solicitarServicio(
                servicio.getTipoService(),
                servicio.getDistancia(),
                servicio.getDuracion(),
                servicio.getCosto(),
                servicio.getHorarioInicio(),
                servicio.getHoraFin(),
                idConductorDisponible,
                idPasajero
            );

            return new ResponseEntity<>("Servicio solicitado exitosamente", HttpStatus.CREATED);

        } catch (Exception e) {
            throw new RuntimeException("Error transaccional: " + e.getMessage(), e);
        }
    }


    

    // RF9 - REGISTRAR EL FINAL DE UN VIAJE
    @PostMapping("/{id}/completar")
    @Transactional
    public ResponseEntity<Map<String, Object>> servicioCompletar(@PathVariable("id") long id, @RequestBody Servicio servicio) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 1. Obtener el conductor asignado a este servicio
            Long idConductor = servicioRepository.obtenerConductorDelServicio(id);
            
            if (idConductor == null) {
                response.put("success", false);
                response.put("message", "No se encontró el conductor asignado a este servicio");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // 2. Actualizar el registro del viaje con los datos finales
            servicioRepository.registrarViajeCompletado(
                id,
                servicio.getDistancia(),
                servicio.getDuracion(),
                servicio.getCosto(),
                servicio.getHoraFin()
            );

            // 3. Marcar al conductor como disponible nuevamente
            servicioRepository.liberarConductor(idConductor);

            response.put("success", true);
            response.put("message", "Viaje completado exitosamente - Conductor marcado como disponible");
            response.put("idServicio", id);
            response.put("idConductorLiberado", idConductor);
            response.put("distanciaRegistrada", servicio.getDistancia());
            response.put("duracionRegistrada", servicio.getDuracion());
            response.put("costoFinal", servicio.getCosto());
            
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al completar el viaje: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<Collection<Servicio>> servicios() {
        try {
            Collection<Servicio> servicios = servicioRepository.darServicios();
            return ResponseEntity.ok(servicios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/pasajero/{idPasajero}")
    public ResponseEntity<Collection<Servicio>> serviciosPorPasajero(@PathVariable("idPasajero") long idPasajero) {
        try {
            Collection<Servicio> servicios = servicioRepository.darServiciosPorPasajero(idPasajero);
            return ResponseEntity.ok(servicios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/conductor/{idConductor}")
    public ResponseEntity<Collection<Servicio>> serviciosPorConductor(@PathVariable("idConductor") long idConductor) {
        try {
            Collection<Servicio> servicios = servicioRepository.darServiciosPorConductor(idConductor);
            return ResponseEntity.ok(servicios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /**
 * RFC4 - UTILIZACIÓN DE SERVICIOS POR CIUDAD DADO UN RANGO DE FECHAS
 */
    @GetMapping("/estadisticas/ciudad")
    public ResponseEntity<Map<String, Object>> utilizacionServiciosPorCiudad(
            @RequestParam String ciudad,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Collection<Object[]> resultados = servicioRepository.utilizacionServiciosPorCiudad(ciudad, fechaInicio, fechaFin);
            
            response.put("success", true);
            response.put("ciudad", ciudad);
            response.put("fechaInicio", fechaInicio);
            response.put("fechaFin", fechaFin);
            response.put("estadisticas", resultados);
            response.put("totalTiposServicio", resultados.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener estadísticas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
}
}