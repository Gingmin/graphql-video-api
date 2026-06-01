package com.example.genre.infra.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.UUID;

public interface GenreJpaRepository extends JpaRepository<GenreJpaEntity, UUID> {
    
    @Query("SELECT g FROM GenreJpaEntity g WHERE g.isDeleted = false ORDER BY g.createdAt DESC")
    Page<GenreJpaEntity> findPage(Pageable pageable);

    @Query("SELECT g FROM GenreJpaEntity g WHERE g.id = :id AND g.isDeleted = false")
    Optional<GenreJpaEntity> findActiveById(UUID id);

    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END FROM GenreJpaEntity g WHERE g.code = :code AND g.isDeleted = false")
    boolean existsByCode(String code);
}
