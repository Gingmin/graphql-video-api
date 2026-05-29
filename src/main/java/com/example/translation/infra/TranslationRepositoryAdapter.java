package com.example.translation.infra;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.translation.application.TranslationRepository;
import com.example.translation.domain.Translation;
import com.example.translation.infra.jpa.TranslationJpaEntity;
import com.example.translation.infra.jpa.TranslationJpaRepository;

@Repository
public class TranslationRepositoryAdapter implements TranslationRepository {
    private final TranslationJpaRepository jpaRepository;

    public TranslationRepositoryAdapter(TranslationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    private static Translation toDomain(TranslationJpaEntity e) {
        return new Translation(
            e.getId(),
            e.getTargetId(),
            e.getLanguage(),
            e.getName(),
            e.getCreatedAt(),
            e.getModifiedAt()
        );
    }

    @Override
    public List<Translation> findByTargetId(UUID targetId) {
        return jpaRepository.findByTargetId(targetId).stream()
            .map(TranslationRepositoryAdapter::toDomain)
            .toList();
    }

    @Override
    public Optional<Translation> findById(UUID id) {
        return jpaRepository.findById(id).map(TranslationRepositoryAdapter::toDomain);
    }

    @Override
    public Translation add(UUID targetId, String language, String name) {
        var entity = new TranslationJpaEntity(targetId, language, name);
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Translation modify(UUID id, String language, String name) {
        var entity = jpaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("translation not found"));
        entity.setLanguage(language);
        entity.setName(name);
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public boolean delete(UUID id) {
        jpaRepository.deleteById(id);
        return true;
    }
}
