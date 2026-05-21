package com.example.graphql.personTranslation;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.example.person.application.PersonTranslationService;

@Controller
public class PersonTranslationMutationController {
    private final PersonTranslationService personTranslationService;

    public PersonTranslationMutationController(PersonTranslationService personTranslationService) {
        this.personTranslationService = personTranslationService;
    }

    @MutationMapping
    public PersonTranslationGql addPersonTranslation(
        @Argument("personId") String personId,
        @Argument("language") String language,
        @Argument("name") String name
    ) {
        var pt = personTranslationService.add(Long.parseLong(personId), language, name);
        return PersonTranslationMapper.toGql(pt);
    }

    @MutationMapping
    public PersonTranslationGql modifyPersonTranslation(
        @Argument("id") String id,
        @Argument("language") String language,
        @Argument("name") String name
    ) {
        var pt = personTranslationService.modify(Long.parseLong(id), language, name);
        return PersonTranslationMapper.toGql(pt);
    }

    @MutationMapping
    public boolean deletePersonTranslation(@Argument("id") String id) {
        return personTranslationService.delete(Long.parseLong(id));
    }
}
