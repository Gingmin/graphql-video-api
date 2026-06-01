package com.example.translation.infra.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TranslationJpaRepository extends JpaRepository<TranslationJpaEntity, UUID> {

    @Query("SELECT t FROM TranslationJpaEntity t WHERE t.targetId = :targetId AND t.isDeleted = false")
    List<TranslationJpaEntity> findActiveByTargetId(UUID targetId);

    @Query("SELECT t FROM TranslationJpaEntity t WHERE t.id = :id AND t.isDeleted = false")
    Optional<TranslationJpaEntity> findActiveById(UUID id);
}
