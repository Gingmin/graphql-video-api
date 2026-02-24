package com.example.user.application;

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

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        String jti = UUID.randomUUID().toString();
        Map claims = new Map();
        claims.put("sub", user.id());
        claims.put("jti", jti);
        claims.put("email", email);

        var token = jwtService.generateToken(user.id(), claims);
        return new AuthResult(user, token);
        // return userRepository.create(name, email, passwordHash);
    }

    @Transactional(readOnly = true)
    public User login(String email, String password) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("invalid email or password"));

        // 비밀번호 검증은 DB에 저장된 해시가 필요하므로,
        // 여기서는 domain User에 password가 없으므로 infra 레벨에서 검증이 필요합니다.
        // 현재 구조에서는 adapter에서 password 검증 후 User를 반환하는 방식을 권장합니다.
        return user;
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
    }
}