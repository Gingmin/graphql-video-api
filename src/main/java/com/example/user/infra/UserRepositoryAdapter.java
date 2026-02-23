package com.example.user.infra;

import com.example.user.application.UserRepository;
import com.example.user.domain.User;
import com.example.user.infra.jpa.UserJpaEntity;
import com.example.user.infra.jpa.UserJpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository jpaRepository;

    public UserRepositoryAdapter(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public User create(String name, String email, String passwordHash) {
        var saved = jpaRepository.save(new UserJpaEntity(name, email, passwordHash));
        return toDomain(saved);
    }

    private static User toDomain(UserJpaEntity e) {
        return new User(
            e.getId(),
            e.getName(),
            e.getEmail(),
            e.getPasswordHash(),
            e.getLastLoginIp(),
            e.getLastLoginDate(),
            e.getCreatedAt(),
            e.getModifiedAt()
        );
    }
}