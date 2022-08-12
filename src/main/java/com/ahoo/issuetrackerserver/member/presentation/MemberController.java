package com.ahoo.issuetrackerserver.member.presentation;

import com.ahoo.issuetrackerserver.auth.application.JwtService;
import com.ahoo.issuetrackerserver.auth.infrastructure.RefreshTokenRepository;
import com.ahoo.issuetrackerserver.auth.infrastructure.jwt.AccessToken;
import com.ahoo.issuetrackerserver.auth.infrastructure.jwt.JwtGenerator;
import com.ahoo.issuetrackerserver.auth.infrastructure.jwt.RefreshToken;
import com.ahoo.issuetrackerserver.common.argumentresolver.SignInMemberId;
import com.ahoo.issuetrackerserver.common.exception.ErrorResponse;
import com.ahoo.issuetrackerserver.member.application.MemberService;
import com.ahoo.issuetrackerserver.member.presentation.dto.AuthMemberCreateRequest;
import com.ahoo.issuetrackerserver.member.presentation.dto.GeneralMemberCreateRequest;
import com.ahoo.issuetrackerserver.member.presentation.dto.GeneralSignInRequest;
import com.ahoo.issuetrackerserver.member.presentation.dto.MemberResponse;
import com.ahoo.issuetrackerserver.member.presentation.dto.SignResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "members", description = "회원 API")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String DELETE_COOKIE_PATH = "/";
    private static final int DELETE_COOKIE_AGE = 0;

    private final MemberService memberService;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Operation(summary = "일반 회원가입",
        description = "일반 회원으로 가입합니다.",
        responses = {
            @ApiResponse(responseCode = "201",
                description = "일반 회원가입 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = MemberResponse.class)
                    )
                }),
            @ApiResponse(responseCode = "400",
                description = "일반 회원가입 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @PostMapping("/new/general")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse signUpByGeneral(@Valid @RequestBody GeneralMemberCreateRequest memberCreateRequest) {
        return memberService.signUpByGeneral(memberCreateRequest);
    }

    @Operation(summary = "OAuth 회원가입",
        description = "OAuth 회원가입으로 가입합니다.",
        responses = {
            @ApiResponse(responseCode = "201",
                description = "OAuth 회원가입 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = SignResponse.class)
                    )
                }),
            @ApiResponse(responseCode = "400",
                description = "OAuth 회원가입 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @PostMapping("/new/auth")
    @ResponseStatus(HttpStatus.CREATED)
    public SignResponse signUpByAuth(@Valid @RequestBody AuthMemberCreateRequest memberCreateRequest,
        HttpServletResponse response) {
        MemberResponse memberResponse = memberService.signUpByAuth(memberCreateRequest);

        AccessToken accessToken = JwtGenerator.generateAccessToken(memberResponse.getId());
        RefreshToken refreshToken = JwtGenerator.generateRefreshToken(memberResponse.getId());
        refreshTokenRepository.save(refreshToken);

        response.addCookie(refreshToken.toCookie());
        return SignResponse.of(memberResponse, accessToken);
    }

    @Operation(summary = "일반 로그인",
        description = "일반 로그인을 진행합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "일반 로그인 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = SignResponse.class)
                    )
                }),
            @ApiResponse(responseCode = "400",
                description = "일반 로그인 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @PostMapping("/signin")
    public SignResponse signInByGeneral(@RequestBody GeneralSignInRequest generalSignInRequest,
        HttpServletResponse response) {
        MemberResponse memberResponse = memberService.signInByGeneral(generalSignInRequest.getId(),
            generalSignInRequest.getPassword());

        AccessToken accessToken = JwtGenerator.generateAccessToken(memberResponse.getId());
        RefreshToken refreshToken = JwtGenerator.generateRefreshToken(memberResponse.getId());
        refreshTokenRepository.save(refreshToken);

        response.addCookie(refreshToken.toCookie());
        return SignResponse.of(memberResponse, accessToken);
    }

    @Operation(summary = "로그인 아이디 중복 검사",
        description = "로그인 아이디의 중복 여부를 검사합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "로그인 아이디 중복 검사 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Boolean.class)
                    )
                })
        }
    )
    @GetMapping("/signin-id/{signInId}/exists")
    public Boolean checkDuplicatedSignInId(@PathVariable String signInId) {
        return memberService.isDuplicatedSignInId(signInId);
    }

    @Operation(summary = "닉네임 중복 검사",
        description = "닉네임의 중복 여부를 검사합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "닉네임 중복 검사 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Boolean.class)
                    )
                })
        }
    )
    @GetMapping("/nickname/{nickname}/exists")
    public Boolean checkDuplicatedNickname(@PathVariable String nickname) {
        return memberService.isDuplicatedNickname(nickname);
    }

    @Operation(summary = "이메일 중복 검사",
        description = "이메일의 중복 여부를 검사합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "이메일 중복 검사 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Boolean.class)
                    )
                })
        }
    )
    @GetMapping("/email/{email}/exists")
    public Boolean checkDuplicatedEmail(@PathVariable String email) {
        return memberService.isDuplicatedEmail(email);
    }

    @Operation(summary = "회원 로그인 정보",
        description = "회원 로그인 정보를 요청합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "회원 로그인 정보 응답 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = MemberResponse.class)
                    )
                }),
            @ApiResponse(responseCode = "400",
                description = "회원 로그인 정보 응답 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @GetMapping("/info")
    public MemberResponse getMemberInfo(@SignInMemberId Long memberId) {
        return memberService.findById(memberId);
    }

    @Operation(summary = "로그아웃",
        description = "로그아웃을 요청합니다.",
        responses = {
            @ApiResponse(responseCode = "204",
                description = "로그아웃 성공"),
            @ApiResponse(responseCode = "400",
                description = "로그아웃 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @RequestMapping(method = RequestMethod.HEAD, value = "/signout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signOut(@CookieValue(value = REFRESH_TOKEN) Cookie refreshTokenCookie, HttpServletResponse response) {
        RefreshToken refreshToken = RefreshToken.of(refreshTokenCookie.getValue());
        jwtService.validateToken(refreshToken);

        Cookie cookie = new Cookie(REFRESH_TOKEN, null);
        cookie.setMaxAge(DELETE_COOKIE_AGE);
        cookie.setPath(DELETE_COOKIE_PATH);
        response.addCookie(cookie);

        refreshTokenRepository.delete(refreshToken);
    }
}
