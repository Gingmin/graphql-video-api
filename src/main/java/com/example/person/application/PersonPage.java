package com.example.person.application;

import java.util.List;

import com.example.person.domain.Person;

public record PersonPage(
    List<Person> items,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean hasNext,
    boolean hasPrev
) {}
