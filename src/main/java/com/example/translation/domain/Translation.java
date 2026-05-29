package com.example.translation.domain;

import java.time.Instant;
import java.util.UUID;

public record Translation(UUID id, UUID targetId, String language, String name, Instant createdAt, Instant modifiedAt) {}
