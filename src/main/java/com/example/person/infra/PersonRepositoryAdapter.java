package com.example.person.infra;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.data.domain.PageRequest;

import com.example.person.application.PersonRepository;
import com.example.person.domain.Person;
import com.example.person.infra.jpa.PersonJpaEntity;
import com.example.person.infra.jpa.PersonJpaRepository;
import com.example.person.application.PersonPage;

@Repository
public class PersonRepositoryAdapter implements PersonRepository {
    private final PersonJpaRepository jpaRepository;

    public PersonRepositoryAdapter(PersonJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    private static Person toDomain(PersonJpaEntity e) {
        return new Person(
            e.getId(),
            e.getCode(),
            e.getBirthDate(),
            e.getNationality(),
            e.getCreatedAt(),
            e.getModifiedAt()
        );
    }

    @Override
    public PersonPage findPage(Integer page, Integer size) {
        var pageable = PageRequest.of(page - 1, size);
        var pageResult = jpaRepository.findPage(pageable);
        var persons = pageResult.getContent();

        if (persons.isEmpty()) {
            return new PersonPage(
                List.of(),
                pageResult.getNumber() + 1,
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.hasNext(),
                pageResult.hasPrevious()
            );
        }

        var items = persons.stream().map(p -> toDomain(p)).toList();

        return new PersonPage(
            items,
            pageResult.getNumber() + 1,
            pageResult.getSize(),
            pageResult.getTotalElements(),
            pageResult.getTotalPages(),
            pageResult.hasNext(),
            pageResult.hasPrevious()
        );
    }

    @Override
    public Optional<Person> findById(Long id) {
        return jpaRepository.findById(id).map(PersonRepositoryAdapter::toDomain);
    }

    @Override
    public Person addPerson(String code, LocalDate birthDate, String nationality) {
        var saved = jpaRepository.save(new PersonJpaEntity(code, birthDate, nationality));
        return toDomain(saved);
    }

    @Override
    public Person modifyPerson(Long id, String code, LocalDate birthDate, String nationality) {
        var entity = jpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("person not found"));

        entity.setCode(code);
        entity.setBirthDate(birthDate);
        entity.setNationality(nationality);
        var saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public boolean deletePerson(Long id) {
        jpaRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }
}
