package com.example.translation.application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.translation.domain.Translation;

public interface TranslationRepository {

    List<Translation> findByTargetId(UUID targetId);

    Optional<Translation> findById(UUID id);

    Translation add(UUID targetId, String language, String name);

    Translation modify(UUID id, String language, String name);

    boolean delete(UUID id);
}
