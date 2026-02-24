package com.example.graphql.user;

import com.example.user.domain.User;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;

import com.example.user.application.UserService;

@Controller
public class UserQueryController {
    private final UserService userService;

    public UserQueryController(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping
    public UserGql me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return UserMapper.toGql(user);
        }

        // principal이 subject(String)로 들어온 경우를 대비
        if (principal instanceof String subject && subject != null && !subject.isBlank()) {
            User user = userService.getById(subject);
            return UserMapper.toGql(user);
        }

        return null;
    }
}