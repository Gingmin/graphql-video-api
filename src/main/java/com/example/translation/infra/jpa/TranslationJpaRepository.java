package com.example.translation.infra.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TranslationJpaRepository extends JpaRepository<TranslationJpaEntity, UUID> {
    List<TranslationJpaEntity> findByTargetId(UUID targetId);
}
