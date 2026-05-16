package cl.duoc.dsy1103.resennas_microservice.service;


import cl.duoc.dsy1103.resennas_microservice.client.LibroClient;
import cl.duoc.dsy1103.resennas_microservice.client.UsuarioClient;
import cl.duoc.dsy1103.resennas_microservice.dto.*;
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

        LibroResponse libro = libroClient.obtenerLibrosPorId(resennaRequest.getIdLibro());
        UsuarioResponse usuario = usuarioClient.obtenerUsuarioPorId(resennaRequest.getIdUsuario());

        if(libro.getTitulo().contains("no está disponible") || usuario.getNombreCompleto().contains("no está disponible")){
            throw new NoSuchElementException("No se puede crear la reseña: El usuario o el libro especificado no existe.");
        }

        if(resennaRepository.existsByIdUsuarioAndIdLibro(
                resennaRequest.getIdUsuario(),
                resennaRequest.getIdLibro())){
                    throw new ConflictException("El usuario ya hizo una reseña para este libro");
        }
        Resenna resenna = resennaMapper.fromRequest(resennaRequest);
        resenna.setFechaRegistro(LocalDateTime.now());

        Resenna resennaAGuardar = resennaRepository.save(resenna);
        return resennaMapper.toResponse(resennaAGuardar, libro, usuario);
    }


    public ResennaResponse buscarResennaPorId(Long id){
        log.info("buscando resenna por Id {}", id);
        Resenna resenna = resennaRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("Resenna no encontrada"));
        LibroResponse libro = libroClient.obtenerLibrosPorId(resenna.getIdLibro());
        UsuarioResponse usuario = usuarioClient.obtenerUsuarioPorId(resenna.getIdUsuario());
        return resennaMapper.toResponse(resenna, libro , usuario);
    }

    public List<ResennaResponse> listarResennas(){
        log.info("Listando resennas");
        List<Resenna> resennas = resennaRepository.findAll();
        return resennas.stream()
                .map(resenna ->{
                    LibroResponse libro = libroClient.obtenerLibrosPorId(resenna.getIdLibro());
                    UsuarioResponse usuario = usuarioClient.obtenerUsuarioPorId(resenna.getIdUsuario());
                    return resennaMapper.toResponse(resenna, libro , usuario);
                })
                .toList();
    }
    public ResennaResponse modificarResenna(Long id, ResennaUpdateRequest request){
        log.info("modificando resenna: {}", id);
        Resenna resenna = resennaRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("Resenna no encontrada"));

        if(request.getCalificacion()!=null){
            resenna.setCalificacion(request.getCalificacion());
        }
        if(request.getDescripcion()!=null){
            resenna.setDescripcion(request.getDescripcion());
        }

        Resenna resennaModificada = resennaRepository.save(resenna);

        LibroResponse libro = libroClient.obtenerLibrosPorId(resennaModificada.getIdLibro());
        UsuarioResponse usuario = usuarioClient.obtenerUsuarioPorId(resennaModificada.getIdUsuario());

        return resennaMapper.toResponse(resennaModificada, libro, usuario);
    }

    public void eliminarResenna(Long id){
        log.info("Eliminando resenna id: {}", id);
        if(!resennaRepository.existsById(id)){
            throw new NoSuchElementException("Resenna no encontrada " + id);
        }
        resennaRepository.deleteById(id);
    }

}
