package cl.duoc.multas_microservice.service;

import cl.duoc.multas_microservice.client.PrestamoClient;
import cl.duoc.multas_microservice.client.UsuarioClient;
import cl.duoc.multas_microservice.dto.MultaRequest;
import cl.duoc.multas_microservice.dto.MultaResponse;
import cl.duoc.multas_microservice.mapper.MultaMapper;
import cl.duoc.multas_microservice.model.Multa;
import cl.duoc.multas_microservice.repository.MultaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
public class MultaService {
    @Autowired
    private MultaRepository multaRepository;

    @Autowired
    private MultaMapper multaMapper;

    @Autowired
    private UsuarioClient usuarioClient;

    @Autowired
    private PrestamoClient prestamoClient;

    public MultaResponse crearMulta(MultaRequest multaRequest, String token) {
        log.info("Creando nueva multa para el Usuario ID: {} asociado al Préstamo ID: {}",
                multaRequest.getUsuarioId(), multaRequest.getPrestamoId());
        //Validar existencia del usuario en el microservicio de usuarios
        UsuarioResponse usuarioResponse = usuarioClient.obtenerUsuarioPorId(multaRequest.getUsuarioId(), token);

        //Validar existencia del préstamo en el microservicio de préstamos
        PrestamoResponse prestamoResponse = prestamoClient.obtenerPrestamoPorId(multaRequest.getPrestamoId(), token);

        //Mapear el request a la entidad Multa
        Multa multa = multaMapper.fromRequest(multaRequest);


        //Guardar la multa en la base de datos
        Multa savedMulta = multaRepository.save(multa);
        log.info("Multa creada exitosamente con ID: {}", savedMulta.getId());

        return multaMapper.toResponse(savedMulta);
    }

    //Listar todas la multas
    public List<MultaResponse> obtenerTodasLasMultas() {
        log.info("Obteniendo listado completo de multas");
        List<Multa> multas = multaRepository.findAll();
        return multas.stream()
                .map(multaMapper::toResponse)
                .toList();
    }

    //Listar multas asociadas a un usuario
    public List<MultaResponse> obtenerMultasPorUsuarioId(Long usuarioId) {
        log.info("Buscando multas para el Usuario ID: {}", usuarioId);
        List<Multa> multas = multaRepository.findAllByUsuarioId(usuarioId);
        return multas.stream()
                .map(multaMapper::toResponse)
                .toList();
    }

    //Eliminar multa por su id
    public void eliminarMulta(Long id) {
        log.info("Eliminando multa con ID: {}", id);
        if (multaRepository.existsById(id)) {
            multaRepository.deleteById(id);
        } else {
            log.warn("Se intentó eliminar una multa inexistente con ID: {}", id);
            // Aquí tu GlobalHandler atrapará esta excepción si la lanzas
            throw new java.util.NoSuchElementException("No se encontró la multa con ID: " + id);
        }
    }
}
