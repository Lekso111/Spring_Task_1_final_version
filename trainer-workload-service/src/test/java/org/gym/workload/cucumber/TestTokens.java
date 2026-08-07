package org.gym.workload.cucumber;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

final class TestTokens {

    static final String SECRET = "integration-and-component-test-secret-key-that-is-long-enough";

    private TestTokens() {
    }

    static String tokenFor(String username) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        Date now = new Date();
        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + 3_600_000L))
                .signWith(key)
                .compact();
    }
}
