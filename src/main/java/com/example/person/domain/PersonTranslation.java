package com.example.person.domain;

import java.time.Instant;

public record PersonTranslation(
    Long id,
    Long personId,
    String language,
    String name,
    Instant createdAt,
    Instant modifiedAt
) {}
