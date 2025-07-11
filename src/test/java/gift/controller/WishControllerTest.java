package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.PageRequestDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Role;
import gift.entity.Wish;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WishControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired WishRepository wishRepository;
    @Autowired ProductRepository productRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ObjectMapper objectMapper;

    private Long memberId;
    private Long productId;

    @BeforeEach
    void setUp() {
        wishRepository.findAllWishByMemberId(1L)
                .forEach(w -> wishRepository.deleteWishById(w.getId()));

        memberRepository.findAllMembers()
                .forEach(m -> memberRepository.deleteMember(m.getId()));

        productRepository.findAllProducts(new PageRequestDto(0, Integer.MAX_VALUE))
                .content()
                .forEach(p -> productRepository.deleteProduct(p.getId()));

        Member savedMember = memberRepository.saveMember(
                new Member(null, "솨야", "wish@test.com", "pw", Role.USER)
        );
        Product savedProduct = productRepository.createProduct(
                new Product(null, "하리보 젤리", 1500, "http://img.url/test.png")
        );

        memberId = savedMember.getId();
        productId = savedProduct.getId();
    }

    @Test
    @DisplayName("위시를 추가하면, 해당 상품 정보가 담긴 응답을 반환한다. ")
    void shouldAddWish() throws Exception {
        mockMvc.perform(post("/api/wishes")
                        .param("memberId", memberId.toString())
                        .param("productId", productId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.productName").value("하리보 젤리"));
    }

    @Test
    @DisplayName("회원 ID로 위시 목록을 조회하면, 해당 회원의 위시 목록을 반환한다. ")
    void shouldGetWishes() throws Exception {
        wishRepository.createWish(new Wish(null, memberId, productId));

        mockMvc.perform(get("/api/wishes")
                        .param("memberId", memberId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].productName").value("하리보 젤리"));
    }

    @Test
    @DisplayName("위시 ID로 삭제 요청하면, 204(No Content)를 반환한다. ")
    void shouldDeleteWish() throws Exception {
        var savedWish = wishRepository.createWish(new Wish(memberId, productId));

        mockMvc.perform(delete("/api/wishes/" + savedWish.getId()))
                .andExpect(status().isNoContent());
    }
}
