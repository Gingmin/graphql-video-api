package com.example.user.application;

import com.example.user.domain.User;

public interface UserRepository {
    User create(String name, String email, String passwordHash);

    boolean existsByEmail(String email);
}