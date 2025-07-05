package gift.member.service;


import gift.domain.Member;
import gift.domain.Role;
import gift.global.exception.AuthenticationException;
import gift.global.exception.BadRequestEntityException;
import gift.global.exception.DuplicateEntityException;
import gift.global.exception.NotFoundEntityException;
import gift.member.dto.MemberCreateRequest;
import gift.member.dto.MemberDeleteRequest;
import gift.member.dto.MemberResponse;
import gift.member.dto.MemberUpdateRequest;
import gift.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
class MemberServiceV1Test {

    @InjectMocks
    private MemberServiceV1 memberService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 조회 성공")
    void getMemberSuccess() {

        // given
        Member member = createMember();

        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));

        // when
        MemberResponse response = memberService.findById(member.getId());

        //then
        assertThat(response.getEmail()).isEqualTo(member.getEmail());
        assertThat(response.getRole()).isEqualTo(member.getRole());
        verify(memberRepository).findById(member.getId());
    }

    @Test
    @DisplayName("회원 조회 실패")
    void getMemberFail() {

        UUID id = UUID.randomUUID();

        // given
        given(memberRepository.findById(id))
                .willReturn(Optional.empty());


        // when & then
        assertThatThrownBy(()->memberService.findById(id))
                .isInstanceOf(NotFoundEntityException.class);
        verify(memberRepository).findById(id);
    }


    @Test
    @DisplayName("회원가입 성공")
    void joinMemberSuccess() {

        // given

        MemberCreateRequest memberDto = createRequest();
        Member joinMember = createMember();

        given(memberRepository.findByEmail(memberDto.getEmail()))
                .willReturn(Optional.empty());

        given(memberRepository.save(any(Member.class)))
                .willReturn(joinMember);

        // when
        UUID saveId = memberService.save(memberDto);

        // then
        assertThat(saveId).isEqualTo(joinMember.getId());
        verify(memberRepository).save(any(Member.class));
        verify(memberRepository).findByEmail(memberDto.getEmail());
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 이메일")
    void joinMemberFail() {

        // given
        MemberCreateRequest memberDto = createRequest();
        Member joinMember = createMember();
        given(memberRepository.findByEmail(memberDto.getEmail()))
                .willReturn(Optional.of(joinMember));

        //when & then
        assertThatThrownBy(() -> memberService.save(memberDto))
                .isInstanceOf(DuplicateEntityException.class);
        verify(memberRepository).findByEmail(memberDto.getEmail());
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    @DisplayName("회원가입 실패 - 서로 다른 비밀번호")
    void joinMemberFail2() {

        // given
        MemberCreateRequest memberDto =
                new MemberCreateRequest("ljw2109@naver.com", "Qwer1234!!", "Qwer1234!!5" );

        //when & then
        assertThatThrownBy(() -> memberService.save(memberDto))
                .isInstanceOf(BadRequestEntityException.class);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void deleteMemberSuccess() {

        Member joinMember = createMember();
        MemberDeleteRequest deleteRequest = new MemberDeleteRequest("curPassword");
        // given
        given(memberRepository.findById(joinMember.getId()))
                .willReturn(Optional.of(joinMember));

        // when
        memberService.deleteMember(joinMember.getId(), deleteRequest);

        // then
        verify(memberRepository).deleteById(joinMember.getId());
        verify(memberRepository).findById(joinMember.getId());
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    @DisplayName("회원 탈퇴 실패 - 존재하는 회원 없음")
    void deleteMemberFail1() {


        MemberDeleteRequest deleteRequest = new MemberDeleteRequest("wrong");

        // given
        given(memberRepository.findById(any(UUID.class)))
                .willReturn(Optional.empty());

        // when
        assertThatThrownBy(()->memberService.deleteMember(UUID.randomUUID(), deleteRequest))
                .isInstanceOf(NotFoundEntityException.class);

        // then
        verify(memberRepository).findById(any(UUID.class));
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    @DisplayName("회원 탈퇴 실패 - 비밀번호가 다름")
    void deleteMemberFail2() {

        Member member = createMember();
        MemberDeleteRequest deleteRequest = new MemberDeleteRequest("wrong");
        // given
        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));

        // when
        assertThatThrownBy(()->memberService.deleteMember(member.getId(), deleteRequest))
                .isInstanceOf(AuthenticationException.class);

        // then
        verify(memberRepository).findById(member.getId());
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void updatePasswordSuccess() {

        // given
        Member member = createMember();
        MemberUpdateRequest updateRequest = new MemberUpdateRequest("curPassword","changePassword", "changePassword");

        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));

        // when
        memberService.changePassword(member.getId(), updateRequest);

        verify(memberRepository).findById(member.getId());
        verify(memberRepository).update(any(Member.class));
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    @DisplayName("비밀번호 변경 실패 - 인증 실패")
    void updatePasswordFail1() {

        // given
        Member member = createMember();
        MemberUpdateRequest updateRequest = new MemberUpdateRequest("wrongPassword","changePassword", "changePassword");

        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));

        // when
        assertThatThrownBy(()->memberService.changePassword(member.getId(), updateRequest))
                .isInstanceOf(AuthenticationException.class);

        verify(memberRepository).findById(member.getId());
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    @DisplayName("비밀번호 변경 실패 - newPassword와 confirmPassword가 다름")
    void updatePasswordFail2() {

        // given
        Member member = createMember();
        MemberUpdateRequest updateRequest = new MemberUpdateRequest("curPassword","changePassword", "changePassword2");

        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));

        // when
        assertThatThrownBy(()->memberService.changePassword(member.getId(), updateRequest))
                .isInstanceOf(BadRequestEntityException.class);

        verify(memberRepository).findById(member.getId());
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    @DisplayName("인증 성공")
    void AuthenticationSuccess() {

        // given
        Member member = createMember();

        given(memberRepository.findByEmail(member.getEmail()))
                .willReturn(Optional.of(member));

        // when
        MemberResponse result = memberService.validate(member.getEmail(), member.getPassword());

        // then
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
        assertThat(result.getId()).isEqualTo(member.getId());
        assertThat(result.getRole()).isEqualTo(member.getRole());
        verify(memberRepository).findByEmail(member.getEmail());
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    @DisplayName("인증 실패 - 존재하는 회원 없음")
    void AuthenticationFail1() {

        // given
        Member member = createMember();

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(()->memberService.validate(anyString(), member.getPassword()))
                .isInstanceOf(NotFoundEntityException.class);
        verify(memberRepository).findByEmail(anyString());
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    @DisplayName("인증 실패 - 비밀번호 불일치")
    void AuthenticationFail2() {

        // given
        Member member = createMember();

        given(memberRepository.findByEmail(member.getEmail()))
                .willReturn(Optional.of(member));
        //when & then
        assertThatThrownBy(()->memberService.validate(member.getEmail(), "1234Qwer!!"))
                .isInstanceOf(AuthenticationException.class);
        verify(memberRepository).findByEmail(member.getEmail());
        verifyNoMoreInteractions(memberRepository);
    }

    MemberCreateRequest createRequest() {
        return new MemberCreateRequest("ljw2109@naver.com", "curPassword", "curPassword");
    }

    Member createMember() {
        return new Member("ljw2109@naver.com", "curPassword", Role.REGULAR);
    }

}