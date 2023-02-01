package com.ahoo.issuetrackerserver.auth.infrastructure.jwt;

import com.ahoo.issuetrackerserver.common.exception.ErrorType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.http.ResponseCookie;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24)
public class RefreshToken implements JwtToken {

    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String COOKIE_PATH = "/";

    @Id
    private String refreshToken;

    @Override
    public String getToken() {
        return refreshToken;
    }

    @Override
    public ResponseCookie toCookie() {
        return ResponseCookie.from(REFRESH_TOKEN, this.refreshToken)
            .maxAge(JwtConstant.REFRESH_TOKEN_EXPIRED_TIME)
            .path(COOKIE_PATH)
            .secure(true)
            .httpOnly(true)
            .sameSite("None")
            .build();
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.INVALID_REFRESH_TOKEN;
    }

    public static RefreshToken of(String refreshToken) {
        return new RefreshToken(refreshToken);
    }
}
