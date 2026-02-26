package com.example.graphql.user;

import graphql.schema.DataFetchingEnvironment;
import com.example.user.application.UserService;
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
        DataFetchingEnvironment env
    ) {
        String clientIp = env.getGraphQlContext().get(SetCookieWebGraphQlInterceptor.REMOTE_IP_CTX_KEY);
        var result = userService.login(email, password, clientIp);

        ResponseCookie cookie = ResponseCookie.from("token", result.accessToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Lax")
                .build();
        env.getGraphQlContext().put(SetCookieWebGraphQlInterceptor.SET_COOKIE_CTX_KEY, cookie.toString());
        return UserMapper.toGql(result.user());
    }

    @MutationMapping
    public boolean logout(DataFetchingEnvironment env) {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ZERO)
                .sameSite("Lax")
                .build();
        env.getGraphQlContext().put(SetCookieWebGraphQlInterceptor.SET_COOKIE_CTX_KEY, cookie.toString());
        return true;
    }
}