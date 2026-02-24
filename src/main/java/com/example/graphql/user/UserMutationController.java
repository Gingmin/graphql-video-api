package com.example.graphql.user;

import com.example.auth.SessionService;
import com.example.user.application.UserService;
import com.example.user.domain.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UserMutationController {
    private final UserService userService;
    private final SessionService sessionService;

    public UserMutationController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @MutationMapping
    public User signUp(
        @Argument("name") String name,
        @Argument("email") String email,
        @Argument("password") String password) {
        
        var user = userService.signUp(name, email, password);
        return UserMapper.toGql(user);
    }

    @MutationMapping
    public User login(
        @Argument("email") String email,
        @Argument("password") String password,
        HttpServletResponse response
    ) {
        User user = userService.login(email, password);

        String sessionId = UUID.randomUUID().toString();
        sessionService.save(sessionId, user);

        Cookie cookie = new Cookie("session_id", sessionId);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 7);
        response.addCookie(cookie);

        return UserMapper.toGql(user);
    }

    @MutationMapping
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = sessionService.extractSessionCookie(request);
        if (sessionId != null) {
            sessionService.delete(sessionId);
        }

        Cookie cookie = new Cookie("session_id", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return true;
    }
}