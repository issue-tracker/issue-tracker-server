package com.ahoo.issuetrackerserver.auth;

import javax.servlet.http.Cookie;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AccessToken implements JwtToken {

    private String accessToken;

    @Override
    public String getToken() {
        return this.accessToken;
    }

    @Override
    public Cookie toCookie() {
        Cookie cookie = new Cookie("access_token", this.accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/api");
        return cookie;
    }
}
