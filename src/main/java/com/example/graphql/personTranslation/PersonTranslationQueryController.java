package com.example.graphql.personTranslation;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.example.graphql.person.PersonGql;
import com.example.person.application.PersonTranslationService;

import java.util.List;

@Controller
public class PersonTranslationQueryController {
    private final PersonTranslationService personTranslationService;

    public PersonTranslationQueryController(PersonTranslationService personTranslationService) {
        this.personTranslationService = personTranslationService;
    }

    @QueryMapping
    public List<PersonTranslationGql> personTranslations(@Argument("personId") String personId) {
        return personTranslationService.findByPersonId(Long.parseLong(personId)).stream()
            .map(PersonTranslationMapper::toGql)
            .toList();
    }

    @QueryMapping
    public PersonTranslationGql personTranslation(@Argument("id") String id) {
        return PersonTranslationMapper.toGql(
            personTranslationService.findById(Long.parseLong(id))
        );
    }

    @SchemaMapping(typeName = "Person", field = "translations")
    public List<PersonTranslationGql> translations(PersonGql person) {
        return personTranslationService.findByPersonId(Long.parseLong(person.id())).stream()
            .map(PersonTranslationMapper::toGql)
            .toList();
    }
}
