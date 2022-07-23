package com.ahoo.issuetrackerserver.member;

import com.ahoo.issuetrackerserver.member.dto.GeneralMemberCreateRequest;
import com.ahoo.issuetrackerserver.member.dto.MemberResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse signUpByGeneral(@Valid @RequestBody GeneralMemberCreateRequest memberCreateRequest) {
        return memberService.signUpByGeneral(memberCreateRequest);
    }

    @GetMapping("/members/login-id/{loginId}/exists")
    public Boolean checkDuplicatedLoginId(@PathVariable String loginId) {
        return memberService.isDuplicatedLoginId(loginId);
    }

    @GetMapping("/members/nickname/{nickname}/exists")
    public Boolean checkDuplicatedNickname(@PathVariable String nickname) {
        return memberService.isDuplicatedNickname(nickname);
    }

    @GetMapping("/members/email/{email}/exists")
    public Boolean checkDuplicatedEmail(@PathVariable String email) {
        return memberService.isDuplicatedEmail(email);
    }
}
