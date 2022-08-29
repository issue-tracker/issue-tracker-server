package com.ahoo.issuetrackerserver.auth.infrastructure.jwt;

import javax.servlet.http.Cookie;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

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
    public Cookie toCookie() {
        Cookie cookie = new Cookie(REFRESH_TOKEN, this.refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge((int) JwtConstant.REFRESH_TOKEN_EXPIRED_TIME);
        return cookie;
    }

    public static RefreshToken of(String refreshToken) {
        return new RefreshToken(refreshToken);
    }
}
