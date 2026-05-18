package cl.duoc.dsy1103.autores_microservice.repository;

import cl.duoc.dsy1103.autores_microservice.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //Indica a Spring que esta interfaz es un componente de acceso a datos. Permite la traducción automática de
            // excepciones de persistencia (como restricciones de BD) a excepciones de Spring, y la habilita para ser
            //inyectada mediante @Autowired en la capa de Servicio.
public interface AutorRepository extends JpaRepository<Autor,Long> {
}
