package com.example.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.user.domain.User;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class JwtServiceTest {
    @Test
    void issue_and_verify_token() {
        // "0123456789abcdef0123456789abcdef" (32 bytes) base64
        var jwtService = new JwtService("MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=", 36000000, 604800000);
        var user = new User(1L, "test", "test@example.com", null, null, Instant.now(), Instant.now());

        var token = jwtService.generateToken(String.valueOf(user.id()));
        var userId = jwtService.getSubject(token);

        assertThat(userId).isEqualTo(String.valueOf(user.id()));
        assertThat(jwtService.isValid(token)).isTrue();
        assertThat(jwtService.isValid(token, String.valueOf(user.id()))).isTrue();
    }
}

