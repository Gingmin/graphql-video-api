package com.example.user.application;

import java.util.List;

import com.example.user.domain.User;

public record UserPage(
    List<User> items,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean hasNext,
    boolean hasPrev
) {}
