package com.ahoo.issuetrackerserver.common.argumentresolver;

import com.ahoo.issuetrackerserver.auth.application.JwtService;
import com.ahoo.issuetrackerserver.auth.infrastructure.jwt.AccessToken;
import com.ahoo.issuetrackerserver.common.exception.UnAuthorizedException;
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

        String authorizationHeader = webRequest.getHeader("Authorization");
        if (authorizationHeader == null) {
            throw new UnAuthorizedException("요청에 Authorization 헤더가 존재하지 않습니다.");
        }

        AccessToken accessToken = AccessToken.headerToAccessToken(authorizationHeader);
        jwtService.validateToken(accessToken);

        return jwtService.extractMemberId(accessToken);
    }
}
