package com.tensai.cms.auth.internal.service;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import com.tensai.cms.auth.internal.config.SecurityProperties;
import com.tensai.cms.auth.internal.entity.TokenPurpose;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final SecurityProperties properties;

    private static final int MILLIS_PER_MINUTE = 60000;
    private static final int MINUTES_PER_DAY = 1440;


    public String generateAccessToken(Map<String, Object> claims, String subject, int expiryMinutes) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (long) MILLIS_PER_MINUTE * expiryMinutes))
                .signWith(getSignInKey())
                .compact();
    }

    public String generateAccessToken(Map<String, Object> claims, String subject) {
        return generateAccessToken(claims, subject, MINUTES_PER_DAY);
    }

    public boolean validateAccessToken(String token, TokenPurpose tokenPurpose) {

        String purpose = extractPurpose(token);

        return (purpose.equals(tokenPurpose.name()) && !isTokenExpired(token));
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractPurpose(String token) {
        return extractClaim(token, Claims -> Claims.get("purpose", String.class));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(properties.jwtSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
