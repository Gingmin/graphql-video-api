package com.example.graphql.person;

import java.util.List;

import com.example.graphql.PageInfoGql;

public record PersonPageGql(
    List<PersonGql> items,
    PageInfoGql pageInfo
) {}