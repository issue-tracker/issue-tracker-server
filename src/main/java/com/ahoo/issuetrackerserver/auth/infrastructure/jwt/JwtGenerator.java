package com.ahoo.issuetrackerserver.auth.infrastructure.jwt;

import static com.ahoo.issuetrackerserver.auth.infrastructure.jwt.JwtConstant.ACCESS_TOKEN_EXPIRED_TIME;
import static com.ahoo.issuetrackerserver.auth.infrastructure.jwt.JwtConstant.CLAIM_NAME;
import static com.ahoo.issuetrackerserver.auth.infrastructure.jwt.JwtConstant.REFRESH_TOKEN_EXPIRED_TIME;
import static com.ahoo.issuetrackerserver.auth.infrastructure.jwt.JwtConstant.SECRET_KEY;

import io.jsonwebtoken.Jwts;
import java.time.Instant;
import java.util.Date;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtGenerator {

    public static AccessToken generateAccessToken(Long memberId) {
        Date now = new Date();
        String accessToken = Jwts.builder()
            .setIssuedAt(now)
            .setExpiration(Date.from(Instant.now().plusSeconds(ACCESS_TOKEN_EXPIRED_TIME)))
            .claim(CLAIM_NAME, memberId)
            .signWith(SECRET_KEY)
            .compact();

        return AccessToken.of(accessToken);
    }

    public static RefreshToken generateRefreshToken(Long memberId) {
        Date now = new Date();
        String refreshToken = Jwts.builder()
            .setIssuedAt(now)
            .setExpiration(Date.from(Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRED_TIME)))
            .claim(CLAIM_NAME, memberId)
            .signWith(SECRET_KEY)
            .compact();

        return RefreshToken.of(refreshToken);
    }
}
