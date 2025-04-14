package com.sourcery.km.service;

import com.sourcery.km.dto.SessionDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtService {
    public static final String SECRET = "5367566859703373367639792F423F452848284D6251655468576D5A71347437";
    public static final int EXPIRES_IN_SECONDS = 3600;
    public static final String TOKEN_TYPE = "Bearer";

    public String generateAccessToken() {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims);
    }

    public SessionDTO createNewSession() {
        return SessionDTO.builder()
                .tokenType(TOKEN_TYPE)
                .expiresInSeconds(String.valueOf(EXPIRES_IN_SECONDS))
                .accessToken(generateAccessToken())
                .build();
    }

    private String createToken(Map<String, Object> claims) {
        return Jwts.builder().claims(claims)
                .subject(UUID.randomUUID().toString())
                .issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + EXPIRES_IN_SECONDS * 1000L))
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUUID(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build().parseSignedClaims(token).getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
