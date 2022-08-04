package com.ahoo.issuetrackerserver.auth;

import javax.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@NoArgsConstructor
@AllArgsConstructor
@RedisHash(timeToLive = 60)
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
        return cookie;
    }
}
