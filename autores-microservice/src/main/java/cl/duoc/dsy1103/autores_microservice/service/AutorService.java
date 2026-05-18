package cl.duoc.dsy1103.autores_microservice.service;

import cl.duoc.dsy1103.autores_microservice.dto.AutorRequest;
import cl.duoc.dsy1103.autores_microservice.dto.AutorResponse;
import cl.duoc.dsy1103.autores_microservice.dto.AutorUpdateRequest;
import cl.duoc.dsy1103.autores_microservice.mapper.AutorMapper;
import cl.duoc.dsy1103.autores_microservice.model.Autor;
import cl.duoc.dsy1103.autores_microservice.repository.AutorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.NoSuchElementException;

@Service // Registra la clase como un Bean de Servicio
@Slf4j //Inyecta de forma automática una instancia del framework logístico SLF4J (Simple Logging Facade for Java) bajo
        // la variable estática 'log', habilitando trazabilidad técnica de alto nivel.
public class AutorService {
    @Autowired //inyección de dependencias de forma automatica
    private AutorRepository autorRepository;

    @Autowired
    private AutorMapper autorMapper;

    //Buscar
    public List<AutorResponse> buscarAutores(){
       //Log de nivel INFO para trazabilidad. Registra la entrada al flujo de consulta general antes de entrar a persistencia.
        log.info("Obteniendo todos los autores con sus libros...");
        // Uso de la API Stream de Java.
        // '.stream()' procesa los datos en memoria de forma secuencial sin alterar la colección original de la BD.
        // '.map()' aplica la conversión del mapper a cada entidad en tiempo de ejecución.
        // '.toList()' empaqueta todo de manera inmutable en una nueva lista DTO.
        return autorRepository.findAll().stream() //stream para procesar datos sin afectar la lista original
                .map(autor -> {
                    return autorMapper.toResponse(autor);
                })
                .toList(); //empaquetar los nuevos objetos AutorResponse en una nueva lista.
    }
    //Buscar por ID
    public AutorResponse buscarAutorPorId(Long id){
        log.info("Obteniendo autor con ID: {}", id);
        // Uso defensivo de Optional con '.orElseThrow()'. Si el autor no existe,
        // se frena el flujo lanzando un error semántico controlado, evitando un quiebre de NullPointerException.
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró autor con ID: " + id));
        return autorMapper.toResponse(autor);
    }

    //Crear
    public AutorResponse crearAutor(AutorRequest autorRequest){
        //Log parametrizado para trazabilidad dinámica. Captura atributos específicos de la petición para auditorías del servidor.
        log.info("Creando nuevo autor: {}", autorRequest.getNombreCompleto());
        Autor autor = autorRepository.save(autorMapper.toEntity(autorRequest));
        // Al ser nuevo, retornamos una lista vacía de libros para mantener la consistencia del dto
        return autorMapper.toResponse(autor);
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
        return autorMapper.toResponse(autorActualizado);
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
