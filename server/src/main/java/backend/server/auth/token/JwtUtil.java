package backend.server.auth.token;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final Key secretKey; // key aleatória no arquivo .env

    private static final long EXPIRATION_TIME = 604800000; // Tempo de vida do token em ms (7 dias)

    // jwt.secret pega o valor em application.properties que por sua vez pega do .env
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        // Garante chave longa o suficiente para HS512
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Gera de token
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // Decodifica e pega o email do usuário dentro do token gerado
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // pega o username e do token e compara
    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = extractEmail(token); // Email como username pq n tem username no DB
        return username.equals(userDetails.getUsername()); // Comparando
    }
}
