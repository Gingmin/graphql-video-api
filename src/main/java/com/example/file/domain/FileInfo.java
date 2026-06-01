package com.example.file.domain;

import java.time.Instant;
import java.util.UUID;

public record FileInfo(
    UUID id,
    String name,
    String originalName,
    String path,
    String extension,
    String mimeType,
    long fileSize,
    Instant createdAt,
    Instant modifiedAt
) {}
