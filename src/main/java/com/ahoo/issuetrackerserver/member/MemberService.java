package com.ahoo.issuetrackerserver.member;

import com.ahoo.issuetrackerserver.exception.DuplicateMemberException;
import com.ahoo.issuetrackerserver.exception.IllegalAuthProviderTypeException;
import com.ahoo.issuetrackerserver.member.dto.AuthMemberCreateRequest;
import com.ahoo.issuetrackerserver.member.dto.GeneralMemberCreateRequest;
import com.ahoo.issuetrackerserver.member.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse signUpByGeneral(GeneralMemberCreateRequest memberCreateRequest) {
        validateGeneralMemberRequest(memberCreateRequest);

        Member savedMember = memberRepository.save(memberCreateRequest.toEntity());

        return MemberResponse.from(savedMember);
    }

    @Transactional
    public MemberResponse signUpByAuth(AuthMemberCreateRequest memberCreateRequest) {
        validateAuthMemberRequest(memberCreateRequest);

        try {
            Member savedMember = memberRepository.save(memberCreateRequest.toEntity());
            return MemberResponse.from(savedMember);
        } catch (IllegalArgumentException e) {
            throw new IllegalAuthProviderTypeException(e);
        }
    }

    @Transactional(readOnly = true)
    public Boolean isDuplicatedNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public Boolean isDuplicatedLoginId(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    @Transactional(readOnly = true)
    public Boolean isDuplicatedEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    private void validateGeneralMemberRequest(GeneralMemberCreateRequest generalMemberCreateRequest) {
        validateDuplicatedEmail(generalMemberCreateRequest.getEmail());

        if (isDuplicatedLoginId(generalMemberCreateRequest.getLoginId())) {
            throw new DuplicateMemberException("중복되는 아이디가 존재합니다.");
        }

        if (isDuplicatedNickname(generalMemberCreateRequest.getNickname())) {
            throw new DuplicateMemberException("중복되는 닉네임이 존재합니다.");
        }
    }

    private void validateAuthMemberRequest(AuthMemberCreateRequest authMemberCreateRequest) {
        validateDuplicatedEmail(authMemberCreateRequest.getEmail());

        if (isDuplicatedNickname(authMemberCreateRequest.getNickname())) {
            throw new DuplicateMemberException("중복되는 닉네임이 존재합니다.");
        }
    }

    public void validateDuplicatedEmail(String email) {
        memberRepository.findByEmail(email).ifPresent(m -> {
            String authProviderName = m.getAuthProviderType().getProviderName();
            throw new DuplicateMemberException(authProviderName + "(으)로 이미 가입된 이메일입니다.");
        });
    }
}
