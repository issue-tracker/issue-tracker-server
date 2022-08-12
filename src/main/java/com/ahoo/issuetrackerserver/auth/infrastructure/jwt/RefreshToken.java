package com.ahoo.issuetrackerserver.auth.infrastructure.jwt;

import javax.servlet.http.Cookie;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash(value = "refreshToken", timeToLive = 60)
public class RefreshToken implements JwtToken {

    @Id
    private String refreshToken;

    @Override
    public String getToken() {
        return refreshToken;
    }

    @Override
    public Cookie toCookie() {
        Cookie cookie = new Cookie("refresh_token", this.refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);
        return cookie;
    }

    public static RefreshToken of(String refreshToken) {
        return new RefreshToken(refreshToken);
    }
}