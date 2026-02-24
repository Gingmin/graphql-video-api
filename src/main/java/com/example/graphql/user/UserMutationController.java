package com.example.graphql.user;

import com.example.user.application.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UserMutationController {
    private final UserService userService;

    public UserMutationController(UserService userService) {
        this.userService = userService;
    }

    @MutationMapping
    public UserGql signUp(
        @Argument("name") String name,
        @Argument("email") String email,
        @Argument("password") String password) {
        
        var user = userService.signUp(name, email, password);
        return UserMapper.toGql(user);
    }

    @MutationMapping
    public UserGql login(
        @Argument("email") String email,
        @Argument("password") String password,
        HttpServletResponse response
    ) {
        var result = userService.login(email, password);

        ResponseCookie cookie = ResponseCookie.from("token", result.accessToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return UserMapper.toGql(result.user());
    }

    @MutationMapping
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ZERO)
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        return true;
    }
}