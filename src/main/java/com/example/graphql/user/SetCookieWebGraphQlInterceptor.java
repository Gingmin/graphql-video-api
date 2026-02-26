package com.example.graphql.user;

import org.springframework.stereotype.Component;

import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Component
public class SetCookieWebGraphQlInterceptor implements WebGraphQlInterceptor {
    public static final String SET_COOKIE_CTX_KEY = "SET_COOKIE";

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        return chain.next(request).doOnNext(response -> {
            Object v = response.getExecutionInput().getGraphQLContext().get(SET_COOKIE_CTX_KEY);
            if (v instanceof String cookie && !cookie.isBlank()) {
                response.getResponseHeaders().add(HttpHeaders.SET_COOKIE, cookie);
            }
        });
    }
}
