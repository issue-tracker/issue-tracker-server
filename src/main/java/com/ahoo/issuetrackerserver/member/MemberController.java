package com.ahoo.issuetrackerserver.member;

import com.ahoo.issuetrackerserver.auth.AccessToken;
import com.ahoo.issuetrackerserver.auth.JwtService;
import com.ahoo.issuetrackerserver.auth.RefreshToken;
import com.ahoo.issuetrackerserver.auth.RefreshTokenRepository;
import com.ahoo.issuetrackerserver.auth.SignInMemberId;
import com.ahoo.issuetrackerserver.auth.jwt.JwtGenerator;
import com.ahoo.issuetrackerserver.exception.ErrorResponse;
import com.ahoo.issuetrackerserver.member.dto.AuthMemberCreateRequest;
import com.ahoo.issuetrackerserver.member.dto.GeneralMemberCreateRequest;
import com.ahoo.issuetrackerserver.member.dto.GeneralSignInRequest;
import com.ahoo.issuetrackerserver.member.dto.MemberAndTokenResponse;
import com.ahoo.issuetrackerserver.member.dto.MemberResponse;
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
                        schema = @Schema(implementation = MemberResponse.class)
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
    public MemberAndTokenResponse signUpByAuth(@Valid @RequestBody AuthMemberCreateRequest memberCreateRequest,
        HttpServletResponse response) {
        MemberResponse memberResponse = memberService.signUpByAuth(memberCreateRequest);

        AccessToken accessToken = JwtGenerator.generateAccessToken(memberResponse.getId());
        RefreshToken refreshToken = JwtGenerator.generateRefreshToken(memberResponse.getId());
        refreshTokenRepository.save(refreshToken);

        response.addCookie(refreshToken.toCookie());
        return MemberAndTokenResponse.of(memberResponse, accessToken);
    }

    @Operation(summary = "일반 로그인",
        description = "일반 로그인을 진행합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "일반 로그인 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = MemberResponse.class)
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
    public MemberAndTokenResponse singInByGeneral(@RequestBody GeneralSignInRequest generalSignInRequest, HttpServletResponse response) {
        MemberResponse memberResponse = memberService.signInByGeneral(generalSignInRequest.getId(), generalSignInRequest.getPassword());

        AccessToken accessToken = JwtGenerator.generateAccessToken(memberResponse.getId());
        RefreshToken refreshToken = JwtGenerator.generateRefreshToken(memberResponse.getId());
        refreshTokenRepository.save(refreshToken);

        response.addCookie(refreshToken.toCookie());
        return MemberAndTokenResponse.of(memberResponse, accessToken);
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
        Member findMember = memberService.findById(memberId);

        return MemberResponse.from(findMember);
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
    public void signOut(@CookieValue(value = "refresh_token") Cookie refreshTokenCookie, HttpServletResponse response) {
        RefreshToken refreshToken = new RefreshToken(refreshTokenCookie.getValue());
        jwtService.validateToken(refreshToken);

        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        refreshTokenRepository.findById(refreshToken.getToken()).ifPresent(refreshTokenRepository::delete);
    }
}
