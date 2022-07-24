package com.ahoo.issuetrackerserver.auth;

import com.ahoo.issuetrackerserver.auth.dto.AccessTokenRequest;
import com.ahoo.issuetrackerserver.auth.dto.AuthUserResponse;
import com.ahoo.issuetrackerserver.auth.dto.GithubEmailResponse;
import com.ahoo.issuetrackerserver.member.MemberService;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final WebClient webClient;
    private final MemberService memberService;

    public AccessToken requestAccessToken(String code) {
        AccessTokenRequest accessTokenRequest = new AccessTokenRequest(
            System.getenv("GITHUB_CLIENT_ID"),
            System.getenv("GITHUB_CLIENT_SECRET"),
            code);

        return webClient.post()
            .uri("https://github.com/login/oauth/access_token")
            .accept(MediaType.APPLICATION_JSON)
            .acceptCharset(StandardCharsets.UTF_8)
            .bodyValue(accessTokenRequest)
            .retrieve()
            .bodyToMono(AccessToken.class)
            .block();
    }

    public AuthUserResponse requestAuthUser(AccessToken accessToken) {
        AuthUserResponse authUserResponse = webClient.get()
            .uri("https://api.github.com/user")
            .header(HttpHeaders.AUTHORIZATION, accessToken.convertAuthorizationHeader())
            .accept(MediaType.APPLICATION_JSON)
            .acceptCharset(StandardCharsets.UTF_8)
            .retrieve()
            .bodyToMono(AuthUserResponse.class)
            .block();

        if (authUserResponse.getEmail() == null) {
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
