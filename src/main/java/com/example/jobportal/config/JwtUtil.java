package com.example.jobportal.config;

import com.example.jobportal.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")  // Load secret key from application.properties
    private String secretKey;

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

    // Generate JWT token for a user
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername()) // Set username as the subject
                .setIssuedAt(new Date()) // Issue time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Set expiration
                .signWith(SignatureAlgorithm.HS256, secretKey) // Use HS256 algorithm with secret key
                .compact();
    }

    // Extract username from JWT token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Extract expiration date from JWT token
    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    // Check if token is expired
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate JWT token
    public boolean validateToken(String token, User user) {
        String username = extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    // Extract all claims from JWT token
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }


}
