package br.ifba.turisty.infrastructure.config;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.ifba.turisty.domain.user.model.User;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret; // Segredo usado para assinar e verificar tokens JWT, carregado a partir das propriedades da aplicação

    // Gera um token JWT para o usuário fornecido
    public String generateToken(User user) {
        try {
            // Define o algoritmo HMAC256 com o segredo fornecido
            Algorithm algorithm = Algorithm.HMAC256(secret);
            
            // Cria o token JWT
            String token = JWT.create()
                    .withIssuer("auth-api") // Define o emissor do token
                    .withSubject(user.getEmail()) // Define o sujeito (email do usuário) como o payload do token
                    .withExpiresAt(genExpirationDate()) // Define a data de expiração do token
                    .sign(algorithm); // Assina o token com o algoritmo HMAC256
            
            return token;
        } catch (JWTCreationException exception) {
            // Lança uma exceção de tempo de execução se ocorrer um erro na criação do token
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    // Valida o token JWT e retorna o sujeito (email) do token, ou uma string vazia se o token for inválido
    public String validateToken(String token) {
        try {
            // Define o algoritmo HMAC256 com o segredo fornecido
            Algorithm algorithm = Algorithm.HMAC256(secret);
            
            // Valida o token JWT e retorna o sujeito
            return JWT.require(algorithm)
                    .withIssuer("auth-api") // Verifica que o emissor do token é "auth-api"
                    .build()
                    .verify(token) // Verifica o token
                    .getSubject(); // Retorna o sujeito (email do usuário)
        } catch (JWTVerificationException exception) {
            // Retorna uma string vazia se o token for inválido ou ocorrer um erro de verificação
            return "";
        }
    }

    // Gera a data de expiração do token (2 horas a partir do momento atual)
    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00")); // Define o fuso horário como GMT-3
    }
}
