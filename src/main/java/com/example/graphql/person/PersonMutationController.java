package com.example.graphql.person;

import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.example.person.application.PersonService;

import org.springframework.graphql.data.method.annotation.Argument;

import java.time.LocalDate;
import java.util.UUID;

@Controller
public class PersonMutationController {
    private final PersonService personService;

    public PersonMutationController(PersonService personService) {
        this.personService = personService;
    }

    @MutationMapping
    public PersonGql addPerson(
        @Argument("code") String code,
        @Argument("birthDate") String birthDate,
        @Argument("nationality") String nationality) {
        
        LocalDate birthDateParsed = birthDate == null ? null : LocalDate.parse(birthDate);
        var person = personService.addPerson(code, birthDateParsed, nationality);
        return PersonMapper.toGql(person);
    }

    @MutationMapping
    public PersonGql modifyPerson(
        @Argument("id") String id,
        @Argument("code") String code,
        @Argument("birthDate") String birthDate,
        @Argument("nationality") String nationality) {
            LocalDate birthDateParsed = birthDate == null ? null : LocalDate.parse(birthDate);
            var person = personService.modifyPerson(UUID.fromString(id), code, birthDateParsed, nationality);
            return PersonMapper.toGql(person);
    }

    @MutationMapping
    public boolean deletePerson(@Argument("id") String id) {
        return personService.deletePerson(UUID.fromString(id));
    }
}
