package com.example.user.application;

import com.example.auth.JwtService;
import com.example.user.domain.User;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
    public User signUp(String name, String email, String password) {
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

        return user;
    }

    @Transactional
    public AuthResult login(String email, String password, String clientIp) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email must not be blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password must not be blank");
        }

        var auth = userRepository.findAuthByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("invalid credentials"));
        
        if (!passwordEncoder.matches(password, auth.password())) {
            throw new IllegalArgumentException("invalid credentials");
        }

        String jti = UUID.randomUUID().toString();
        User user = auth.user();
        String id = String.valueOf(user.id());

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", id);
        claims.put("jti", jti);
        claims.put("email", email);
        
        var token = jwtService.generateToken(id, claims);

        userRepository.login(user.id(), jti, clientIp);

        return new AuthResult(user, token);
    }

    @Transactional(readOnly = true)
    public User getById(String id) {
        return userRepository.findById(Long.parseLong(id)).orElseThrow(() -> new IllegalArgumentException("user not found"));
    }
}