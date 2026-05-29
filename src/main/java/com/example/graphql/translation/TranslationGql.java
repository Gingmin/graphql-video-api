package com.example.graphql.translation;

public record TranslationGql(
    String id,
    String targetId,
    String language,
    String name,
    String createdAt,
    String modifiedAt
) {}
