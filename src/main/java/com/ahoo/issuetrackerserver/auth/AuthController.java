package com.ahoo.issuetrackerserver.auth;

import com.ahoo.issuetrackerserver.auth.dto.AuthAccessToken;
import com.ahoo.issuetrackerserver.auth.dto.AuthResponse;
import com.ahoo.issuetrackerserver.auth.dto.AuthUserResponse;
import com.ahoo.issuetrackerserver.auth.jwt.JwtGenerator;
import com.ahoo.issuetrackerserver.exception.ErrorResponse;
import com.ahoo.issuetrackerserver.exception.UnAuthorizedException;
import com.ahoo.issuetrackerserver.member.Member;
import com.ahoo.issuetrackerserver.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.Arrays;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
            throw new UnAuthorizedException("code가 유효하지 않습니다.", e);
        }

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

    @RequestMapping(method = RequestMethod.HEAD, value = "/reissue")
    public AuthResponse reissueToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new UnAuthorizedException("요청에 refresh_token 쿠키가 존재하지 않습니다.");
        }
        RefreshToken refreshToken = Arrays.stream(cookies)
            .filter(c -> c.getName().equals("refresh_token"))
            .map(c -> new RefreshToken(c.getValue()))
            .findFirst()
            .orElseThrow(() -> new UnAuthorizedException("요청에 access_token 쿠키가 존재하지 않습니다."));

        refreshTokenRepository.findById(refreshToken.getToken()).orElseThrow(() -> new UnAuthorizedException("refresh_token의 유효기간이 만료되었습니다."));

        Long memberId = jwtService.extractMemberId(refreshToken);
        Member signInMember = memberService.findById(memberId);

        AccessToken newAccessToken = JwtGenerator.generateAccessToken(memberId);
        RefreshToken newRefreshToken = JwtGenerator.generateRefreshToken(memberId);
        refreshTokenRepository.save(newRefreshToken);

        addTokenCookies(response, newAccessToken, newRefreshToken);
        return authService.responseSignInMember(signInMember);
    }

    private void addTokenCookies(HttpServletResponse response, AccessToken accessToken, RefreshToken refreshToken) {
        response.addCookie(accessToken.toCookie());
        response.addCookie(refreshToken.toCookie());
    }

    @Operation(summary = "로그인 검사 테스트용 API",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "로그인 검사 성공(로그인 사용자의 id를 반환합니다.)",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = String.class)
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
