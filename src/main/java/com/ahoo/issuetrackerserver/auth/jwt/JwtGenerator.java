package com.ahoo.issuetrackerserver.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtGenerator {

    private static final String CLAIM_NAME = "memberId";
    private static final long ACCESS_TOKEN_EXPIRED_TIME = Duration.ofMinutes(30).toSeconds();
    private static final long REFRESH_TOKEN_EXPIRED_TIME = Duration.ofDays(1).toSeconds();
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(System.getenv("JWT_SECRET_KEY").getBytes());

    public static String generateAccessToken(Long memberId) {
        Date now = new Date();
        return Jwts.builder()
            .setIssuedAt(now)
            .setExpiration(Date.from(Instant.now().plusSeconds(ACCESS_TOKEN_EXPIRED_TIME)))
            .claim(CLAIM_NAME, memberId)
            .signWith(SECRET_KEY)
            .compact();
    }

    public static String generateRefreshToken(Long memberId) {
        Date now = new Date();
        return Jwts.builder()
            .setIssuedAt(now)
            .setExpiration(Date.from(Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRED_TIME)))
            .claim(CLAIM_NAME, memberId)
            .signWith(SECRET_KEY)
            .compact();
    }
}
