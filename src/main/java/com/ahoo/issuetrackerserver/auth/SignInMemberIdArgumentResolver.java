package com.ahoo.issuetrackerserver.auth;

import com.ahoo.issuetrackerserver.exception.UnAuthorizedException;
import java.util.Arrays;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class SignInMemberIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtService jwtService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SignInMemberId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest nativeRequest = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = nativeRequest.getCookies();

        if (cookies == null) {
            throw new UnAuthorizedException("요청에 access_token 쿠키가 존재하지 않습니다.");
        }
        AccessToken accessToken = Arrays.stream(cookies)
            .filter(c -> c.getName().equals("access_token"))
            .map(c -> new AccessToken(c.getValue()))
            .findFirst()
            .orElseThrow(() -> new UnAuthorizedException("요청에 access_token 쿠키가 존재하지 않습니다."));
        jwtService.validateToken(accessToken);

        return jwtService.extractMemberId(accessToken);
    }
}
