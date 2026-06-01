package com.example.graphql.genre;

import java.util.List;

import com.example.graphql.PageInfoGql;

public record GenrePageGql(
    List<GenreGql> items,
    PageInfoGql pageInfo
) {}