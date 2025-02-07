package com.example.jobportal.security;

import com.example.jobportal.config.JwtUtil;
import com.example.jobportal.model.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, @Lazy UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        System.out.println("JWT Filter Executing for request: " + request.getServletPath());

        // Skip authentication for /auth/** routes
        if (request.getServletPath().startsWith("/auth/")) {
            System.out.println("Skipping JWT authentication for auth routes.");
            chain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authorizationHeader);

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            System.out.println("Extracted JWT: " + jwt);

            if (jwt != null && !jwt.isEmpty()) {
                username = jwtUtil.extractUsername(jwt);
                System.out.println("Extracted Username: " + username);
            }
        } else {
            System.out.println("No JWT token found, proceeding without authentication.");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println("Loaded UserDetails: " + userDetails.getUsername());

                if (userDetails instanceof CustomUserDetails customUserDetails) {
                    if (jwtUtil.validateToken(jwt, customUserDetails.getUser())) {
                        System.out.println("JWT Token is valid. Setting authentication.");

                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                        System.out.println("Security Context Updated Successfully!");
                    } else {
                        System.out.println("JWT Token validation failed!");
                    }
                }
            } catch (Exception e) {
                System.out.println("Error during authentication: " + e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }
}
