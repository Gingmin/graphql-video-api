package com.example.graphql.personTranslation;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.example.person.domain.PersonTranslation;

public class PersonTranslationMapper {

    private PersonTranslationMapper() {}

    private static final DateTimeFormatter ISO_INSTANT =
        DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC);

    static PersonTranslationGql toGql(PersonTranslation pt) {
        return new PersonTranslationGql(
            String.valueOf(pt.id()),
            String.valueOf(pt.personId()),
            pt.language(),
            pt.name(),
            pt.createdAt() == null ? null : ISO_INSTANT.format(pt.createdAt()),
            pt.modifiedAt() == null ? null : ISO_INSTANT.format(pt.modifiedAt())
        );
    }
}
