package gift;

import gift.entity.Member;
import gift.entity.Role;
import gift.repository.MemberRepository;
import gift.token.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/clear_member_table.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AdminPageTokenTest {

    private final String baseUrl = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private RestClient restClient;
    private String adminToken;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create();

        // 관리자 계정을 데이터베이스에 직접 생성
        String encodedPassword = passwordEncoder.encode("adminpassword123456789");
        Member admin = new Member(1L, "admin@example.com", encodedPassword, Role.ROLE_MD);
        memberRepository.createMember(admin);

        // 관리자용 JWT 토큰 생성
        adminToken = jwtTokenProvider.createToken("admin@example.com");
    }

    @Test
    void 관리자_권한으로_상품목록_조회_성공() throws Exception {
        String url = baseUrl + port + "/admin/products";

        ResponseEntity<String> response = restClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + adminToken)
                .retrieve()
                .toEntity(String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void 권한_없이_관리자_페이지_접근_시_401() throws Exception {
        String url = baseUrl + port + "/admin/products";

        assertThatExceptionOfType(HttpClientErrorException.class)
                .isThrownBy(() -> restClient.get()
                        .uri(url)
                        .retrieve()
                        .toBodilessEntity())
                .satisfies(ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED));
    }

    @Test
    void 일반_사용자_권한으로_관리자_페이지_접근_시_403() throws Exception {
        // 일반 사용자 계정 생성
        String userPassword = passwordEncoder.encode("userpassword123456789");
        Member user = new Member(2L, "user@example.com", userPassword, Role.ROLE_USER);
        memberRepository.createMember(user);

        // 일반 사용자 토큰 생성
        String userToken = jwtTokenProvider.createToken("user@example.com");
        String url = baseUrl + port + "/admin/products";

        assertThatExceptionOfType(HttpClientErrorException.class)
                .isThrownBy(() -> restClient.get()
                        .uri(url)
                        .header("Authorization", "Bearer " + userToken)
                        .retrieve()
                        .toBodilessEntity())
                .satisfies(ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED));
    }
}