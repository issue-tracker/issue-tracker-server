package com.ahoo.issuetrackerserver.member;

import com.ahoo.issuetrackerserver.auth.AccessToken;
import com.ahoo.issuetrackerserver.auth.RefreshToken;
import com.ahoo.issuetrackerserver.auth.RefreshTokenRepository;
import com.ahoo.issuetrackerserver.auth.jwt.JwtGenerator;
import com.ahoo.issuetrackerserver.exception.ErrorResponse;
import com.ahoo.issuetrackerserver.member.dto.AuthMemberCreateRequest;
import com.ahoo.issuetrackerserver.member.dto.GeneralMemberCreateRequest;
import com.ahoo.issuetrackerserver.member.dto.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "members", description = "회원 API")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
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
    public MemberResponse signUpByAuth(@Valid @RequestBody AuthMemberCreateRequest memberCreateRequest,
        HttpServletResponse response) {
        MemberResponse memberResponse = memberService.signUpByAuth(memberCreateRequest);

        AccessToken accessToken = JwtGenerator.generateAccessToken(memberResponse.getId());
        RefreshToken refreshToken = JwtGenerator.generateRefreshToken(memberResponse.getId());
        refreshTokenRepository.save(refreshToken);

        addTokenCookies(response, accessToken, refreshToken);

        return memberResponse;
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
    @GetMapping("/login-id/{loginId}/exists")
    public Boolean checkDuplicatedLoginId(@PathVariable String loginId) {
        return memberService.isDuplicatedLoginId(loginId);
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

    private void addTokenCookies(HttpServletResponse response, AccessToken accessToken, RefreshToken refreshToken) {
        response.addCookie(accessToken.toCookie());
        response.addCookie(refreshToken.toCookie());
    }
}
