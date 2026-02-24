package com.example.user.domain;

import java.time.Instant;

public record User(Long id, String name, String email, String latestLoginIp, Instant lastLoginDate, Instant createdAt, Instant modifiedAt) {}