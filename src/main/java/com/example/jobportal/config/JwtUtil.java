package com.example.jobportal.config;

import com.example.jobportal.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private String secretKey = "secret";

    // Generate JWT token for a user
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())  // Set the username as the subject of the token
                .setIssuedAt(new Date())  // Set the issue time of the token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiration
                .signWith(SignatureAlgorithm.HS256, secretKey)  // Use HS256 algorithm and secret key
                .compact();
    }



}
