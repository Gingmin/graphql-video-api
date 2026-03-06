package com.example.graphql;

public record PageInfoGql (
    int page,
    int size,
    int totalElements,
    int totalPages,
    boolean hasNext,
    boolean hasPrev
){}
