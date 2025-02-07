package com.example.jobportal.config;

import com.example.jobportal.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")  // Load secret key from application.properties
    private String secretKey;

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        Claims claims = extractClaims(token);
        return claims != null ? claims.getSubject() : null;
    }

    public Date extractExpiration(String token) {
        Claims claims = extractClaims(token);
        return claims != null ? claims.getExpiration() : null;
    }

    public boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration != null && expiration.before(new Date());
    }
    public boolean validateToken(String token, User user) {
        String username = extractUsername(token);

        System.out.println("Validating Token for Username: " + username);

        if (username == null) {
            System.out.println("Token is invalid (Username extraction failed)");
            return false;
        }

        if (!username.equals(user.getUsername())) {
            System.out.println("Token validation failed: Token username does not match user's username");
            return false;
        }

        boolean isExpired = isTokenExpired(token);
        System.out.println("Is Token Expired: " + isExpired);

        if (isExpired) {
            System.out.println("Token validation failed: Token has expired");
            return false;
        }

        System.out.println("Token is valid");
        return true;
    }



    private Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            System.out.println("Token Expired: " + e.getMessage());
        } catch (MalformedJwtException | SignatureException e) {
            System.out.println("Invalid Token: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Token Parsing Error: " + e.getMessage());
        }
        return null;
    }
}
