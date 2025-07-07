package gift.member.controller;

import gift.domain.Member;
import gift.domain.Role;
import gift.jwt.JWTUtil;
import gift.member.dto.MemberCreateRequest;
import gift.member.dto.MemberLoginRequest;
import gift.member.dto.MemberResponse;
import gift.member.dto.MemberUpdateRequest;
import gift.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JWTUtil jwtUtil;

    private RestClient restClient;

    private RestClient loginRestClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port + "/api/members")
                .build();
        loginRestClient = RestClient.builder()
                .baseUrl("http://localhost:" + port + "/api/members/login")
                .build();
    }

    @AfterEach
    void clear() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 가입 성공")
    void joinMemberSuccess() {
        MemberCreateRequest memberCreateRequest = memberCreateRequest();

        ResponseEntity<Void> response = restClient.post()
                .body(memberCreateRequest)
                .retrieve()
                .toEntity(Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("회원 가입 실패 - 비밀번호에 특수문자 없음")
    void joinMemberFail() {
        MemberCreateRequest wrongRequest = new MemberCreateRequest("ljw2109@naver.com", "Qwer123456", "Qwer123456");

        assertThatThrownBy(()->restClient.post()
                .body(wrongRequest)
                .retrieve()
                .toEntity(Void.class))
                .isInstanceOf(HttpClientErrorException.BadRequest.class);
    }

    @Test
    @DisplayName("회원 가입 실패 - 이메일 형식에 맞지 않음")
    void joinMemberFail2() {
        MemberCreateRequest wrongRequest = new MemberCreateRequest("ljw2109", "Qwer1234!!", "Qwer1234!!");

        assertThatThrownBy(()->restClient.post()
                .body(wrongRequest)
                .retrieve()
                .toEntity(Void.class))
                .isInstanceOf(HttpClientErrorException.BadRequest.class);
    }

    @Test
    @DisplayName("회원 가입 실패 - 비밀번호가 확인 비밀번호와 서로 다름")
    void joinMemberFail3() {
        MemberCreateRequest wrongRequest = new MemberCreateRequest("ljw2109@naver.com", "Qwer1234!!!", "Qwer1234!!");

        assertThatThrownBy(()->restClient.post()
                .body(wrongRequest)
                .retrieve()
                .toEntity(Void.class))
                .isInstanceOf(HttpClientErrorException.BadRequest.class);
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() {

        memberRepository.save(createMember());

        ResponseEntity<Void> response = loginRestClient.post()
                .body(new MemberLoginRequest("ljw2109@naver.com", "Qwer1234!!"))
                .retrieve()
                .toEntity(Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 틀림")
    void loginFail() {
        memberRepository.save(createMember());

        assertThatThrownBy(()->loginRestClient.post()
                .body(new MemberLoginRequest("ljw2109@naver.com", "Qwer1235!!"))
                .retrieve()
                .toEntity(Void.class))
                .isInstanceOf(HttpClientErrorException.Forbidden.class)
                .hasMessageContaining("비밀번호가 다릅니다.");
    }

    @Test
    @DisplayName("로그인 실패 - 아이디 틀림")
    void loginFail2() {
        memberRepository.save(createMember());

        assertThatThrownBy(()->loginRestClient.post()
                .body(new MemberLoginRequest("ljw2108@naver.com", "Qwer1234!!"))
                .retrieve()
                .toEntity(Void.class))
                .isInstanceOf(HttpClientErrorException.Forbidden.class)
                .hasMessageContaining("존재하는 회원이 아닙니다");
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void withDrawSuccess() {
        Member member = createMember();
        Member savedMember = memberRepository.save(member);

        ResponseEntity<Void> response = restClient.delete()
                .cookie("Authorization",createJWT(savedMember))
                .retrieve()
                .toEntity(Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("회원 탈퇴 실패 - 존재하지 않는 회원")
    void withDrawFail() {
        String jwt = jwtUtil.createJWT("unkhown@email.com", Role.REGULAR.toString(),  1000 * 60L);
        assertThatThrownBy(()->restClient.delete()
                .cookie("Authorization", jwt)
                .retrieve()
                .toEntity(Void.class)
        ).isInstanceOf(HttpClientErrorException.Unauthorized.class);
    }

    @Test
    @DisplayName("회원 비밀번호 변경 성공")
    void changePasswordSuccess() {
        Member member = createMember();
        Member savedMember = memberRepository.save(member);
        MemberUpdateRequest updateRequest =
                new MemberUpdateRequest(savedMember.getPassword(), "Qwer12345!!", "Qwer12345!!");

        ResponseEntity<Void> response = restClient.patch()
                .body(updateRequest)
                .cookie("Authorization",createJWT(savedMember))
                .retrieve()
                .toEntity(Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("회원 비밀 번호 변경 실패 - 존재하지 않는 회원")
    void changePasswordFail() {
        MemberUpdateRequest updateRequest =
                new MemberUpdateRequest("Qwer1234!!", "Qwer12345!!", "Qwer12345!!");

        String jwt = jwtUtil.createJWT("unkhown@email.com", Role.REGULAR.toString(),  1000 * 60L);

        assertThatThrownBy(()->restClient.patch()
                .cookie("Authorization",jwt)
                .body(updateRequest)
                .retrieve()
                .toEntity(Void.class)
        ).isInstanceOf(HttpClientErrorException.Unauthorized.class);
    }

    private String createJWT(Member member) {
        return jwtUtil.createJWT(member.getEmail(), member.getRole().toString(), 1000 * 60L);
    }

    private Member createMember() {
        return new Member("ljw2109@naver.com", "Qwer1234!!", Role.REGULAR);
    }

    private MemberCreateRequest memberCreateRequest() {
        return new MemberCreateRequest("ljw2109@naver.com", "Qwer1234!!", "Qwer1234!!");
    }
}