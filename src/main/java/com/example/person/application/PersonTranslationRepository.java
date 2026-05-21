package com.example.person.application;

import java.util.List;
import java.util.Optional;

import com.example.person.domain.PersonTranslation;

public interface PersonTranslationRepository {

    List<PersonTranslation> findByPersonId(Long personId);

    Optional<PersonTranslation> findById(Long id);

    PersonTranslation add(Long personId, String language, String name);

    PersonTranslation modify(Long id, String language, String name);

    boolean delete(Long id);
}
