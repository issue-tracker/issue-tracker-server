package com.ahoo.issuetrackerserver.auth;

import com.ahoo.issuetrackerserver.auth.dto.AuthResponse;
import com.ahoo.issuetrackerserver.auth.dto.AuthUserResponse;
import com.ahoo.issuetrackerserver.auth.dto.GithubEmailResponse;
import com.ahoo.issuetrackerserver.exception.EssentialFieldDisagreeException;
import com.ahoo.issuetrackerserver.member.Member;
import com.ahoo.issuetrackerserver.member.MemberService;
import com.ahoo.issuetrackerserver.member.dto.MemberResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
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

    public AccessToken requestAccessToken(AuthProvider authProvider, String code) {
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

    public AuthResponse requestAuthUser(AuthProvider authProvider, AccessToken accessToken) {
        JSONObject jsonResponse = new JSONObject(webClient.get()
            .uri(authProvider.getRequestAuthUserUrl())
            .header(HttpHeaders.AUTHORIZATION, accessToken.convertAuthorizationHeader())
            .accept(MediaType.APPLICATION_JSON)
            .acceptCharset(StandardCharsets.UTF_8)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
            })
            .block());

        if (authProvider == AuthProvider.GITHUB && !jsonResponse.has("email")) {
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
            jsonResponse.put("email", email);
        }

        try {
            AuthUserResponse authUserResponse = authProvider.parseAuthUserResponse(jsonResponse);
            Member authMember = memberService.findAuthMember(authProvider, authUserResponse.getResourceOwnerId());
            if (authMember == null) {
                memberService.validateDuplicatedEmail(authUserResponse.getEmail());
                return AuthResponse.from(authUserResponse);
            }
            return AuthResponse.from(MemberResponse.from(authMember));
        } catch (JSONException e) {
            throw new EssentialFieldDisagreeException("필수 제공 동의 항목을 동의하지 않았습니다.");
        }
    }
}
