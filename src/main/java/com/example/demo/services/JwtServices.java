package com.example.demo.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.domain.entities.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtServices {
    public String generateJwtToken(User user) {
        Instant expiresAt = generateExpiresAt(2);

        try {
            Algorithm algorithm = Algorithm.HMAC256("SECRET_KEY");
            return JWT.create()
                    .withIssuer("api")
                    .withSubject(user.getLogin())
                    .withExpiresAt(expiresAt)
                    .withClaim("id", user.getId())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("token creation has failed: " + exception);
        }
    }

    public String extractSubjectFromJwtToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("SECRET_KEY");
            return JWT.require(algorithm)
                    .withIssuer("api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            throw new RuntimeException("invalid token: " + exception);
        }
    }

    private Instant generateExpiresAt(Integer span) {
        return LocalDateTime.now().plusHours(span).toInstant(ZoneOffset.of("-03:00"));
    }
}
