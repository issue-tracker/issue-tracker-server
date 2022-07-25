package com.ahoo.issuetrackerserver.auth;

import com.ahoo.issuetrackerserver.auth.dto.AuthUserResponse;
import com.ahoo.issuetrackerserver.auth.dto.GithubEmailResponse;
import com.ahoo.issuetrackerserver.member.MemberService;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final WebClient webClient;
    private final MemberService memberService;

    public AccessToken requestAccessToken(AuthProviderType authProvider, String code) {
        MultiValueMap<String, String> accessTokenRequest = authProvider.createAccessTokenRequest(code);

        return webClient.post()
            .uri(authProvider.getRequestAccessTokenUrl())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .accept(MediaType.APPLICATION_JSON)
            .acceptCharset(StandardCharsets.UTF_8)
            .bodyValue(accessTokenRequest)
            .retrieve()
            .bodyToMono(AccessToken.class)
            .block();
    }

    public AuthUserResponse requestAuthUser(AuthProviderType authProvider, AccessToken accessToken) {
        AuthUserResponse authUserResponse = webClient.get()
            .uri(authProvider.getRequestAuthUserUrl())
            .header(HttpHeaders.AUTHORIZATION, accessToken.convertAuthorizationHeader())
            .accept(MediaType.APPLICATION_JSON)
            .acceptCharset(StandardCharsets.UTF_8)
            .retrieve()
            .bodyToMono(AuthUserResponse.class)
            .block();

        if (authProvider == AuthProviderType.GITHUB && authUserResponse.getEmail() == null) {
            String email = webClient.get()
                .uri("https://api.github.com/user/emails")
                .header(HttpHeaders.AUTHORIZATION, accessToken.convertAuthorizationHeader())
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToFlux(GithubEmailResponse.class)
                .filter(GithubEmailResponse::getPrimary)
                .blockFirst()
                .getEmail();
            authUserResponse.setEmail(email);
        }

        memberService.validateDuplicatedEmail(authUserResponse.getEmail());

        return authUserResponse;
    }
}
