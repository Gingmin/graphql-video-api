package com.example.file.application;

import java.util.List;

import com.example.file.domain.FileInfo;

public record FilePage(
    List<FileInfo> items,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean hasNext,
    boolean hasPrev
) {}
