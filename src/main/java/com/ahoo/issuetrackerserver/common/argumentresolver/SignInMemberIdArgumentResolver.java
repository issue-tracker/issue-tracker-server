package com.ahoo.issuetrackerserver.common.argumentresolver;

import com.ahoo.issuetrackerserver.auth.application.JwtService;
import com.ahoo.issuetrackerserver.auth.infrastructure.jwt.AccessToken;
import com.ahoo.issuetrackerserver.common.exception.ErrorType;
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

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private final JwtService jwtService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SignInMemberId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String authorizationHeader = webRequest.getHeader(AUTHORIZATION_HEADER_NAME);
        if (authorizationHeader == null) {
            throw new UnAuthorizedException(ErrorType.NO_AUTHORIZATION_HEADER);
        }

        AccessToken accessToken = AccessToken.headerToAccessToken(authorizationHeader);
        jwtService.validateToken(accessToken);

        return jwtService.extractMemberId(accessToken);
    }
}
