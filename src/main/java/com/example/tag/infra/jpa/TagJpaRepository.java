package com.example.tag.infra.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.UUID;

public interface TagJpaRepository extends JpaRepository<TagJpaEntity, UUID> {
    
    @Query("SELECT t FROM TagJpaEntity t ORDER BY t.createdAt DESC")
    Page<TagJpaEntity> findPage(Pageable pageable);

    Optional<TagJpaEntity> findById(UUID id);

    boolean existsByCode(String code);
}
