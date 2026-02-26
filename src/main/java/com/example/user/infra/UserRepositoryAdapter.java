package com.example.user.infra;

import com.example.user.application.UserRepository;
import com.example.user.domain.User;
import com.example.user.infra.jpa.UserJpaEntity;
import com.example.user.infra.jpa.UserJpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.time.Instant;

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

    @Override
    public void login(long id, String jti, String clientIp) {
        var entity = jpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("user not found"));

        entity.setJti(UUID.fromString(jti));
        entity.setLatestLoginIp(clientIp);
        entity.setLastLoginDate(Instant.now());
        jpaRepository.save(entity);
    }

    @Override
    public Optional<UserAuth> findAuthByEmail(String email) {
        return jpaRepository.findByEmail(email).map(e -> new UserAuth(toDomain(e), e.getPassword()));
    }

    @Override
    public Optional<User> findById(long id) {
        return jpaRepository.findById(id).map(UserRepositoryAdapter::toDomain);
    }

    private static User toDomain(UserJpaEntity e) {
        return new User(
            e.getId(),
            e.getName(),
            e.getEmail(),
            e.getLatestLoginIp(),
            e.getLastLoginDate(),
            e.getCreatedAt(),
            e.getModifiedAt()
        );
    }
}