package cl.duoc.multas_microservice.service;

import cl.duoc.multas_microservice.dto.MultaRequest;
import cl.duoc.multas_microservice.dto.MultaResponse;
import cl.duoc.multas_microservice.mapper.MultaMapper;
import cl.duoc.multas_microservice.repository.MultaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
public class MultaService {
    @Autowired
    private MultaRepository multaRepository;

    @Autowired
    private MultaMapper multaMapper;

    public MultaResponse crearMulta(MultaRequest multaRequest, String token){
        log.info("Creando nueva multa para el usuario indicado: {}", multaRequest.getIdUsuario());

    }
}
