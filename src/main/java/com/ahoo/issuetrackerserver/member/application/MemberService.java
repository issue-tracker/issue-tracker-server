package com.ahoo.issuetrackerserver.member.application;

import com.ahoo.issuetrackerserver.auth.domain.AuthProvider;
import com.ahoo.issuetrackerserver.common.exception.DuplicateMemberException;
import com.ahoo.issuetrackerserver.common.exception.IllegalAuthProviderTypeException;
import com.ahoo.issuetrackerserver.member.domain.Member;
import com.ahoo.issuetrackerserver.member.infrastructure.MemberRepository;
import com.ahoo.issuetrackerserver.member.presentation.dto.AuthMemberCreateRequest;
import com.ahoo.issuetrackerserver.member.presentation.dto.GeneralMemberCreateRequest;
import com.ahoo.issuetrackerserver.member.presentation.dto.MemberResponse;
import java.util.NoSuchElementException;
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
    public MemberResponse signInByGeneral(String id, String password) {
        Member findMember = memberRepository.findBySignInId(id).orElseThrow(() -> new IllegalArgumentException("로그인에 실패했습니다. 아이디와 비밀번호를 다시 확인해주세요."));

        if (!findMember.isCorrectPassword(password)) {
            throw new IllegalArgumentException("로그인에 실패했습니다. 아이디와 비밀번호를 다시 확인해주세요.");
        }

        return MemberResponse.from(findMember);
    }

    @Transactional(readOnly = true)
    public Boolean isDuplicatedNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public Boolean isDuplicatedSignInId(String signInId) {
        return memberRepository.existsBySignInId(signInId);
    }

    @Transactional(readOnly = true)
    public Boolean isDuplicatedEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    private void validateGeneralMemberRequest(GeneralMemberCreateRequest generalMemberCreateRequest) {
        validateDuplicatedEmail(generalMemberCreateRequest.getEmail());

        if (isDuplicatedSignInId(generalMemberCreateRequest.getSignInId())) {
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

    @Transactional(readOnly = true)
    public void validateDuplicatedEmail(String email) {
        memberRepository.findByEmail(email).ifPresent(m -> {
            String authProviderName = m.getAuthProviderType().getProviderName();
            throw new DuplicateMemberException(authProviderName + "(으)로 이미 가입된 이메일입니다.");
        });
    }

    @Transactional(readOnly = true)
    public MemberResponse findAuthMember(AuthProvider authProvider, String resourceOwnerId) {
        Member findMember = memberRepository.findByAuthProviderTypeAndResourceOwnerId(authProvider, resourceOwnerId)
            .orElse(null);

        if (findMember == null) {
            return null;
        }

        return MemberResponse.from(findMember);
    }

    @Transactional(readOnly = true)
    public MemberResponse findById(Long id) {
        Member findMember = memberRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));
        return MemberResponse.from(findMember);
    }
}