package com.example.person.application;

import java.time.LocalDate;
import java.util.Optional;

import com.example.person.domain.Person;

public interface PersonRepository {
    PersonPage findPage(Integer page, Integer size);

    Optional<Person> findById(Long id);

    Person addPerson(String code, LocalDate birthDate, String nationality);

    Person modifyPerson(Long id, String code, LocalDate birthDate, String nationality);

    boolean deletePerson(Long id);

    boolean existsByCode(String code);
}
