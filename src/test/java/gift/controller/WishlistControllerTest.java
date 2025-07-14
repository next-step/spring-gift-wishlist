package gift.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import gift.dto.AddWishlistRequest;
import gift.dto.WishResponse;
import gift.interceptor.MemberAuthInterceptor;
import gift.repository.MemberRepository;
import gift.service.WishlistService;
import gift.util.TokenProvider;

@WebMvcTest(WishlistController.class)
class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WishlistService wishlistService;

    @MockitoBean
    private TokenProvider tokenProvider;

    @MockitoBean
    private MemberAuthInterceptor memberAuthInterceptor;

    @MockitoBean
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() throws Exception {
        given(memberAuthInterceptor.preHandle(any(), any(), any())).willReturn(true);
        given(memberRepository.existsById(1L)).willReturn(true);
        given(tokenProvider.getMemberId(eq("validJwt"))).willReturn(1L);
    }

    @Test
    void getProductsFromWishlistTest() throws Exception {
        // given
        Long memberId = 1L;
        List<WishResponse> products = List.of(
            new WishResponse(1L, 1000L, "상품1", "image1.jpg", 1L),
            new WishResponse(2L, 2000L, "상품2", "image2.jpg", 1L)
        );
        given(wishlistService.getProductsFromWishlist(memberId)).willReturn(products);

        // when
        MockHttpServletResponse actual = mockMvc.perform(get("/api/wishes")
            .header("Authorization", "Bearer validJwt")).andReturn().getResponse();

        // then
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.OK.value());

        List<WishResponse> result = Arrays.asList(objectMapper.readValue(
            actual.getContentAsString(),
            WishResponse[].class
        ));
        assertThat(result).hasSize(2);
        assertThat(result.get(0).productId()).isEqualTo(1L);
        assertThat(result.get(0).name()).isEqualTo("상품1");
        assertThat(result.get(0).price()).isEqualTo(1000);
        assertThat(result.get(0).imageUrl()).isEqualTo("image1.jpg");
        assertThat(result.get(1).productId()).isEqualTo(2L);
        assertThat(result.get(1).name()).isEqualTo("상품2");
        assertThat(result.get(1).price()).isEqualTo(2000);
        assertThat(result.get(1).imageUrl()).isEqualTo("image2.jpg");
    }

    @Test
    void addProductToWishlistTest() throws Exception {
        // given
        Long memberId = 1L;
        AddWishlistRequest request = new AddWishlistRequest(1L);
        WishResponse response = new WishResponse(1L, 1000L, "상품1", "image1.jpg", 1L);
        given(wishlistService.addProductToWishlist(eq(memberId), any(AddWishlistRequest.class)))
            .willReturn(response);

        String content = objectMapper.writeValueAsString(request);

        // when
        MockHttpServletResponse actual = mockMvc.perform(post("/api/wishes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)
            .header("Authorization", "Bearer validJwt")).andReturn().getResponse();

        // then
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        WishResponse result = objectMapper.readValue(
            actual.getContentAsString(),
            WishResponse.class
        );
        assertThat(result.productId()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("상품1");
        assertThat(result.price()).isEqualTo(1000);
        assertThat(result.imageUrl()).isEqualTo("image1.jpg");
    }

    @Test
    void deleteProductFromWishlistTest() throws Exception {
        // given
        Long memberId = 1L;
        Long productId = 1L;
        willDoNothing().given(wishlistService).deleteProductFromWishlist(memberId, productId);

        // when
        var actual = mockMvc.perform(delete("/api/wishes/{productId}", productId)
            .header("Authorization", "Bearer validJwt")).andReturn().getResponse();

        // then
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
