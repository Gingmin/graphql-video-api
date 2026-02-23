package com.example.user.domain;

import java.time.Instant;

public record User(Long id, String name, String email, String lastLoginIp, Instant lastLoginDate, Instant createdAt, Instant modifiedAt) {}