package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.config.JwtUtil;
import gift.dto.MemberRequest;
import gift.dto.TokenResponse;
import gift.entity.Member;
import gift.repository.MemberRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerNormalCaseTokenResponse() {
        MemberRequest request = new MemberRequest(
            "test@test.com",
            "password123",
            "USER"
        );
        Member savedMember = new Member(
            1L,
            "test@test.com",
            "password123",
            "USER"
        );
        when(memberRepository.findAll()).thenReturn(Collections.emptyList());
        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);
        when(jwtUtil.generateToken(savedMember)).thenReturn("mocked-token");

        TokenResponse response = memberService.register(request);

        assertThat(response.token()).isEqualTo("mocked-token");
        verify(memberRepository).save(any(Member.class));
        verify(jwtUtil).generateToken(savedMember);
    }

    @Test
    void registerDuplicateCaseException() {
        MemberRequest request = new MemberRequest(
            "test@test.com",
            "password123",
            "USER"
        );
        Member existingMember = new Member(
            1L,
            "test@test.com",
            "password123",
            "USER"
        );
        when(memberRepository.findAll()).thenReturn(List.of(existingMember));

        assertThatThrownBy(() -> memberService.register(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미 존재하는 이메일입니다.");
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void loginNormalCaseTokenResponse() {
        MemberRequest request = new MemberRequest(
            "test@test.com",
            "password123",
            "USER"
        );
        Member member = new Member(
            1L,
            "test@test.com",
            "password123",
            "USER"
        );
        when(memberRepository.findAll()).thenReturn(List.of(member));
        when(jwtUtil.generateToken(member)).thenReturn("mocked-token");

        TokenResponse response = memberService.login(request);

        assertThat(response.token()).isEqualTo("mocked-token");
        verify(jwtUtil).generateToken(member);
    }

    @Test
    void loginWrongEmailException() {
        MemberRequest request = new MemberRequest(
            "notexisting@test.com",
            "password123",
            "USER"
        );
        when(memberRepository.findAll()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> memberService.login(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이메일이 올바르지 않습니다.");
    }

    @Test
    void loginWrongPasswordException() {
        MemberRequest request = new MemberRequest(
            "test@test.com",
            "wrong123",
            "USER"
        );
        Member member = new Member(
            1L,
            "test@test.com",
            "password123",
            "USER"
        );
        when(memberRepository.findAll()).thenReturn(List.of(member));

        assertThatThrownBy(() -> memberService.login(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("비밀번호가 올바르지 않습니다.");
    }

}
