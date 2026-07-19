package com.secondhand.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class JwtService {


    private final String SECRET_KEY =
            "secondhandSecretKeySecondhandProject123456789";


    public String generateToken(String username) {


        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                + 1000 * 60 * 60 * 24
                        )
                )
                .signWith(
                        SignatureAlgorithm.HS256,
                        SECRET_KEY
                )
                .compact();

    }


    public String extractUsername(String token) {


        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

    }

    public boolean isTokenValid(String token, String username) {

        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
        }

        private boolean isTokenExpired(String token) {

        Date expiration = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiration.before(new Date());
        }
}