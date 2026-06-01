package com.example.tag.infra.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.UUID;

public interface TagJpaRepository extends JpaRepository<TagJpaEntity, UUID> {
    
    @Query("SELECT t FROM TagJpaEntity t WHERE t.isDeleted = false ORDER BY t.createdAt DESC")
    Page<TagJpaEntity> findPage(Pageable pageable);

    @Query("SELECT t FROM TagJpaEntity t WHERE t.id = :id AND t.isDeleted = false")
    Optional<TagJpaEntity> findActiveById(UUID id);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM TagJpaEntity t WHERE t.code = :code AND t.isDeleted = false")
    boolean existsByCode(String code);
}
