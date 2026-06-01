package com.example.graphql.user;

import com.example.graphql.GqlDateTimeFormat;
import com.example.user.domain.User;

final class UserMapper {
    private UserMapper() {}

    static UserGql toGql(User user) {
        return new UserGql(
            String.valueOf(user.id()),
            user.name(),
            user.email(),
            user.latestLoginIp(),
            GqlDateTimeFormat.formatOrNull(user.lastLoginDate()),
            GqlDateTimeFormat.formatOrNull(user.createdAt()),
            GqlDateTimeFormat.formatOrNull(user.modifiedAt())
        );
    }
}