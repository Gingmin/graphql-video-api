package com.example.graphql;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class HelloGraphqlController {
    @QueryMapping
    public String hello() {
        return "hello from gingflix";
    }
}

