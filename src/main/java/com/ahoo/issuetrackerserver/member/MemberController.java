package com.ahoo.issuetrackerserver.member;

import com.ahoo.issuetrackerserver.member.dto.GeneralMemberCreateRequest;
import com.ahoo.issuetrackerserver.member.dto.MemberResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public MemberResponse signUpByGeneral(@Valid @RequestBody GeneralMemberCreateRequest memberCreateRequest) {
        return memberService.signUpByGeneral(memberCreateRequest);
    }

    @GetMapping("/members/loginid")
    public Boolean isDuplicateLoginId(@RequestParam("loginId") String loginId) {
        return memberService.validateLoginId(loginId);
    }

    @GetMapping("/members/nickname")
    public Boolean isDuplicateNickname(@RequestParam("nickname") String nickname) {
        return memberService.validateNickname(nickname);
    }

    @GetMapping("/members/email")
    public Boolean isDuplicateEmail(@RequestParam("email") String email) {
        return memberService.validateEmail(email);
    }
}
