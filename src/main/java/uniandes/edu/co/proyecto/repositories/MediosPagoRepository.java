package uniandes.edu.co.proyecto.repositories;
import uniandes.edu.co.proyecto.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediosPagoRepository extends JpaRepository<MediosPagoEntity, Long> {

    // Método esencial para RF8: Verificar si el usuario tiene un medio de pago registrado
    List<MediosPagoEntity> findByUsuarioDeServicio(UsuarioServicioEntity usuarioDeServicio);
    
    // Método para buscar un medio de pago específico por número (para gestión/actualización)
    MediosPagoEntity findByNumeroTarjeta(String numeroTarjeta);
}
