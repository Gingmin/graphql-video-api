package com.example.content.application;

import java.util.List;

import com.example.content.domain.Content;

public record ContentPage(
    List<Content> items,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean hasNext,
    boolean hasPrev
) {}
