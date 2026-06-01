package com.example.graphql.file;

import java.util.List;

import com.example.graphql.PageInfoGql;

public record FilePageGql(
    List<FileGql> items,
    PageInfoGql pageInfo
) {}
