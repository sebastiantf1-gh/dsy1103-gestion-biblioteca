package cl.duoc.dsy1103.prestamos_microservice.mapper;

import cl.duoc.dsy1103.prestamos_microservice.dto.LibroResponse;
import cl.duoc.dsy1103.prestamos_microservice.dto.PrestamoRequest;
import cl.duoc.dsy1103.prestamos_microservice.dto.PrestamoResponse;
import cl.duoc.dsy1103.prestamos_microservice.dto.UsuarioResponse;
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
    public PrestamoResponse toResponse(Prestamo prestamo, UsuarioResponse usuario, LibroResponse libro){
        return PrestamoResponse.builder()
                .id(prestamo.getId())
                .fechaPrestamo(prestamo.getFechaPrestamo())
                .fechaDevolucion(prestamo.getFechaDevolucion())
                .usuario(usuario)
                .libro(libro)
                .estado(prestamo.getEstado())
                .build();
    }
}
