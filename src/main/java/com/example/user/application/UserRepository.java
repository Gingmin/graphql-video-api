package com.example.user.application;

import com.example.user.domain.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    record UserAuth(User user, String password) {}

    UserPage findPage(Integer page, Integer size);

    User create(String name, String email, String passwordHash);

    void login(UUID userId, String clientIp);

    boolean existsByEmail(String email);

    Optional<UserAuth> findAuthByEmail(String email);

    Optional<User> findById(UUID id);
}
