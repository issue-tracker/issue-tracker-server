package com.ahoo.issuetrackerserver.auth.infrastructure.jwt;

import com.ahoo.issuetrackerserver.common.exception.ErrorType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessToken implements JwtToken {

    private String accessToken;

    @Override
    public String getToken() {
        return this.accessToken;
    }

    @Override
    public ResponseCookie toCookie() {
//        Cookie cookie = new Cookie("access_token", this.accessToken);
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//        return cookie;
        return ResponseCookie.from("access_token", this.accessToken)
            .httpOnly(true)
            .path("/")
            .build();
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
