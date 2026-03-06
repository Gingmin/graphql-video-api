package com.example.graphql.personTranslation;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.Argument;

import com.example.personTranslation.application.PersonTranslationService;
import com.example.graphql.PageInfoGql;

@Controller
public class PersonTranslationQueryController {
    private final PersonTranslationService personTranslastionService;

    public PersonTranslationQueryController(PersonTranslationService personTranslationService) {
        this.personTranslastionService = personTranslationService;
    }

    @QueryMapping
    public PersonTranslationPageGql personTranslations(@Argument("page") Integer page, @Argument("size") Integer size) {
        return null;
    }
}
