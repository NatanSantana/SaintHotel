package com.example.Saint.InfraSecurity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${secret.key}")
    private String secret_key;
    private final long EXPIRATION = 1000 * 60 * 60;

    // gera o token
    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .setSubject(user.getUsername()) // dono do token
                .setIssuedAt(new Date()) // data de criação do token
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // data de expiração
                .signWith(Keys.hmacShaKeyFor(secret_key.getBytes()), SignatureAlgorithm.HS256) // assinatura de criptografia
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails user) {
        return extractUsername(token).equals(user.getUsername()) && !isExpired(token);
    }


    private boolean isExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret_key.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
