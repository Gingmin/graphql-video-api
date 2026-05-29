package com.example.person.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record Person(UUID id, String code, LocalDate birthDate, String nationality, Instant createdAt, Instant modifiedAt) {}
