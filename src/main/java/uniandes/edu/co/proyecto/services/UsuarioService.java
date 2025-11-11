package uniandes.edu.co.proyecto.services;// Paquete del servicio de usuarios

import uniandes.edu.co.proyecto.entities.*;// Entidades base y subtipos (UsuarioServicioEntity, UsuarioConductorEntity)
import uniandes.edu.co.proyecto.repositories.*;// Repositorio JPA de usuarios
import org.springframework.beans.factory.annotation.Autowired; // Inyección de dependencias
import org.springframework.stereotype.Service;// Estereotipo de servicio de Spring
import org.springframework.transaction.annotation.Transactional; // Soporte transaccional para actualizaciones

import java.util.List;// Colecciones
import java.util.Optional;// Manejo de presente/ausente
import java.util.stream.Collectors;// Streams para filtrar/castear

@Service// Marca esta clase como un servicio de Spring
public class UsuarioService {

    @Autowired// Inyecta el repositorio de usuarios
    private UsuarioRepository usuarioRepository;

    //  LISTAR 

    /**
     * Lista todos los usuarios de tipo Cliente (UsuarioServicioEntity).
     * Implementación: trae todos los usuarios y filtra por subtipo usando instanceof.
     */
    public List<UsuarioServicioEntity> listarClientes() {
        return usuarioRepository.findAll()// Carga todos los usuarios
                .stream()// Abre stream para operar en memoria
                .filter(u -> u instanceof UsuarioServicioEntity)// Filtra solo los clientes
                .map(u -> (UsuarioServicioEntity) u)// Castea al subtipo concreto
                .collect(Collectors.toList());// Retorna como lista
    }

    /**
     * Lista todos los usuarios de tipo Conductor (UsuarioConductorEntity).
     * Misma estrategia que listarClientes(), pero para conductores.
     */
    public List<UsuarioConductorEntity> listarConductores() {
        return usuarioRepository.findAll()
                .stream()
                .filter(u -> u instanceof UsuarioConductorEntity)
                .map(u -> (UsuarioConductorEntity) u)
                .collect(Collectors.toList());
    }

    // OBTENER POR ID 

    /**
     * Obtiene un Cliente por id.
     * Si el id no existe o el usuario no es de tipo Cliente, retorna null (para que el controller responda 404).
     */
    public UsuarioServicioEntity getCliente(Long id) {
        Optional<UsuarioEntity> opt = usuarioRepository.findById(id);// Busca por id
        if (opt.isPresent() && opt.get() instanceof UsuarioServicioEntity)// Verifica subtipo correcto
            return (UsuarioServicioEntity) opt.get();// Castea y retorna
        return null;// No encontrado / tipo incorrecto
    }

    /**
     * Obtiene un Conductor por id.
     * Si el id no existe o el usuario no es de tipo Conductor, retorna null (para que el controller responda 404).
     */
    public UsuarioConductorEntity getConductor(Long id) {
        Optional<UsuarioEntity> opt = usuarioRepository.findById(id);
        if (opt.isPresent() && opt.get() instanceof UsuarioConductorEntity)
            return (UsuarioConductorEntity) opt.get();
        return null;
    }

    //ACTUALIZAR 

    /**
     * Actualiza un Cliente (UsuarioServicioEntity).
     * Reglas:
     *  - Debe existir el id.
     *  - El id debe corresponder a un usuario de tipo Cliente.
     * Estrategia: se asegura el id y se guarda con JPA (merge). Si no existe o el tipo no coincide, lanza Exception.
     */
    @Transactional
    public UsuarioServicioEntity actualizarCliente(Long id, UsuarioServicioEntity datos) throws Exception {
        UsuarioEntity actual = usuarioRepository.findById(id)// Verifica existencia
                .orElseThrow(() -> new Exception("Cliente con ID " + id + " no encontrado."));
        if (!(actual instanceof UsuarioServicioEntity))// Verifica subtipo correcto
            throw new Exception("El ID no corresponde a un Usuario de Servicio.");
        datos.setId(id);// Garantiza que se actualiza el registro del path
        return usuarioRepository.save(datos);// Persiste y retorna el actualizado
    }

    /**
     * Actualiza un Conductor (UsuarioConductorEntity).
     * Reglas:
     *  - Debe existir el id.
     *  - El id debe corresponder a un usuario de tipo Conductor.
     * Estrategia: igual a actualizarCliente(), asegurando el id y guardando.
     */
    @Transactional
    public UsuarioConductorEntity actualizarConductor(Long id, UsuarioConductorEntity datos) throws Exception {
        UsuarioEntity actual = usuarioRepository.findById(id)
                .orElseThrow(() -> new Exception("Conductor con ID " + id + " no encontrado."));
        if (!(actual instanceof UsuarioConductorEntity))
            throw new Exception("El ID no corresponde a un Usuario Conductor.");
        datos.setId(id);
        return usuarioRepository.save(datos);
    }
}
