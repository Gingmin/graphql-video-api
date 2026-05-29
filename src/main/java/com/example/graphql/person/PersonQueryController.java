package com.example.graphql.person;

import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import com.example.graphql.PageInfoGql;
import com.example.person.application.PersonService;

import java.util.UUID;

@Controller
public class PersonQueryController {
    private final PersonService personService;

    public PersonQueryController(PersonService personService) {
        this.personService = personService;
    }

    @QueryMapping
    public PersonPageGql persons(@Argument("page") Integer page, @Argument("size") Integer size) {
        var result = personService.persons(page, size);

        int totalElements = result.totalElements() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result.totalElements();

        return new PersonPageGql(
            result.items().stream().map(PersonMapper::toGql).toList(),
            new PageInfoGql(
                result.page(),
                result.size(),
                totalElements,
                result.totalPages(),
                result.hasNext(),
                result.hasPrev()
            )
        );
    }

    @QueryMapping
    public PersonGql person(@Argument("id") String id) {
        return PersonMapper.toGql(personService.person(UUID.fromString(id)));
    }
}
