package com.example.tag.domain;

import java.time.Instant;
import java.util.UUID;

public record Tag(UUID id, String code, Instant createdAt, Instant modifiedAt) {
    
}
