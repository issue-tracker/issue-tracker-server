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
        memberRepository.findByEmail(memberCreateRequest.getEmail()).ifPresent(m -> {
            String authProviderName = m.getAuthProviderType().getProviderName();
            throw new DuplicateMemberException(authProviderName + "(으)로 이미 가입된 이메일입니다.");
        });
        validateLoginId(memberCreateRequest.getLoginId());
        validateNickname(memberCreateRequest.getNickname());

        Member savedMember = memberRepository.save(memberCreateRequest.toEntity());

        return MemberResponse.from(savedMember);
    }

    private void validateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new DuplicateMemberException("중복되는 닉네임이 존재합니다.");
        }
    }

    private void validateLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new DuplicateMemberException("중복되는 아이디가 존재합니다.");
        }
    }
}
