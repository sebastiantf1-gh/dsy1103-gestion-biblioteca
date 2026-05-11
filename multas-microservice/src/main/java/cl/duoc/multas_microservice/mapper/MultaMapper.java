package cl.duoc.multas_microservice.mapper;

import cl.duoc.multas_microservice.dto.MultaRequest;
import cl.duoc.multas_microservice.dto.MultaResponse;
import cl.duoc.multas_microservice.model.Multa;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
public class MultaMapper {

    //Convertir de DTO a Entidad
    public Multa toEntity(MultaRequest request){
        if(request == null)
            return null;
        LocalDateTime ahora = LocalDateTime.now();

        //Se verifica si la fechaLimitePago == null y si lo es, se le da como valor la fecha actual y se le suma 10 dias
        //de gracia
        LocalDateTime limite = (request.getFechaLimitePago() !=null)? request.getFechaLimitePago() : ahora.plusDays(10);

        return Multa.builder()
                .monto(request.getMonto())
                .fechaRegistro(ahora)
                .fechaLimitePago(limite)
                .idUsuario(request.getIdUsuario())
                .idPrestamo(request.getIdPrestamo())
                .build();
    }

    //Covierte la entidad a respuesta de servidor
    public MultaResponse toResponse(Multa multa){

        return MultaResponse.builder()
                .id(multa.getId())
                .monto(multa.getMonto())
                .fechaRegistro(multa.getFechaRegistro())
                .fechaLimitePago(multa.getFechaLimitePago())
                .idUsuario(multa.getIdUsuario())
                .idPrestamo(multa.getIdPrestamo())
                .build();
    }
}
