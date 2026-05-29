package com.example.graphql.user;

import com.example.graphql.PageInfoGql;
import com.example.user.domain.User;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;

import com.example.user.application.UserService;

@Controller
public class UserQueryController {
    private final UserService userService;

    public UserQueryController(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping
    public UserPageGql users(@Argument("page") Integer page, @Argument("size") Integer size) {
        var result = userService.users(page, size);

        int totalElements = result.totalElements() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result.totalElements();

        return new UserPageGql(
            result.items().stream().map(UserMapper::toGql).toList(),
            new PageInfoGql(
                result.page(),
                result.size(),
                totalElements,
                result.totalPages(),
                result.hasNext(),
                result.hasPrev()
            )
        );
    }

    @QueryMapping
    public UserGql me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return UserMapper.toGql(user);
        }

        if (principal instanceof String subject && !"anonymousUser".equals(subject) && !subject.isBlank()) {
            User user = userService.getById(subject);
            return UserMapper.toGql(user);
        }

        return null;
    }
}