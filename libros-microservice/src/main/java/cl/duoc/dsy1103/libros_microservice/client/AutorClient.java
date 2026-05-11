package cl.duoc.dsy1103.libros_microservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class AutorClient {

    @Autowired
    private WebClient autorWebClient;


}
