package com.example.jobportal.service;

import com.example.jobportal.dto.RegisterUserDto;
import com.example.jobportal.model.User;
import com.example.jobportal.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
@Getter
@Setter

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegisterUserDto dto) {
        Optional<User> existingUser = userRepository.findByEmail(dto.getEmail());

        if (existingUser.isPresent()) {
            throw new RuntimeException("User with email " + dto.getEmail() + " already exists.");
        }

        User user = new User();
        user.setUsername(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(Collections.singleton("ROLE_USER")); // ✅ Correct


        return userRepository.save(user);
    }
}
