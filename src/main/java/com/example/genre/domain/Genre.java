package com.example.genre.domain;

import java.time.Instant;
import java.util.UUID;

public record Genre(
    UUID id,
    String code,
    Instant createdAt,
    Instant modifiedAt
) {}
