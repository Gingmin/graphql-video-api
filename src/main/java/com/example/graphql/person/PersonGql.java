package com.example.graphql.person;

public record PersonGql(
    String id,
    String code,
    String birthDate,
    String nationality,
    String createdAt,
    String modifiedAt
) {}
