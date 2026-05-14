package cl.duoc.dsy1103.autores_microservice.service;

import cl.duoc.dsy1103.autores_microservice.client.LibroClient;
import cl.duoc.dsy1103.autores_microservice.dto.AutorRequest;
import cl.duoc.dsy1103.autores_microservice.dto.AutorResponse;
import cl.duoc.dsy1103.autores_microservice.dto.AutorUpdateRequest;
import cl.duoc.dsy1103.autores_microservice.dto.LibroResponse;
import cl.duoc.dsy1103.autores_microservice.mapper.AutorMapper;
import cl.duoc.dsy1103.autores_microservice.model.Autor;
import cl.duoc.dsy1103.autores_microservice.repository.AutorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class AutorService {
    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private AutorMapper autorMapper;

    @Autowired
    private LibroClient libroClient;

    //Buscar
    public List<AutorResponse> buscarAutores(){
        log.info("Obteniendo todos los autores con sus libros...");
        return autorRepository.findAll().stream() //stream para procesar datos sin afectar la lista original
                .map(autor -> {
                    List<LibroResponse> libros = libroClient.buscarLibrosPorAutor(autor.getId());
                    return autorMapper.toResponse(autor, libros);
                })
                .toList(); //empaquetar los nuevos objetos AutorResponse en una nueva lista.
    }
    //Buscar por ID
    public AutorResponse buscarAutorPorId(Long id){
        log.info("Obteniendo autor con ID: {}", id);
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró autor con ID: " + id));
        List<LibroResponse> libros = libroClient.buscarLibrosPorAutor(id);
        return autorMapper.toResponse(autor, libros);
    }

    //Crear
    public AutorResponse crearAutor(AutorRequest autorRequest){
        log.info("Creando nuevo autor: {}", autorRequest.getNombreCompleto());
        Autor autor = autorRepository.save(autorMapper.toEntity(autorRequest));
        // Al ser nuevo, retornamos una lista vacía de libros para mantener la consistencia del dto
        return autorMapper.toResponse(autor, Collections.emptyList());
    }

    //Actualizar
    public AutorResponse actualizarAutor(Long id, AutorUpdateRequest request){
        log.info("Actualizando autor con ID: {}", id);
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró autor con ID: " + id));

        if (request.getNombreCompleto() != null){
            autor.setNombreCompleto(request.getNombreCompleto());
        }
        if (request.getBiografia() != null){
            autor.setBiografia(request.getBiografia());
        }
        if (request.getNacionalidad() != null){
            autor.setNacionalidad(request.getNacionalidad());
        }
        if (request.getFechaNacimiento() != null){
            autor.setFechaNacimiento(request.getFechaNacimiento());
        }
        Autor autorActualizado = autorRepository.save(autor);
        List<LibroResponse> libros = libroClient.buscarLibrosPorAutor(id);
        return autorMapper.toResponse(autorActualizado, libros);
    }

    //Eliminar
    public void eliminarAutor (Long id){
        log.info("Eliminando autor con ID: {}", id);
        if (!autorRepository.existsById(id)){
            throw new NoSuchElementException("No se encontró autor con ID: " + id);
        }
        autorRepository.deleteById(id);
    }

}
