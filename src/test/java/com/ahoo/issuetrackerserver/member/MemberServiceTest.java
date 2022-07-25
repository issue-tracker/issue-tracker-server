package com.ahoo.issuetrackerserver.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.ahoo.issuetrackerserver.auth.AuthProvider;
import com.ahoo.issuetrackerserver.exception.DuplicateMemberException;
import com.ahoo.issuetrackerserver.member.dto.AuthMemberCreateRequest;
import com.ahoo.issuetrackerserver.member.dto.GeneralMemberCreateRequest;
import com.ahoo.issuetrackerserver.member.dto.MemberResponse;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.event.annotation.BeforeTestClass;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("일반 회원가입은")
    class Describe_signUpByGeneral {

        @BeforeTestClass
        void beforeTestClass() {
            Member member = Member.of(1L, "who-hoo", "1234", "who.ho3ov@gmail.com", "hoo",
                "https://avatars.githubusercontent.com/u/68011320?v=4", AuthProvider.GITHUB);
            given(memberRepository.findByEmail("who.ho3ov@gmail.com")).willReturn(Optional.of(member));
            given(memberRepository.existsByLoginId("who-hoo")).willReturn(true);
            given(memberRepository.existsByNickname("hoo")).willReturn(true);
        }

        @Test
        void 중복되지_않는_이메일_닉네임_아이디를_가진_회원가입_요청_입력이_주어지면_회원가입이_성공한다() {
            //given
            GeneralMemberCreateRequest successRequest = GeneralMemberCreateRequest.of("ak2j38", "1234",
                "ak2j38@gmail.com", "ader",
                "https://avatars.githubusercontent.com/u/29879110?v=4");
            Member newMember = successRequest.toEntity();
            Member savedMember = Member.of(2L, "ak2j38", "1234", "ak2j38@gmail.com", "ader",
                "https://avatars.githubusercontent.com/u/29879110?v=4", AuthProvider.NONE);
            given(memberRepository.findByEmail(successRequest.getEmail())).willReturn(Optional.empty());
            given(memberRepository.existsByLoginId(successRequest.getLoginId())).willReturn(false);
            given(memberRepository.existsByNickname(successRequest.getNickname())).willReturn(false);
            given(memberRepository.save(newMember)).willReturn(savedMember);

            //when
            MemberResponse actual = memberService.signUpByGeneral(successRequest);

            //then
            then(memberRepository).should(times(1)).findByEmail(successRequest.getEmail());
            then(memberRepository).should(times(1)).existsByLoginId(successRequest.getLoginId());
            then(memberRepository).should(times(1)).existsByNickname(successRequest.getNickname());
            then(memberRepository).should(times(1)).save(newMember);
            then(memberRepository).shouldHaveNoMoreInteractions();
            assertThat(actual.getId()).isEqualTo(2L);
        }

        @Test
        void 중복된_이메일을_가진_회원가입_요청이_주어지면_이미_가입된_이메일이라는_메시지를_가진_중복_회원_에러가_반환되고_회원가입이_실패한다() {
            //given
            GeneralMemberCreateRequest failureRequest = GeneralMemberCreateRequest.of("ak2j38", "1234",
                "who.ho3ov@gmail.com", "ader",
                "https://avatars.githubusercontent.com/u/29879110?v=4");
            Member duplicatedMember = Member.of(1L, "who-hoo", "1234", "who.ho3ov@gmail.com", "hoo",
                "https://avatars.githubusercontent.com/u/68011320?v=4", AuthProvider.GITHUB);
            given(memberRepository.findByEmail(failureRequest.getEmail())).willReturn(Optional.of(duplicatedMember));

            //when

            //then
            assertThatThrownBy(() -> memberService.signUpByGeneral(failureRequest))
                .isInstanceOf(DuplicateMemberException.class)
                .hasMessage(duplicatedMember.getAuthProviderType().getProviderName() + "(으)로 이미 가입된 이메일입니다.");
            then(memberRepository).should(times(1)).findByEmail(failureRequest.getEmail());
            then(memberRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        void 중복된_아이디를_가진_회원가입_요청이_주어지면_이미_가입된_아이디라는_메시지를_가진_중복_회원_에러가_반환되고_회원가입이_실패한다() {
            //given
            GeneralMemberCreateRequest failureRequest = GeneralMemberCreateRequest.of("who-hoo", "1234",
                "ak2j38@gmail.com", "ader",
                "https://avatars.githubusercontent.com/u/29879110?v=4");
            given(memberRepository.findByEmail(failureRequest.getEmail())).willReturn(Optional.empty());
            given(memberRepository.existsByLoginId(failureRequest.getLoginId())).willReturn(true);

            //when

            //then
            assertThatThrownBy(() -> memberService.signUpByGeneral(failureRequest))
                .isInstanceOf(DuplicateMemberException.class)
                .hasMessage("중복되는 아이디가 존재합니다.");
            then(memberRepository).should(times(1)).findByEmail(failureRequest.getEmail());
            then(memberRepository).should(times(1)).existsByLoginId(failureRequest.getLoginId());
            then(memberRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        void 중복된_닉네임을_가진_회원가입_요청이_주어지면_이미_가입된_닉네임이라는_메시지를_가진_중복_회원_에러가_반환되고_회원가입이_실패한다() {
            //given
            GeneralMemberCreateRequest failureRequest = GeneralMemberCreateRequest.of("ak2j38", "1234",
                "ak2j38@gmail.com", "hoo",
                "https://avatars.githubusercontent.com/u/29879110?v=4");
            given(memberRepository.findByEmail(failureRequest.getEmail())).willReturn(Optional.empty());
            given(memberRepository.existsByLoginId(failureRequest.getLoginId())).willReturn(false);
            given(memberRepository.existsByNickname(failureRequest.getNickname())).willReturn(true);

            //when

            //then
            assertThatThrownBy(() -> memberService.signUpByGeneral(failureRequest))
                .isInstanceOf(DuplicateMemberException.class)
                .hasMessage("중복되는 닉네임이 존재합니다.");
            then(memberRepository).should(times(1)).findByEmail(failureRequest.getEmail());
            then(memberRepository).should(times(1)).existsByLoginId(failureRequest.getLoginId());
            then(memberRepository).should(times(1)).existsByNickname(failureRequest.getNickname());
            then(memberRepository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("간편 회원가입은")
    class Describe_signUpByAuth {

        @BeforeTestClass
        void beforeTestClass() {
            Member member = Member.of(1L, "who-hoo", "1234", "who.ho3ov@gmail.com", "hoo",
                "https://avatars.githubusercontent.com/u/68011320?v=4", AuthProvider.GITHUB);
            given(memberRepository.findByEmail("who.ho3ov@gmail.com")).willReturn(Optional.of(member));
            given(memberRepository.existsByLoginId("who-hoo")).willReturn(true);
            given(memberRepository.existsByNickname("hoo")).willReturn(true);
        }

        @Test
        void 중복되지_않는_이메일_닉네임을_가진_회원가입_요청_입력이_주어지면_회원가입이_성공한다() {
            //given
            AuthMemberCreateRequest successRequest = AuthMemberCreateRequest.of("ak2j38@gmail.com", "ader",
                "https://avatars.githubusercontent.com/u/29879110?v=4", "GITHUB");
            Member newMember = successRequest.toEntity();
            Member savedMember = Member.of(2L, null, null, "ak2j38@gmail.com", "ader",
                "https://avatars.githubusercontent.com/u/29879110?v=4", AuthProvider.GITHUB);
            given(memberRepository.findByEmail(successRequest.getEmail())).willReturn(Optional.empty());
            given(memberRepository.existsByNickname(successRequest.getNickname())).willReturn(false);
            given(memberRepository.save(newMember)).willReturn(savedMember);

            //when
            MemberResponse actual = memberService.signUpByAuth(successRequest);

            //then
            then(memberRepository).should(times(1)).findByEmail(successRequest.getEmail());
            then(memberRepository).should(times(1)).existsByNickname(successRequest.getNickname());
            then(memberRepository).should(times(1)).save(newMember);
            then(memberRepository).shouldHaveNoMoreInteractions();
            assertThat(actual.getId()).isEqualTo(2L);
        }

        @Test
        void 중복된_이메일을_가진_회원가입_요청이_주어지면_이미_가입된_이메일이라는_메시지를_가진_중복_회원_에러가_반환되고_회원가입이_실패한다() {
            //given
            AuthMemberCreateRequest failureRequest = AuthMemberCreateRequest.of("who.ho3ov@gmail.com", "ader",
                "https://avatars.githubusercontent.com/u/29879110?v=4", "GITHUB");
            Member duplicatedMember = Member.of(1L, "who-hoo", "1234", "who.ho3ov@gmail.com", "hoo",
                "https://avatars.githubusercontent.com/u/68011320?v=4", AuthProvider.GITHUB);
            given(memberRepository.findByEmail(failureRequest.getEmail())).willReturn(Optional.of(duplicatedMember));

            //when

            //then
            assertThatThrownBy(() -> memberService.signUpByAuth(failureRequest))
                .isInstanceOf(DuplicateMemberException.class)
                .hasMessage(duplicatedMember.getAuthProviderType().getProviderName() + "(으)로 이미 가입된 이메일입니다.");
            then(memberRepository).should(times(1)).findByEmail(failureRequest.getEmail());
            then(memberRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        void 중복된_닉네임을_가진_회원가입_요청이_주어지면_이미_가입된_닉네임이라는_메시지를_가진_중복_회원_에러가_반환되고_회원가입이_실패한다() {
            //given
            AuthMemberCreateRequest failureRequest = AuthMemberCreateRequest.of("ak2j38@gmail.com", "hoo",
                "https://avatars.githubusercontent.com/u/29879110?v=4", "GITHUB");
            given(memberRepository.findByEmail(failureRequest.getEmail())).willReturn(Optional.empty());
            given(memberRepository.existsByNickname(failureRequest.getNickname())).willReturn(true);

            //when

            //then
            assertThatThrownBy(() -> memberService.signUpByAuth(failureRequest))
                .isInstanceOf(DuplicateMemberException.class)
                .hasMessage("중복되는 닉네임이 존재합니다.");
            then(memberRepository).should(times(1)).findByEmail(failureRequest.getEmail());
            then(memberRepository).should(times(1)).existsByNickname(failureRequest.getNickname());
            then(memberRepository).shouldHaveNoMoreInteractions();
        }
    }
}
