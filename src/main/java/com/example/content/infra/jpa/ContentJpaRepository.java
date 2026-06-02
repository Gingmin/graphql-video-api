package com.example.content.infra.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.UUID;

public interface ContentJpaRepository extends JpaRepository<ContentJpaEntity, UUID> {
    
    @Query("SELECT c FROM ContentJpaEntity c WHERE c.isDeleted = false ORDER BY c.createdAt DESC")
    Page<ContentJpaEntity> findPage(Pageable pageable);

    @Query("SELECT c FROM ContentJpaEntity c WHERE c.id = :id AND c.isDeleted = false")
    Optional<ContentJpaEntity> findActiveById(UUID id);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ContentJpaEntity c WHERE c.title = :title AND c.isDeleted = false")
    boolean existsByTitle(String title);
}
