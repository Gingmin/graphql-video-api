package com.example.user.application;

import com.example.user.domain.User;
import java.util.Optional;

public interface UserRepository {
    User create(String name, String email, String passwordHash);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);
}