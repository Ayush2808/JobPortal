package com.example.jobportal.controller;

import com.example.jobportal.model.User;
import com.example.jobportal.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get user profile (Only authenticated users can access)
    @GetMapping("/profile")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')") // Restrict access
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Unauthorized: No user found");
        }
        User user = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }

    // Update user details
    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')") // Restrict access
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody User updatedUser) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Unauthorized: No user found");
        }
        User updated = userService.updateUser(userDetails.getUsername(), updatedUser);
        return ResponseEntity.ok(updated);
    }
}
