package com.ahoo.issuetrackerserver.member;

import com.ahoo.issuetrackerserver.member.dto.GeneralMemberCreateRequest;
import com.ahoo.issuetrackerserver.member.dto.MemberResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    @ResponseStatus(HttpStatus.OK)
    public MemberResponse signUpByGeneral(@Valid @RequestBody GeneralMemberCreateRequest memberCreateRequest) {
        return memberService.signUpByGeneral(memberCreateRequest);
    }
}
