package com.example.translation.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import com.example.translation.domain.Translation;

@Service
public class TranslationService {
    private final TranslationRepository translationRepository;

    public TranslationService(TranslationRepository translationRepository) {
        this.translationRepository = translationRepository;
    }

    @Transactional(readOnly = true)
    public List<Translation> findByTargetId(UUID targetId) {
        return translationRepository.findByTargetId(targetId);
    }

    @Transactional(readOnly = true)
    public Translation findById(UUID id) {
        return translationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("translation not found"));
    }

    @Transactional
    public Translation add(UUID targetId, String language, String name) {
        if (targetId == null) {
            throw new IllegalArgumentException("targetId must not be null");
        }
        if (language == null || language.isBlank()) {
            throw new IllegalArgumentException("language must not be blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        return translationRepository.add(targetId, language, name);
    }

    @Transactional
    public Translation modify(UUID id, String language, String name) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (language == null || language.isBlank()) {
            throw new IllegalArgumentException("language must not be blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        return translationRepository.modify(id, language, name);
    }

    @Transactional
    public boolean delete(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return translationRepository.delete(id);
    }
}
