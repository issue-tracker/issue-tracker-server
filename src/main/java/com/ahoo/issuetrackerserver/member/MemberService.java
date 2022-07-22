package com.ahoo.issuetrackerserver.member;

import com.ahoo.issuetrackerserver.exception.DuplicateMemberException;
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
        validateMemberRequest(memberCreateRequest);

        Member savedMember = memberRepository.save(memberCreateRequest.toEntity());

        return MemberResponse.from(savedMember);
    }


    @Transactional(readOnly = true)
    public Boolean checkDuplicatedNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public Boolean checkDuplicatedLoginId(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    @Transactional(readOnly = true)
    public Boolean checkDuplicatedEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    private void validateMemberRequest(GeneralMemberCreateRequest generalMemberCreateRequest) {
        if (checkDuplicatedLoginId(generalMemberCreateRequest.getLoginId())) {
            throw new DuplicateMemberException("중복되는 아이디가 존재합니다.");
        }

        if (checkDuplicatedNickname(generalMemberCreateRequest.getNickname())) {
            throw new DuplicateMemberException("중복되는 닉네임이 존재합니다.");
        }

        if (checkDuplicatedEmail(generalMemberCreateRequest.getEmail())) {
            memberRepository.findByEmail(generalMemberCreateRequest.getEmail()).ifPresent(m -> {
                String authProviderName = m.getAuthProviderType().getProviderName();
                throw new DuplicateMemberException(authProviderName + "(으)로 이미 가입된 이메일입니다.");
            });
        }
    }
}
