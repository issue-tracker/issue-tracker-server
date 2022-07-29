package com.ahoo.issuetrackerserver.auth;

import com.ahoo.issuetrackerserver.exception.UnAuthorizedException;
import io.jsonwebtoken.JwtException;
import java.util.Arrays;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class SignInAspect {

    private final JwtService jwtService;
    private final HttpServletRequest httpServletRequest;

    @Before("@annotation(com.ahoo.issuetrackerserver.auth.SignInRequired)")
    public void validateAccessToken() {
        try {
            AccessToken accessToken = extractAccessToken().orElseThrow(
                () -> new UnAuthorizedException("요청에 access_token 쿠키가 존재하지 않습니다."));
            jwtService.validateAccessToken(accessToken);
        } catch (JwtException e) {
            throw new UnAuthorizedException(e.getMessage(), e);
        }
    }

    private Optional<AccessToken> extractAccessToken() {
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
            .filter(c -> c.getName().equals("access_token"))
            .map(c -> new AccessToken(c.getValue()))
            .findFirst();
    }
}
