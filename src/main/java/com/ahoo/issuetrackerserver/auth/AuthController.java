package com.ahoo.issuetrackerserver.auth;

import com.ahoo.issuetrackerserver.auth.dto.AuthUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/{provider}")
    public AuthUserResponse authMemberInfo(@PathVariable String provider, @RequestParam String code) {
        AuthProviderType authProviderType = AuthProviderType.valueOf(provider.toUpperCase());
        AccessToken accessTokenResponse = authService.requestAccessToken(authProviderType, code);
        AuthUserResponse authUser = authService.requestAuthUser(authProviderType, accessTokenResponse);

        return authUser;
    }
}
