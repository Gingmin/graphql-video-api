package com.example.graphql.user;

import com.example.auth.SessionService;
import com.example.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UserQueryController {
    private final SessionService sessionService;

    public UserQueryController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @QueryMapping
    public UserGql me(HttpServletRequest request) {
        String sessionId = sessionService.extractSessionCookie(request);
        if (sessionId == null) {
            return null;
        }
        User user = sessionService.get(sessionId);
        if (user == null) {
            return null;
        }
        return UserMapper.toGql(user);
    }
}