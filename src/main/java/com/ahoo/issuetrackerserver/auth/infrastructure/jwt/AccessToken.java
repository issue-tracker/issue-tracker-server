package com.ahoo.issuetrackerserver.auth.infrastructure.jwt;

import com.ahoo.issuetrackerserver.common.exception.ErrorType;
import javax.servlet.http.Cookie;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
        cookie.setPath("/");
        return cookie;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.INVALID_ACCESS_TOKEN;
    }

    public static AccessToken of(String accessToken) {
        return new AccessToken(accessToken);
    }

    public static AccessToken headerToAccessToken(String accessTokenHeader) {
        return new AccessToken(accessTokenHeader.split(" ")[1]);
    }
}
