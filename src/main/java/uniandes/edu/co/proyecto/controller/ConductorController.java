package uniandes.edu.co.proyecto.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uniandes.edu.co.proyecto.modelo.Conductor;
import uniandes.edu.co.proyecto.repositorio.ConductorRepository;

@RestController
@RequestMapping("/conductores")
public class ConductorController {

    @Autowired
    private ConductorRepository conductorRepository;

    // RF3 - REGISTRAR UN USUARIO CONDUCTOR
    @PostMapping("/new/save")
    public ResponseEntity<String> conductorGuardar(@RequestParam long idUsuario) {
        try {
            // ver que el usuario exista
            if (conductorRepository.darConductor(idUsuario) == null) {
                return new ResponseEntity<>("El usuario no existe", HttpStatus.BAD_REQUEST);
            }
            conductorRepository.registrarConductor(idUsuario);
            return new ResponseEntity<>("Conductor registrado exitosamente", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al registrar el conductor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<Collection<Conductor>> conductores() {
        try {
            Collection<Conductor> conductores = conductorRepository.darConductores();
            return ResponseEntity.ok(conductores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // RFC2 - MOSTRAR LOS 20 USUARIOS CONDUCTORES QUE MÁS SERVICIOS HAN PRESTADO EN LA APLICACIÓN
    @GetMapping("/top-conductores")
    public ResponseEntity<Collection<Object[]>> topConductores() {
        try {
            Collection<Object[]> topConductores = conductorRepository.darTopConductores();
            return ResponseEntity.ok(topConductores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // RFC3 - MOSTRAR EL TOTAL DE DINERO OBTENIDO POR USUARIOS CONDUCTORES PARA CADA UNO DE SUS VEHÍCULOS, DISCRIMINADO POR SERVICIOS
    @GetMapping("/ganancias-conductores")
    public ResponseEntity<Collection<Object[]>> gananciasConductores() {
        try {
            Collection<Object[]> gananciasConductores = conductorRepository.darGananciasConductores();
            return ResponseEntity.ok(gananciasConductores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}