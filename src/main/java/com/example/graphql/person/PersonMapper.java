package com.example.graphql.person;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.example.person.domain.Person;

public class PersonMapper {
    
    private PersonMapper() {}

    private static final DateTimeFormatter ISO_INSTANT =
        DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC);

    static PersonGql toGql(Person person) {
        return new PersonGql(
            String.valueOf(person.id()),
            person.code(),
            person.birthDate() == null ? null : person.birthDate().toString(),
            person.nationality(),
            person.createdAt() == null ? null : ISO_INSTANT.format(person.createdAt()),
            person.modifiedAt() == null ? null : ISO_INSTANT.format(person.modifiedAt())
        );
    }
}
