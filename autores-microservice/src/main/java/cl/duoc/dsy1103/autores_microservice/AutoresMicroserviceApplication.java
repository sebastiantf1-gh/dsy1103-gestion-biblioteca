package cl.duoc.dsy1103.autores_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //
@SpringBootApplication
public class AutoresMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutoresMicroserviceApplication.class, args);
	}

}
