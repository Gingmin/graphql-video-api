package com.example.graphql.translation;

import com.example.graphql.GqlDateTimeFormat;
import com.example.translation.domain.Translation;

public class TranslationMapper {

    private TranslationMapper() {}

    static TranslationGql toGql(Translation t) {
        return new TranslationGql(
            String.valueOf(t.id()),
            String.valueOf(t.targetId()),
            t.language(),
            t.name(),
            GqlDateTimeFormat.formatOrNull(t.createdAt()),
            GqlDateTimeFormat.formatOrNull(t.modifiedAt())
        );
    }
}
