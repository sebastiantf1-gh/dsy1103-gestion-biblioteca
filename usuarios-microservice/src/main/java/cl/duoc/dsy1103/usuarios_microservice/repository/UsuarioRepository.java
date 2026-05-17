package cl.duoc.dsy1103.usuarios_microservice.repository;

import cl.duoc.dsy1103.usuarios_microservice.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//Con el JpaRepository ya tenemos los metodos CRUD listos
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
