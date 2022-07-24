package com.ahoo.issuetrackerserver.auth;

import com.ahoo.issuetrackerserver.auth.dto.AuthUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/github")
    public AuthUserResponse authMemberInfo현(String code) {
        AccessToken accessTokenResponse = authService.requestAccessToken(code);
        AuthUserResponse authUser = authService.requestAuthUser(accessTokenResponse);

        return authUser;
    }
}
