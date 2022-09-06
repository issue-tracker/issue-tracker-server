package com.ahoo.issuetrackerserver.member.application;

import com.ahoo.issuetrackerserver.auth.domain.AuthProvider;
import com.ahoo.issuetrackerserver.common.exception.ApplicationException;
import com.ahoo.issuetrackerserver.common.exception.DuplicatedMemberException;
import com.ahoo.issuetrackerserver.common.exception.ErrorType;
import com.ahoo.issuetrackerserver.common.exception.IllegalAuthProviderTypeException;
import com.ahoo.issuetrackerserver.member.domain.Member;
import com.ahoo.issuetrackerserver.member.infrastructure.MemberRepository;
import com.ahoo.issuetrackerserver.member.presentation.dto.AuthMemberCreateRequest;
import com.ahoo.issuetrackerserver.member.presentation.dto.GeneralMemberCreateRequest;
import com.ahoo.issuetrackerserver.member.presentation.dto.MemberResponse;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
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
            throw new IllegalAuthProviderTypeException(ErrorType.INVALID_AUTH_PROVIDER_TYPE, e);
        }
    }

    @Transactional(readOnly = true)
    public MemberResponse signInByGeneral(String id, String password) {
        Member findMember = memberRepository.findBySignInId(id)
            .orElseThrow(() -> new ApplicationException(ErrorType.SIGN_IN_FAIL, new IllegalArgumentException()));

        if (!findMember.isCorrectPassword(password)) {
            throw new ApplicationException(ErrorType.SIGN_IN_FAIL, new IllegalArgumentException());
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
            throw new DuplicatedMemberException(ErrorType.DUPLICATED_ID);
        }

        if (isDuplicatedNickname(generalMemberCreateRequest.getNickname())) {
            throw new DuplicatedMemberException(ErrorType.DUPLICATED_NICKNAME);
        }
    }

    private void validateAuthMemberRequest(AuthMemberCreateRequest authMemberCreateRequest) {
        validateDuplicatedEmail(authMemberCreateRequest.getEmail());

        if (isDuplicatedNickname(authMemberCreateRequest.getNickname())) {
            throw new DuplicatedMemberException(ErrorType.DUPLICATED_NICKNAME);
        }
    }

    @Transactional(readOnly = true)
    public void validateDuplicatedEmail(String email) {
        memberRepository.findByEmail(email).ifPresent(m -> {
            String authProviderName = m.getAuthProviderType().getProviderName();
            throw new DuplicatedMemberException(ErrorType.DUPLICATED_EMAIL, authProviderName);
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
        Member findMember = memberRepository.findById(id)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_MEMBER, new NoSuchElementException()));
        return MemberResponse.from(findMember);
    }

    @Transactional
    public List<MemberResponse> findAll() {
        return memberRepository.findAll().stream()
            .map(MemberResponse::from)
            .collect(Collectors.toUnmodifiableList());
    }
}
