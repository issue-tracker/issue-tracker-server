package com.ahoo.issuetrackerserver.auth;

import com.ahoo.issuetrackerserver.auth.dto.AuthAccessToken;
import com.ahoo.issuetrackerserver.auth.dto.AuthResponse;
import com.ahoo.issuetrackerserver.auth.dto.AuthUserResponse;
import com.ahoo.issuetrackerserver.auth.jwt.JwtGenerator;
import com.ahoo.issuetrackerserver.exception.ErrorResponse;
import com.ahoo.issuetrackerserver.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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
    private final RefreshTokenRepository refreshTokenRepository;

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
        AuthAccessToken accessTokenResponse = authService.requestAccessToken(authProvider, code);
        AuthUserResponse authUserResponse = authService.requestAuthUser(authProvider, accessTokenResponse);

        Member authMember = authService.findAuthMember(authProvider, authUserResponse.getResourceOwnerId());
        if (authMember == null) {
            return authService.responseSignUpFormData(authUserResponse);
        }

        AccessToken accessToken = JwtGenerator.generateAccessToken(authMember.getId());
        RefreshToken refreshToken = JwtGenerator.generateRefreshToken(authMember.getId());
        refreshTokenRepository.save(refreshToken);

        addTokenCookies(response, accessToken, refreshToken);
        return authService.responseSignInMember(authMember);
    }

    private void addTokenCookies(HttpServletResponse response, AccessToken accessToken, RefreshToken refreshToken) {
        Cookie accessTokenCookie = new Cookie("access_token", accessToken.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }
}
