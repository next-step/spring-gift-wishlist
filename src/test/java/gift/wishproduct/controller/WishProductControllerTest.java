package gift.wishproduct.controller;


import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Role;
import gift.jwt.JWTUtil;
import gift.member.repository.MemberRepository;
import gift.product.repository.ProductRepository;
import gift.wishproduct.dto.WishProductCreateReq;
import gift.wishproduct.repository.WishProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WishProductControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WishProductRepository wishProductRepository;

    @Autowired
    private JWTUtil jwtUtil;

    private final UUID memberId = UUID.randomUUID();
    private final UUID productId = UUID.randomUUID();

    RestClient restClient;


    @BeforeEach
    void setUp() {
        Member member = memberRepository.save(new Member(memberId,"ljw2109@naver.com", "Qwer1234!!", Role.REGULAR));
        productRepository.save(new Product(productId,"스윙칩", 3000, "data:image/~base64",memberId));

        String token = jwtUtil
                .createJWT(member.getEmail(), member.getRole().toString(), 1000 * 60L);

        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port + "/api/wish-products")
                .defaultCookie("Authorization", token)
                .build();
    }

    @AfterEach
    void clear() {
        memberRepository.deleteAll();
        productRepository.deleteAll();
        wishProductRepository.deleteAll();
    }

    @Test
    @DisplayName("위시리스트 추가 성공")
    void addWishProductSuccess() {

        WishProductCreateReq dto = new WishProductCreateReq(productId, 10);

        ResponseEntity<Void> response = restClient.post()
                .body(dto)
                .retrieve()
                .toEntity(Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().get("location")).isNotNull();
    }


}