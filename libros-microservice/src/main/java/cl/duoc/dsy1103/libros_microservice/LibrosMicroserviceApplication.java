package cl.duoc.dsy1103.libros_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LibrosMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibrosMicroserviceApplication.class, args);
	}

}
