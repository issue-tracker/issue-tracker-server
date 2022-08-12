package com.ahoo.issuetrackerserver.auth.infrastructure.jwt;

import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import javax.crypto.SecretKey;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtConstant {

    public static final String CLAIM_NAME = "memberId";
    public static final long ACCESS_TOKEN_EXPIRED_TIME = Duration.ofMinutes(30).toSeconds();
    public static final long REFRESH_TOKEN_EXPIRED_TIME = Duration.ofDays(1).toSeconds();
    public static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(System.getenv("JWT_SECRET_KEY").getBytes());
}
