package com.ahoo.issuetrackerserver.auth.jwt;

import com.ahoo.issuetrackerserver.auth.AccessToken;
import com.ahoo.issuetrackerserver.auth.RefreshToken;
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
    public static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(System.getenv("JWT_SECRET_KEY").getBytes());

    public static AccessToken generateAccessToken(Long memberId) {
        Date now = new Date();
        String accessToken = Jwts.builder()
            .setIssuedAt(now)
            .setExpiration(Date.from(Instant.now().plusSeconds(ACCESS_TOKEN_EXPIRED_TIME)))
            .claim(CLAIM_NAME, memberId)
            .signWith(SECRET_KEY)
            .compact();

        return new AccessToken(accessToken);
    }

    public static RefreshToken generateRefreshToken(Long memberId) {
        Date now = new Date();
        String refreshToken = Jwts.builder()
            .setIssuedAt(now)
            .setExpiration(Date.from(Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRED_TIME)))
            .claim(CLAIM_NAME, memberId)
            .signWith(SECRET_KEY)
            .compact();

        return new RefreshToken(refreshToken);
    }
}
