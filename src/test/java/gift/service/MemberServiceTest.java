package gift.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import gift.domain.Member;
import gift.dto.AdminMemberResponse;
import gift.dto.LoginRequest;
import gift.dto.LoginResponse;
import gift.dto.RegisterRequest;
import gift.dto.UpdateMemberRequest;
import gift.exception.LoginException;
import gift.exception.MemberNotFoundException;
import gift.exception.RegisterException;
import gift.repository.MemberRepository;
import gift.util.TokenProvider;

public class MemberServiceTest {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final TokenProvider tokenProvider = mock(TokenProvider.class);

    private final MemberService memberService = new MemberService(
        memberRepository,
        passwordEncoder,
        tokenProvider
    );

    @Test
    void signupTest() {
        // given
        RegisterRequest request = new RegisterRequest("test@test.com", "1234123!");
        Member savedMember = new Member(1L, "test@test.com", "dbPassword", "ROLE_USER");
        String token = "validJwt";
        given(passwordEncoder.encode("1234123!")).willReturn("dbPassword");
        given(memberRepository.existsByEmail("test@test.com")).willReturn(false);
        given(memberRepository.save(any())).willReturn(1L);
        given(memberRepository.findById(1L)).willReturn(Optional.of(savedMember));
        given(tokenProvider.createToken(savedMember)).willReturn(token);

        // when
        LoginResponse response = memberService.signup(request);

        // then
        assertThat(response.token()).isEqualTo("Bearer " + token);
    }

    @Test
    void signupFailTest() {
        // given
        RegisterRequest request = new RegisterRequest("test@test.com", "1234123!");
        given(memberRepository.existsByEmail("test@test.com")).willReturn(true);

        // when, then
        assertThatThrownBy(() -> memberService.signup(request))
            .isInstanceOf(RegisterException.class);
    }

    @Test
    void signinTest() {
        // given
        LoginRequest request = new LoginRequest("test@test.com", "1234123!");
        Member member = Member.createMemberWithEmailAndPassword(
            "test@test.com", "dbPassword"
        );
        String token = "validJwt";
        given(passwordEncoder.matches("1234123!", "dbPassword")).willReturn(true);
        given(memberRepository.findByEmail("test@test.com")).willReturn(Optional.of(member));
        given(tokenProvider.createToken(member)).willReturn(token);

        // when
        LoginResponse response = memberService.signin(request);

        // then
        assertThat(response.token()).isEqualTo("Bearer " + token);
    }

    @Test
    void signinFailTest() {
        // given
        LoginRequest request = new LoginRequest("test@test.com", "wrongPassword");
        Member member = Member.createMemberWithEmailAndPassword(
            "test@test.com", "dbPassword"
        );
        given(memberRepository.findByEmail("test@test.com")).willReturn(Optional.of(member));
        given(passwordEncoder.matches("wrongPassword", "dbPassword")).willReturn(false);

        // when, then
        assertThatThrownBy(() -> memberService.signin(request))
            .isInstanceOf(LoginException.class);
    }

    @Test
    void findAllTest() {
        // given
        given(memberRepository.findAll()).willReturn(List.of(
            new Member(1L, "member1@test.com", "dbPassword1", "ROLE_USER"),
            new Member(2L, "member2@test.com", "dbPassword2!", "ROLE_USER")
        ));

        // when
        List<AdminMemberResponse> response = memberService.findAll();

        // then
        assertThat(response).hasSize(2);
        assertThat(response.get(0).id()).isEqualTo(1L);
        assertThat(response.get(0).email()).isEqualTo("member1@test.com");
        assertThat(response.get(1).id()).isEqualTo(2L);
        assertThat(response.get(1).email()).isEqualTo("member2@test.com");
    }

    @Test
    void findByIdTest() {
        // given
        Long memberId = 1L;
        Member member = new Member(memberId, "test@test.com", "dbPassword", "ROLE_USER");
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // when
        AdminMemberResponse response = memberService.findById(memberId);

        // then
        assertThat(response.id()).isEqualTo(memberId);
        assertThat(response.email()).isEqualTo("test@test.com");
    }

    @Test
    void findByIdFailTest() {
        // given
        Long failId = 999L;
        given(memberRepository.findById(failId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> memberService.findById(failId))
            .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void updateTest() {
        // given
        Long memberId = 1L;
        UpdateMemberRequest request = new UpdateMemberRequest("test2@test.com");
        given(memberRepository.existsById(memberId)).willReturn(true);
        given(memberRepository.update(any())).willReturn(1);

        // when, then
        assertThatCode(() -> memberService.update(memberId, request)).doesNotThrowAnyException();
    }

    @Test
    void updateFailTest() {
        // given
        Long failId = 999L;
        UpdateMemberRequest request = new UpdateMemberRequest("updated@example.com");
        given(memberRepository.existsById(failId)).willReturn(false);

        // when, then
        assertThatThrownBy(() -> memberService.update(failId, request))
            .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void deleteTest() {
        // given
        Long memberId = 1L;
        given(memberRepository.existsById(memberId)).willReturn(true);
        given(memberRepository.delete(memberId)).willReturn(1);

        // when, then
        assertThatCode(() -> memberService.delete(memberId)).doesNotThrowAnyException();
    }

    @Test
    void deleteFailTest() {
        // given
        Long failId = 999L;
        given(memberRepository.existsById(failId)).willReturn(false);

        // when, then
        assertThatThrownBy(() -> memberService.delete(failId))
            .isInstanceOf(MemberNotFoundException.class);
    }
}
