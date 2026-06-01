package com.example.graphql.person;

import com.example.graphql.GqlDateTimeFormat;
import com.example.person.domain.Person;

public class PersonMapper {
    
    private PersonMapper() {}

    static PersonGql toGql(Person person) {
        return new PersonGql(
            String.valueOf(person.id()),
            person.code(),
            person.birthDate() == null ? null : person.birthDate().toString(),
            person.nationality(),
            GqlDateTimeFormat.formatOrNull(person.createdAt()),
            GqlDateTimeFormat.formatOrNull(person.modifiedAt())
        );
    }
}
