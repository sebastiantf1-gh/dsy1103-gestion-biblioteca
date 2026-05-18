package cl.duoc.dsy1103.autores_microservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service //Declara esta clase como un Bean de Servicio especializado en seguridad dentro del contenedor de Spring,
        // permitiendo su reutilización en filtros o interceptores de peticiones HTTP.
@Slf4j // Inyecta el componente de logs SLF4J, el cual se utiliza críticamente aquí para dejar trazas auditables ante
        // intentos de accesos con firmas corruptas o tokens alterados.
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
    // Convierte el String secreto a un arreglo de bytes usando codificación UTF_8 estándar.
    // Luego, 'Keys.hmacShaKeyFor' genera una firma criptográfica simétrica compatible con algoritmos robustos como HS256.

    public Claims extractClaims(String token){
        // Uso de la API fluida de JJWT.
        // 'parser()' configura el lector.
        // 'verifyWith()' inyecta la firma simétrica secreta local para validar que el token no haya sido modificado en el camino.
        // 'parseSignedClaims()' analiza y descifra la estructura.
        // 'getPayload()' extrae el mapa de atributos de identidad del usuario.
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token){
        try {
            // Control de flujo defensivo preventivo Si 'extractClaims(token)' se ejecuta con éxito,
            // significa que la firma digital pasó los controles criptográficos rigurosos y el token es legal, por lo que retorna true de inmediato
            extractClaims(token);
            return true;
        }catch (Exception e){
            // Captura de excepciones e inyección de logs estructurados de nivel ERROR
            // En vez de dejar que el hilo de ejecución del servidor colapse lanzando un error 500, capturamos el fallo de seguridad,
            // registramos el mensaje técnico descriptivo en la consola para depuración y retornamos false de forma controlada.
            log.error("Token JWT invalido: {}", e.getMessage());
            return false;
        }
    }
}
