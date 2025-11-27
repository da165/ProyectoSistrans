package uniandes.edu.co.proyecto.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uniandes.edu.co.proyecto.modelo.Vehiculo;
import uniandes.edu.co.proyecto.repositorio.VehiculoRepository;
import uniandes.edu.co.proyecto.repositorio.ConductorRepository;

@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private ConductorRepository conductorRepository;

    // RF4 - REGISTRAR UN VEHÍCULO PARA UN USUARIO CONDUCTOR
    @PostMapping("/new/save")
    public ResponseEntity<String> vehiculoGuardar(@RequestBody Vehiculo vehiculo) {
        try {
            // Verificar que el conductor existe
            if (conductorRepository.darConductor(vehiculo.getConductor().getId()) == null) {
                return new ResponseEntity<>("El conductor no existe", HttpStatus.BAD_REQUEST);
            }
            
            vehiculoRepository.registrarVehiculo(
                vehiculo.getTipo(),
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                vehiculo.getColor(),
                vehiculo.getPlaca(),
                vehiculo.getCiudadExpo(),
                vehiculo.getCapacidad(),
                vehiculo.getConductor().getId()
            );
            return new ResponseEntity<>("Vehículo registrado exitosamente", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al registrar el vehículo: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<Collection<Vehiculo>> vehiculos() {
        try {
            Collection<Vehiculo> vehiculos = vehiculoRepository.darVehiculos();
            return ResponseEntity.ok(vehiculos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/conductor/{idConductor}")
    public ResponseEntity<Collection<Vehiculo>> vehiculosPorConductor(@PathVariable("idConductor") long idConductor) {
        try {
            Collection<Vehiculo> vehiculos = vehiculoRepository.darVehiculosPorConductor(idConductor);
            return ResponseEntity.ok(vehiculos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}