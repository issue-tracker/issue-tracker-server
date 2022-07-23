package com.ahoo.issuetrackerserver.auth;

import com.ahoo.issuetrackerserver.auth.dto.AuthUserRequest;
import com.ahoo.issuetrackerserver.auth.dto.AuthUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final WebClient webClient;

    public AccessToken requestAccessToken(String code) {
        AuthUserRequest authUserRequest = new AuthUserRequest("1b3426be6211740a5720",
            "7763b59411ae40e3fad87cf142b9b161dddce5f3", code, "");
        
        return WebClient.create().post()
            .uri("https://github.com/login/oauth/access_token")
            .bodyValue(authUserRequest)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(AccessToken.class)
            .block();
    }

    public AuthUserResponse requestAuthMemberInfo(AccessToken accessToken) {
        return webClient.get()
            .uri("https://api.github.com/user")
            .header(HttpHeaders.AUTHORIZATION, accessToken.convertAuthorizationHeader())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(AuthUserResponse.class)
            .block();
    }
}
