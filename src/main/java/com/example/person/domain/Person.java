package com.example.person.domain;

import java.time.Instant;
import java.time.LocalDate;

public record Person(Long id, String code, LocalDate birthDate, String nationality, Instant createdAt, Instant modifiedAt) {}
