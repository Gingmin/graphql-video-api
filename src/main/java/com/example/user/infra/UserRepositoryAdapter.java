package com.example.user.infra;

import com.example.user.application.UserPage;
import com.example.user.application.UserRepository;
import com.example.user.domain.User;
import com.example.user.infra.jpa.UserJpaEntity;
import com.example.user.infra.jpa.UserJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import java.time.Instant;

@Repository
public class UserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository jpaRepository;

    public UserRepositoryAdapter(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public UserPage findPage(Integer page, Integer size) {
        var pageable = PageRequest.of(page - 1, size);
        var pageResult = jpaRepository.findPage(pageable);
        var users = pageResult.getContent();

        if (users.isEmpty()) {
            return new UserPage(
                List.of(),
                pageResult.getNumber() + 1,
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.hasNext(),
                pageResult.hasPrevious()
            );
        }

        var items = users.stream().map(UserRepositoryAdapter::toDomain).toList();

        return new UserPage(
            items,
            pageResult.getNumber() + 1,
            pageResult.getSize(),
            pageResult.getTotalElements(),
            pageResult.getTotalPages(),
            pageResult.hasNext(),
            pageResult.hasPrevious()
        );
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
    public void login(UUID id, String clientIp) {
        var entity = jpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("user not found"));

        entity.setLatestLoginIp(clientIp);
        entity.setLastLoginDate(Instant.now());
        jpaRepository.save(entity);
    }

    @Override
    public Optional<UserAuth> findAuthByEmail(String email) {
        return jpaRepository.findActiveByEmail(email).map(e -> new UserAuth(toDomain(e), e.getPassword()));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findActiveById(id).map(UserRepositoryAdapter::toDomain);
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
