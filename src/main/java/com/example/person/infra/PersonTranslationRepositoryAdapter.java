package com.example.person.infra;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.example.person.application.PersonTranslationRepository;
import com.example.person.domain.PersonTranslation;
import com.example.person.infra.jpa.PersonTranslationJpaEntity;
import com.example.person.infra.jpa.PersonTranslationJpaRepository;

@Repository
public class PersonTranslationRepositoryAdapter implements PersonTranslationRepository {
    private final PersonTranslationJpaRepository jpaRepository;

    public PersonTranslationRepositoryAdapter(PersonTranslationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    private static PersonTranslation toDomain(PersonTranslationJpaEntity e) {
        return new PersonTranslation(
            e.getId(),
            e.getPersonId(),
            e.getLanguage(),
            e.getName(),
            e.getCreatedAt(),
            e.getModifiedAt()
        );
    }

    @Override
    public List<PersonTranslation> findByPersonId(Long personId) {
        return jpaRepository.findByPersonId(personId).stream()
            .map(PersonTranslationRepositoryAdapter::toDomain)
            .toList();
    }

    @Override
    public Optional<PersonTranslation> findById(Long id) {
        return jpaRepository.findById(id).map(PersonTranslationRepositoryAdapter::toDomain);
    }

    @Override
    public PersonTranslation add(Long personId, String language, String name) {
        var entity = new PersonTranslationJpaEntity(personId, language, name);
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public PersonTranslation modify(Long id, String language, String name) {
        var entity = jpaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("person translation not found"));
        entity.setLanguage(language);
        entity.setName(name);
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public boolean delete(Long id) {
        jpaRepository.deleteById(id);
        return true;
    }
}
