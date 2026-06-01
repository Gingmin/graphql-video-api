package com.example.file.infra.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.UUID;

public interface FileJpaRepository extends JpaRepository<FileJpaEntity, UUID> {

    @Query("SELECT f FROM FileJpaEntity f WHERE f.isDeleted = false ORDER BY f.createdAt DESC")
    Page<FileJpaEntity> findPage(Pageable pageable);

    @Query("SELECT f FROM FileJpaEntity f WHERE f.id = :id AND f.isDeleted = false")
    Optional<FileJpaEntity> findActiveById(UUID id);
}
