package com.example.user.infra.jpa;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserJpaEntity u WHERE u.email = :email AND u.isDeleted = false")
    boolean existsByEmail(String email);

    @Query("SELECT u FROM UserJpaEntity u WHERE u.email = :email AND u.isDeleted = false")
    Optional<UserJpaEntity> findActiveByEmail(String email);

    @Query("SELECT u FROM UserJpaEntity u WHERE u.id = :id AND u.isDeleted = false")
    Optional<UserJpaEntity> findActiveById(UUID id);

    @Query("SELECT u FROM UserJpaEntity u WHERE u.isDeleted = false ORDER BY u.createdAt DESC")
    Page<UserJpaEntity> findPage(Pageable pageable);
}
