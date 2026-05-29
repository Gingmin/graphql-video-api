package com.example.user.domain;

import java.time.Instant;
import java.util.UUID;

public record User(UUID id, String name, String email, String latestLoginIp, Instant lastLoginDate, Instant createdAt, Instant modifiedAt) {}
