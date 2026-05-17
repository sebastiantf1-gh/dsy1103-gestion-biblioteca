package cl.duoc.dsy1103.usuarios_microservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class JwtService {

    //Inyeccion de la llave secreta
    @Value("${jwt.secret}")
    private String jwtSecret;

    //Genera la clave de firma
    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    //metodo para extraer el token y sus datos
    public Claims extractClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //validacion del token
    public boolean isTokenValid(String token){
        try{
            extractClaims(token);
            return true;
        }catch (Exception e){
            log.error("Token JWT invalido: {}", e.getMessage());
            return false;
        }
    }

}
