package gift.member.controller;

import gift.domain.Member;
import gift.member.dto.MemberCreateRequest;
import gift.member.dto.MemberResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
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

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port + "/api/members")
                .build();
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
                .isInstanceOf(HttpClientErrorException.Conflict.class);
    }

    private MemberCreateRequest memberCreateRequest() {
        return new MemberCreateRequest("ljw2109@naver.com", "Qwer1234!!", "Qwer1234!!");
    }
}