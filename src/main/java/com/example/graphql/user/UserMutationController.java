package com.example.graphql.user;

import com.example.auth.AuthContext;
import com.example.follow.application.FollowService;
import com.example.user.application.UserService;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UserMutationController {
    private final UserService userService;

    public UserMuatationController(UserService userService) {
        this.userService = userService;
    }

    @MutationMapping
    public User signUp(
        @Argument("name") String name,
        @Argument("email") String email,
        @Argument("password") String password) {
        
        var result = userService.signUp(name, email, password);
        return UserMapper.toGql(result.user());
    }
}