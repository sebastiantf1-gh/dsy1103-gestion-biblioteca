package cl.duoc.dsy1103.resennas_microservice.service;


import cl.duoc.dsy1103.resennas_microservice.client.LibroClient;
import cl.duoc.dsy1103.resennas_microservice.client.UsuarioClient;
import cl.duoc.dsy1103.resennas_microservice.dto.ResennaRequest;
import cl.duoc.dsy1103.resennas_microservice.dto.ResennaResponse;
import cl.duoc.dsy1103.resennas_microservice.exception.ConflictException;
import cl.duoc.dsy1103.resennas_microservice.mapper.ResennaMapper;
import cl.duoc.dsy1103.resennas_microservice.model.Resenna;
import cl.duoc.dsy1103.resennas_microservice.repository.ResennaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional
public class ResennaService {

    @Autowired
    private ResennaRepository resennaRepository;

    @Autowired
    private ResennaMapper resennaMapper;

    @Autowired
    private LibroClient libroClient;

    @Autowired
    private UsuarioClient usuarioClient;


    public ResennaResponse crearResenna(ResennaRequest resennaRequest){
        log.info("creando resenna");
        usuarioClient.obtenerUsuarioPorId(resennaRequest.getIdUsuario());
        libroClient.obtenerLibrosPorId(resennaRequest.getIdLibro());
        if(resennaRepository.existsByIdUsuarioAndIdLibro(
                resennaRequest.getIdUsuario(),
                resennaRequest.getIdLibro())){
                    throw new ConflictException("El usuario ya hizo una reseña para este libro");
        }
        Resenna resenna = resennaMapper.fromRequest(resennaRequest);
        resenna.setFechaRegistro(LocalDateTime.now());
        Resenna resennaAGuardar = resennaRepository.save(resenna);
        return resennaMapper.toResponse(resennaAGuardar);
    }

    public ResennaResponse buscarResennaPorId(Long id){
        log.info("buscando resenna por Id {}", id);
        Resenna resenna = resennaRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("Resenna no encontrada"));
        return resennaMapper.toResponse(resenna);
    }

    public List<ResennaResponse> listarResennas(){
        log.info("Listando resennas");
        List<Resenna> resennas = resennaRepository.findAll();
        return resennas.stream()
                .map(resennaMapper::toResponse)
                .toList();
    }
    public ResennaResponse modificarResenna(Long id, ResennaRequest resennaRequest){
        log.info("modificando resenna: {}", id);
        Resenna resennaAModificar = resennaRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("Resenna no encontrada"));
        resennaAModificar.setCalificacion(resennaRequest.getCalificacion());
        resennaAModificar.setDescripcion(resennaRequest.getDescripcion());
        resennaAModificar.setIdLibro(resennaRequest.getIdLibro());

        Resenna resennaModificada = resennaRepository.save(resennaAModificar);
        return resennaMapper.toResponse(resennaModificada);

    }

    public void eliminarResenna(Long id){
        log.info("Eliminando resenna id: {}", id);
        if(!resennaRepository.existsById(id)){
            throw new NoSuchElementException("Resenna no encontrada " + id);
        }
        resennaRepository.deleteById(id);
    }

}
