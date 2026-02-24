package com.example.graphql.user;

public record UserGql(
    String id,
    String name,
    String email,
    String latestLoginIp,
    String lastLoginDate,
    String createdAt,
    String modifiedAt
) {}