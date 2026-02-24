package com.example.auth;

import com.example.user.domain.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SessionService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final Duration TTL = Duration.ofDays(7);

    public SessionService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String sessionId, User user) {
        redisTemplate.opsForValue().set("session:" + sessionId, user, TTL);
    }

    public User get(String sessionId) {
        return (User) redisTemplate.opsForValue().get("session:" + sessionId);
    }

    public void delete(String sessionId) {
        redisTemplate.delete("session:" + sessionId);
    }

    public String extractSessionCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if ("session".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}