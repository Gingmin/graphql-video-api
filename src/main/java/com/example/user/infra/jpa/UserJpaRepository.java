package com.example.user.infra.jpa;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    boolean existsByEmail(String email);

    Optional<UserJpaEntity> findByEmail(String email);

    Optional<UserJpaEntity> findById(UUID id);

    @Query("SELECT u FROM UserJpaEntity u ORDER BY u.createdAt DESC")
    Page<UserJpaEntity> findPage(Pageable pageable);
}
