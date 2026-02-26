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
    public static final String REMOTE_IP_CTX_KEY = "REMOTE_IP";

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        String ip = resolveIp(request);
        request.configureExecutionInput((input, builder) -> {
            return builder.graphQLContext(ctx -> ctx.put(REMOTE_IP_CTX_KEY, ip)).build();
        });

        return chain.next(request).doOnNext(response -> {
            Object v = response.getExecutionInput().getGraphQLContext().get(SET_COOKIE_CTX_KEY);
            if (v instanceof String cookie && !cookie.isBlank()) {
                response.getResponseHeaders().add(HttpHeaders.SET_COOKIE, cookie);
            }
        });
    }

    private String resolveIp(WebGraphQlRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("X-Forwarded-For");
        if (ip != null && !ip.isBlank()) {
            return ip.split(",")[0].trim();
        }
        ip = headers.getFirst("X-Real-IP");
        if (ip != null && !ip.isBlank()) {
            return ip.trim();
        }
        return request.getUri().getHost();
    }
}
