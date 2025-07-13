package gift.wishlist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.TestConfig;
import gift.dto.MemberLoginRequestDto;
import gift.dto.WishListCreateRequestDto;
import gift.dto.WishListUpdateRequestDto;
import gift.security.JwtProvider;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WishListTest extends TestConfig {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JwtProvider jwtProvider;

  @Autowired
  private ObjectMapper objectMapper;

  private String token;
  private Long memberId;

  @BeforeEach
  void setUp() throws Exception {
    MemberLoginRequestDto signUpDto = new MemberLoginRequestDto("test@email.com", "1234");

    mockMvc.perform(post("/api/members/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signUpDto)))
        .andExpect(status().isCreated());

    MvcResult loginResult = mockMvc.perform(post("/api/members/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signUpDto)))
        .andExpect(status().isOk())
        .andReturn();

    String responseBody = loginResult.getResponse().getContentAsString();
    token = objectMapper.readTree(responseBody).get("token").asText();

    memberId = jwtProvider.getMemberId(token);
  }

  @AfterEach
  void tearDown() throws Exception {
    if (memberId != null) {
      mockMvc.perform(delete("/api/wishlist/all")
              .header("Authorization", "Bearer " + token));

      mockMvc.perform(post("/admin/members/" + memberId + "/delete"));
    }
  }


  @Test
  void 유효한_토큰_으로_접근시_200() throws Exception {
    mockMvc.perform(get("/api/wishlist")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());
  }

  @Test
  void 토큰_변조시_접근_불허_하는지_401() throws Exception {
    String invalidToken = "x" + token.substring(1);

    mockMvc.perform(get("/api/wishlist")
            .header("Authorization", "Bearer " + invalidToken))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void 존재하지_않는_상품_추가_시도시_404() throws Exception {
    WishListCreateRequestDto requestDto = new WishListCreateRequestDto(99999L, 1);

    mockMvc.perform(post("/api/wishlist")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isNotFound());
  }

  @Test
  void 상품_10개_추가후_20개_추가시_30개가_되나() throws Exception {
    Long productId = 1L;

    WishListCreateRequestDto add10 = new WishListCreateRequestDto(productId, 10);
    mockMvc.perform(post("/api/wishlist")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(add10)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.productId").value(productId))
        .andExpect(jsonPath("$.quantity").value(10));

    WishListCreateRequestDto add20 = new WishListCreateRequestDto(productId, 20);
    mockMvc.perform(post("/api/wishlist")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(add20)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.productId").value(productId))
        .andExpect(jsonPath("$.quantity").value(30));

    MvcResult result = mockMvc.perform(get("/api/wishlist")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andReturn();

    String responseBody = result.getResponse().getContentAsString();
    JsonNode root = objectMapper.readTree(responseBody);

    boolean found = false;
    for (JsonNode node : root) {
      if (node.get("productId").asLong() == productId) {
        int quantity = node.get("quantity").asInt();
        assertEquals(30, quantity, "상품 수량이 누적되어 30개여야 합니다.");
        found = true;
        break;
      }
    }
    assertTrue(found, "위시리스트에서 상품 ID 1을 찾을 수 있어야 합니다.");
  }

  @Test
  void 상품_추가한뒤_수정시_더해지는게_아니라_해당_수정개수로_적용되는지() throws Exception {
    Long productId = 1L;

    WishListCreateRequestDto add10 = new WishListCreateRequestDto(productId, 10);
    mockMvc.perform(post("/api/wishlist")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(add10)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.productId").value(productId))
        .andExpect(jsonPath("$.quantity").value(10));

    WishListUpdateRequestDto update777 = new WishListUpdateRequestDto(productId, 777);
    mockMvc.perform(patch("/api/wishlist")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(update777)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.productId").value(productId))
        .andExpect(jsonPath("$.quantity").value(777));

    MvcResult result = mockMvc.perform(get("/api/wishlist")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andReturn();

    String responseBody = result.getResponse().getContentAsString();
    JsonNode root = objectMapper.readTree(responseBody);

    boolean found = false;
    for (JsonNode node : root) {
      if (node.get("productId").asLong() == productId) {
        int quantity = node.get("quantity").asInt();
        assertEquals(777, quantity, "상품 수량이 777로 반영되어야 합니다.");
        found = true;
        break;
      }
    }
    assertTrue(found, "위시리스트에서 상품 ID 1을 찾을 수 있어야 합니다.");
  }

  @Test
  void 상품_추가한뒤_0개로_수정시_삭제처리_되는지() throws Exception {
    Long productId = 1L;

    WishListCreateRequestDto add10 = new WishListCreateRequestDto(productId, 10);
    mockMvc.perform(post("/api/wishlist")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(add10)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.productId").value(productId))
        .andExpect(jsonPath("$.quantity").value(10));

    WishListUpdateRequestDto updateZero = new WishListUpdateRequestDto(productId, 0);
    mockMvc.perform(patch("/api/wishlist")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateZero)))
        .andExpect(status().isOk());

    MvcResult result = mockMvc.perform(get("/api/wishlist")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andReturn();

    String responseBody = result.getResponse().getContentAsString();
    JsonNode root = objectMapper.readTree(responseBody);

    boolean found = false;
    for (JsonNode node : root) {
      if (node.get("productId").asLong() == productId) {
        found = true;
        break;
      }
    }
    assertFalse(found, "수량 0으로 수정 시 위시리스트에서 해당 상품이 삭제되어야 합니다.");
  }
}
