package cl.duoc.reservas_microservice.mapper;

import cl.duoc.reservas_microservice.dto.CrearReservaRequest;
import cl.duoc.reservas_microservice.dto.ReservaResponse;
import cl.duoc.reservas_microservice.model.Reserva;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapper {
    /**
     * Convierte el DTO de petición en la Entidad para la Base de Datos.
     */
    public Reserva toEntity(CrearReservaRequest request) {
        if (request == null) {
            return null;
        }

        return Reserva.builder()
                .idLibro(request.getIdLibro())
                .idUsuario(request.getIdUsuario())
                .fechaInicio(request.getFechaInicio())
                .fechaTermino(request.getFechaTermino())
                .estado("ACTIVA") // Toda reserva parte con estado ACTIVA por defecto
                .build();
    }

    /**
     * Convierte la Entidad de la BD en el DTO de respuesta para el cliente.
     */
    public ReservaResponse toResponse(Reserva entity) {
        if (entity == null) {
            return null;
        }

        return ReservaResponse.builder()
                .id(entity.getId())
                .idLibro(entity.getIdLibro())
                .idUsuario(entity.getIdUsuario())
                .fechaInicio(entity.getFechaInicio())
                .fechaTermino(entity.getFechaTermino())
                .estado(entity.getEstado())
                .build();
    }
}
