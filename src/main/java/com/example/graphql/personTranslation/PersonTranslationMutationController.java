package com.example.graphql.personTranslation;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.example.personTranslation.application.PersonTranslationService;

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
        return null;
    }

    @MutationMapping
    public PersonTranslationGql modifyPersonTranslation(
        @Argument("id") String id,
        @Argument("personId") String personId,
        @Argument("language") String language,
        @Argument("name") String name
    ) {
        return null;
    }

    @MutationMapping
    public boolean deletePersonTranslatino(@Argument("id") String id) {
        return false;
    }
}
