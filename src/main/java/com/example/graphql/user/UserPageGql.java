package com.example.graphql.user;

import java.util.List;

import com.example.graphql.PageInfoGql;

public record UserPageGql(
    List<UserGql> items,
    PageInfoGql pageInfo
) {}
