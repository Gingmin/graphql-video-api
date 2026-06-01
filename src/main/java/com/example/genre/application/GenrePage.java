package com.example.genre.application;

import java.util.List;

import com.example.genre.domain.Genre;

public record GenrePage(
    List<Genre> items,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean hasNext,
    boolean hasPrev
) {}
