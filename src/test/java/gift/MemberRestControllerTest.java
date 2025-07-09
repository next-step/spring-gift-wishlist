package gift;

import gift.Entity.Member;
import gift.Entity.Product;
import gift.dto.MemberDto;
import gift.dto.MemberRequest;
import gift.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
public class MemberRestControllerTest {

    @LocalServerPort
    private int port;

    private RestClient client = RestClient.builder().build();

    @Autowired
    private MemberDto memberDto;

    @BeforeEach
    void setupTestMember() {

        // 테스트용 계정 등록
        Member member = new Member("helloworld", "hello@kakao.com", "123456789", "테스트", "대한민국", "USER");
        memberDto.insertMember(member);
    }

    @Transactional
    @Test
    public void testRegisterMember() {
        var url = "http://localhost:" + port + "/api/register";
        var member = new Member("byeworld", "byeworld@kakao.com", "123456789", "안녕세상", "대한민국", "USER");

        var response = client.post()
                .uri(url)
                .body(member)
                .retrieve()
                .toEntity(Member.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo("byeworld");
    }


    @Test
    public void testLogin() {
        var url = "http://localhost:" + port + "/api/login";
        var req = new MemberRequest("helloworld", "123456789");

        var response = client.post()
                .uri(url)
                .body(req)
                .retrieve()
                .toEntity(String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


}
