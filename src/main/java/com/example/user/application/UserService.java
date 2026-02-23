package com.example.user.application;

import com.example.auth.JwtService;
import com.example.user.domain.User;
import java.util.List;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public record AuthResult(User user, String accessToken) {}

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResult signUp(String name, String email, String password) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email must not be blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password must not be blank");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("email already exists");
        }

        var passwordHash = passwordEncoder.encode(password);
        var user = userRepository.create(name, email, passwordHash);

        String jti = UUID.randomUUID().toString();
        Map claims = new Map();
        claims.put("sub", user.id());
        claims.put("jti", jti);
        claims.put("email", email);

        var token = jwtService.generateToken(user.id(), claims);
        return new AuthResult(user, token);
    }
}