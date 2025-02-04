package com.example.jobportal.controller;



import com.example.jobportal.dto.RegisterUserDto;
import com.example.jobportal.model.User;
import com.example.jobportal.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegisterUserDto dto) {
        return ResponseEntity.ok(authService.registerUser(dto));
    }
}

