package com.example.tag.application;

import java.util.List;

import com.example.tag.domain.Tag;

public record TagPage(
    List<Tag> items,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean hasNext,
    boolean hasPrev
) {}
