package com.ahoo.issuetrackerserver.auth;

import com.ahoo.issuetrackerserver.auth.dto.AuthResponse;
import com.ahoo.issuetrackerserver.auth.dto.AuthUserResponse;
import com.ahoo.issuetrackerserver.exception.ErrorResponse;
import com.ahoo.issuetrackerserver.exception.EssentialFieldDisagreeException;
import com.ahoo.issuetrackerserver.member.Member;
import com.ahoo.issuetrackerserver.member.dto.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
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

    @Operation(summary = "OAuth 유저정보 조회/로그인",
        description = "기존 가입 유저이면 로그인을, 기존 가입 유저가 아니면 OAuth 유저정보를 조회합니다. provider로는 현재 GITHUB, NAVER, KAKAO만 가능합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "OAuth 유저정보 조회/로그인 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = AuthResponse.class)
                    )
                }),
            @ApiResponse(responseCode = "400",
                description = "OAuth 유저정보 조회/로그인 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @GetMapping("/{provider}")
    public AuthResponse authSign(@PathVariable String provider, @RequestParam String code,
        HttpServletResponse response) {
        AuthProvider authProvider = AuthProvider.valueOf(provider.toUpperCase());
        AccessToken accessTokenResponse = authService.requestAccessToken(authProvider, code);
        AuthUserResponse authUserResponse = authService.requestAuthUser(authProvider, accessTokenResponse);

        Member authMember = authService.findAuthMember(authProvider, authUserResponse.getResourceOwnerId());
        if (authMember == null) {
            return authService.responseSignUpFormData(authUserResponse);
        }
        //TODO
        // access_token, refresh_token 발급
        // refresh_token 저장
        response.addCookie(new Cookie("access_token", ""));
        response.addCookie(new Cookie("refresh_token", ""));
        return authService.responseSignInMember(authMember);
    }
}
