package com.ahoo.issuetrackerserver.auth.infrastructure.jwt;

import com.ahoo.issuetrackerserver.common.exception.ErrorType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessToken implements JwtToken {

    private static final String ACCESS_TOKEN = "access_token";
    private static final String COOKIE_PATH = "/";

    private String accessToken;

    @Override
    public String getToken() {
        return this.accessToken;
    }

    @Override
    public ResponseCookie toCookie() {
        return ResponseCookie.from(ACCESS_TOKEN, accessToken)
            .maxAge(JwtConstant.ACCESS_TOKEN_EXPIRED_TIME)
            .path(COOKIE_PATH)
            .secure(true)
            .httpOnly(true)
            .sameSite("None")
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
