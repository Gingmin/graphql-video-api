package com.example.user.application;

import com.example.user.domain.User;
import java.util.Optional;

public interface UserRepository {

    record UserAuth(User user, String password) {}


    User create(String name, String email, String passwordHash);

    boolean existsByEmail(String email);

    Optional<UserAuth> findAuthByEmail(String email);

    Optional<User> findById(long id);
}