package com.example.person.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.example.person.domain.PersonTranslation;

@Service
public class PersonTranslationService {
    private final PersonTranslationRepository personTranslationRepository;

    public PersonTranslationService(PersonTranslationRepository personTranslationRepository) {
        this.personTranslationRepository = personTranslationRepository;
    }

    @Transactional(readOnly = true)
    public List<PersonTranslation> findByPersonId(Long personId) {
        return personTranslationRepository.findByPersonId(personId);
    }

    @Transactional(readOnly = true)
    public PersonTranslation findById(Long id) {
        return personTranslationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("person translation not found"));
    }

    @Transactional
    public PersonTranslation add(Long personId, String language, String name) {
        if (personId == null) {
            throw new IllegalArgumentException("personId must not be null");
        }
        if (language == null || language.isBlank()) {
            throw new IllegalArgumentException("language must not be blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        return personTranslationRepository.add(personId, language, name);
    }

    @Transactional
    public PersonTranslation modify(Long id, String language, String name) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (language == null || language.isBlank()) {
            throw new IllegalArgumentException("language must not be blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        return personTranslationRepository.modify(id, language, name);
    }

    @Transactional
    public boolean delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return personTranslationRepository.delete(id);
    }
}
