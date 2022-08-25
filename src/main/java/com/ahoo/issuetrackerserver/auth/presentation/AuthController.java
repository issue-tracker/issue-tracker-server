package com.ahoo.issuetrackerserver.auth.presentation;

import com.ahoo.issuetrackerserver.auth.application.AuthService;
import com.ahoo.issuetrackerserver.auth.application.JwtService;
import com.ahoo.issuetrackerserver.auth.domain.AuthProvider;
import com.ahoo.issuetrackerserver.auth.infrastructure.RefreshTokenRepository;
import com.ahoo.issuetrackerserver.auth.infrastructure.jwt.AccessToken;
import com.ahoo.issuetrackerserver.auth.infrastructure.jwt.JwtGenerator;
import com.ahoo.issuetrackerserver.auth.infrastructure.jwt.RefreshToken;
import com.ahoo.issuetrackerserver.auth.presentation.dto.AuthAccessToken;
import com.ahoo.issuetrackerserver.auth.presentation.dto.AuthResponse;
import com.ahoo.issuetrackerserver.auth.presentation.dto.AuthUserResponse;
import com.ahoo.issuetrackerserver.common.argumentresolver.SignInMemberId;
import com.ahoo.issuetrackerserver.common.exception.ErrorResponse;
import com.ahoo.issuetrackerserver.common.exception.ErrorType;
import com.ahoo.issuetrackerserver.common.exception.UnAuthorizedException;
import com.ahoo.issuetrackerserver.member.application.MemberService;
import com.ahoo.issuetrackerserver.member.presentation.dto.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final MemberService memberService;
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

        AuthUserResponse authUserResponse;
        try {
            AuthAccessToken accessTokenResponse = authService.requestAccessToken(authProvider, code);
            authUserResponse = authService.requestAuthUser(authProvider, accessTokenResponse);
        } catch (WebClientResponseException e) {
            throw new UnAuthorizedException(ErrorType.INVALID_CODE, e);
        }

        MemberResponse authMember = authService.findAuthMember(authProvider, authUserResponse.getResourceOwnerId());
        if (authMember == null) {
            return authService.responseSignUpFormData(authUserResponse);
        }

        AccessToken accessToken = JwtGenerator.generateAccessToken(authMember.getId());
        RefreshToken refreshToken = JwtGenerator.generateRefreshToken(authMember.getId());
        refreshTokenRepository.save(refreshToken);

        response.addCookie(refreshToken.toCookie());
        return authService.responseSignInMember(authMember, accessToken);
    }

    @Operation(summary = "액세스 토큰 재발급",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "액세스 토큰을 재발급합니다.",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = AuthResponse.class)
                    )
                }),
            @ApiResponse(responseCode = "401",
                description = "액세스 토큰 재발급 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @GetMapping("/reissue")
    public AuthResponse reissueToken(@CookieValue(value = "refresh_token") Cookie refreshTokenCookie,
        HttpServletResponse response) {
        RefreshToken refreshToken = RefreshToken.of(refreshTokenCookie.getValue());

        refreshTokenRepository.findById(refreshToken.getToken())
            .orElseThrow(() -> new UnAuthorizedException(ErrorType.INVALID_REFRESH_TOKEN));

        Long memberId = jwtService.extractMemberId(refreshToken);
        MemberResponse memberResponse = memberService.findById(memberId);

        AccessToken newAccessToken = JwtGenerator.generateAccessToken(memberId);
        RefreshToken newRefreshToken = JwtGenerator.generateRefreshToken(memberId);
        refreshTokenRepository.save(newRefreshToken);

        response.addCookie(refreshToken.toCookie());
        return authService.responseSignInMember(memberResponse, newAccessToken);
    }

    @Operation(summary = "로그인 검사 테스트용 API",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "로그인 검사 성공(로그인 사용자의 id를 반환합니다.)",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Long.class)
                    )
                }),
            @ApiResponse(responseCode = "401",
                description = "로그인 검사 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @GetMapping("/test")
    public Long signInRequiredTest(@SignInMemberId Long memberId) {
        return memberId;
    }
}
