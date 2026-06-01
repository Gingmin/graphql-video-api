package com.example.graphql.file;

public record FileGql(
    String id,
    String name,
    String originalName,
    String path,
    String extension,
    String mimeType,
    String fileSize,
    String createdAt,
    String modifiedAt
) {}
