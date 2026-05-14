package cl.duoc.dsy1103.prestamos_microservice.mapper;

import cl.duoc.dsy1103.prestamos_microservice.dto.PrestamoRequest;
import cl.duoc.dsy1103.prestamos_microservice.dto.PrestamoResponse;
import cl.duoc.dsy1103.prestamos_microservice.model.Prestamo;
import org.springframework.stereotype.Component;

@Component
public class PrestamoMapper {
    public Prestamo toEntity(PrestamoRequest request){
        return Prestamo.builder()
                .fechaDevolucion(request.getFechaDevolucion())
                .idUsuario(request.getIdUsuario())
                .idLibro(request.getIdLibro())
                .estado(request.getEstado())
                .build();
    }
    public PrestamoResponse toRepsonse(Prestamo prestamo){
        return PrestamoResponse.builder()
                .id(prestamo.getId())
                .fechaPrestamo(prestamo.getFechaPrestamo())
                .fechaDevolucion(prestamo.getFechaDevolucion())
                .idUsuario(prestamo.getIdUsuario())
                .idLibro(prestamo.getIdLibro())
                .estado(prestamo.getEstado())
                .build();
    }
}
