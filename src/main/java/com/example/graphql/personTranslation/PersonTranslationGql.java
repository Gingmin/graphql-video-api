package com.example.graphql.personTranslation;

public record PersonTranslationGql(
    String id,
    String personId,
    String language,
    String name,
    String createdAt,
    String modifiedAt
) {}
