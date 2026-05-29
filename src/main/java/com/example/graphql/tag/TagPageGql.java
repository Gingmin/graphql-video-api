package com.example.graphql.tag;

import java.util.List;

import com.example.graphql.PageInfoGql;

public record TagPageGql(
    List<TagGql> items,
    PageInfoGql pageInfo
) {}
