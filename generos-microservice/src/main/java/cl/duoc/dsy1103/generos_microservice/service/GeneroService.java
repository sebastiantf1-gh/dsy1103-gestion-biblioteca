package cl.duoc.dsy1103.generos_microservice.service;

import cl.duoc.dsy1103.generos_microservice.dto.GeneroRequest;
import cl.duoc.dsy1103.generos_microservice.dto.GeneroResponse;
import cl.duoc.dsy1103.generos_microservice.dto.GeneroUpdateRequest;
import cl.duoc.dsy1103.generos_microservice.mapper.GeneroMapper;
import cl.duoc.dsy1103.generos_microservice.model.Genero;
import cl.duoc.dsy1103.generos_microservice.repository.GeneroRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class GeneroService {

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private GeneroMapper generoMapper;

    //le puse agregarGenero por el punto de vista del negocio, los géneros son categorias
    //que ya existen y que el cliente puede incorporar al sistema
    public GeneroResponse agregarGenero(GeneroRequest generoRequest){
        log.info("agregando genero: {}", generoRequest.getNombreGenero());
        Genero genero = generoRepository.save(generoMapper.fromRequest(generoRequest));
        return generoMapper.toResponse(genero);
    }

    public List<GeneroResponse> listarGeneros(){
        log.info("listando todos los generos");
        List<Genero> generos = generoRepository.findAll();
        return generos.stream()
                .map(generoMapper::toResponse)
                .toList();
    }
    public GeneroResponse buscarGeneroPorId(Long id){
        log.info("buscando id: {} ", id);
        Genero genero = generoRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("No se encontro el genero con id: " + id));
        return generoMapper.toResponse(genero);
    }
    public GeneroResponse modificarGenero(Long id, GeneroUpdateRequest request){
        log.info("modificando Genero id: {}", id);
        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("genero no encontrado :" + id));
        // Actualizaciones parciales para que el cliente modifique solo los campos enviados
        if(request.getNombreGenero()!=null){
            genero.setNombreGenero(request.getNombreGenero());
        }
        if(request.getDescripcion()!=null){
            genero.setDescripcion(request.getDescripcion());
        }


        Genero generoModificado = generoRepository.save(genero);
        return generoMapper.toResponse(generoModificado);
    }
    public void eliminarGenero(Long id){
        log.info("Eliminando Genero Id: {}", id);
        if(!generoRepository.existsById(id)){
            throw new NoSuchElementException("No se encontro el genero id: " + id);
        }
        generoRepository.deleteById(id);
    }




}
