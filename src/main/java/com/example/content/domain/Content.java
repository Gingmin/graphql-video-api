package com.example.content.domain;

import java.time.Instant;
import java.util.UUID;

public record Content(
    UUID id,
    String contentType,
    String title,
    String description,
    String ageRating,
    UUID thumbnailFileId,
    UUID trailerFileId,
    Instant createdAt,
    Instant modifiedAt
) {}
