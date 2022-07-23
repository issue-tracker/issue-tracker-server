package com.ahoo.issuetrackerserver.auth;

import com.ahoo.issuetrackerserver.auth.dto.AuthUserResponse;
import com.ahoo.issuetrackerserver.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;

    @GetMapping("/github")
    public AuthUserResponse authMemberInfo(String code) {
        AccessToken accessTokenResponse = authService.requestAccessToken(code);
        AuthUserResponse authMemberInfo =
            authService.requestAuthMemberInfo(accessTokenResponse);
//        if (memberService.isDuplicatedEmail(authMemberInfo.getEmail())) {
//            throw new RuntimeException("");
//        }
        return authMemberInfo;
    }
}
