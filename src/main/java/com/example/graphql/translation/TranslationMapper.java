package com.example.graphql.translation;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.example.translation.domain.Translation;

public class TranslationMapper {

    private TranslationMapper() {}

    private static final DateTimeFormatter ISO_INSTANT =
        DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC);

    static TranslationGql toGql(Translation t) {
        return new TranslationGql(
            String.valueOf(t.id()),
            String.valueOf(t.targetId()),
            t.language(),
            t.name(),
            t.createdAt() == null ? null : ISO_INSTANT.format(t.createdAt()),
            t.modifiedAt() == null ? null : ISO_INSTANT.format(t.modifiedAt())
        );
    }
}
