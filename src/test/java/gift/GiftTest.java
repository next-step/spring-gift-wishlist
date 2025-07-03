package gift;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.ProductAdminRequestDto;
import gift.dto.ProductRequestDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GiftTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private Long createdProductId;

  @BeforeEach
  void 초기_상품_등록() throws Exception {
    ProductRequestDto dto = new ProductRequestDto("테스트상품", 1000, "test.png");
    String json = objectMapper.writeValueAsString(dto);

    MvcResult result = mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated())
        .andReturn();

    String location = result.getResponse().getHeader("Location");
    createdProductId = Long.valueOf(location.substring(location.lastIndexOf("/") + 1));
  }

  @AfterEach
  void 테스트_종료_모든_기록_삭제() throws Exception {
    if (createdProductId != null) {
      mockMvc.perform(delete("/api/products/" + createdProductId))
          .andExpect(status().isNoContent());
    }
  }

  @Test
  void 존재하는_상품_단건_조회_시_200() throws Exception {
    mockMvc.perform(get("/api/products/" + createdProductId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(createdProductId))
        .andExpect(jsonPath("$.name").value("테스트상품"));
  }

  @Test
  void 상품_수정() throws Exception {
    ProductRequestDto updatedDto = new ProductRequestDto("수정상품", 2000, "update.png");
    String json = objectMapper.writeValueAsString(updatedDto);

    mockMvc.perform(put("/api/products/" + createdProductId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("수정상품"));
  }

  @Test
  void 존재하지_않는_상품_조회_시_404() throws Exception {
    mockMvc.perform(get("/api/products/9999999"))
        .andExpect(status().isNotFound());
  }

  @Test
  void 글자수_15자를_넘는_상품_이름_생성_요청시_400() throws Exception {
    ProductRequestDto invalidDto = new ProductRequestDto("가나다라마바사아자차카타파하가나다", 1000, "url");
    String json = objectMapper.writeValueAsString(invalidDto);

    mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void 허용_되는_특수문자_로만_구성된_상품_이름_생성_요청시_201() throws Exception {
    ProductRequestDto invalidDto = new ProductRequestDto("()[]+-&/_", 1000, "t.png");
    String json = objectMapper.writeValueAsString(invalidDto);

    mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated());
  }

  @Test
  void 허용_되지_않는_특수문자_포함_상품_이름_생성_요청시_400() throws Exception {
    ProductRequestDto invalidDto = new ProductRequestDto("()[]+-&/_;", 1000, "t.png");
    String json = objectMapper.writeValueAsString(invalidDto);

    mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void 가격이_음수인_상품_생성_요청시_400() throws Exception {
    ProductRequestDto invalidDto = new ProductRequestDto("사탕", -1, "candy.png");
    String json = objectMapper.writeValueAsString(invalidDto);

    mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void 관리자_API_카카오_상품인데_MD_협의_안된_경우_400() throws Exception {
    ProductAdminRequestDto kakaoDto = new ProductAdminRequestDto("카카오프렌즈", 3000, "kakaofriends.png", false);
    String json = objectMapper.writeValueAsString(kakaoDto);

    mockMvc.perform(post("/admin/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void 관리자_API_카카오_상품인데_MD_협의_된_경우_201() throws Exception {
    ProductAdminRequestDto kakaoDto = new ProductAdminRequestDto("카카오프렌즈", 3000, "kakaofriends.png", true);
    String json = objectMapper.writeValueAsString(kakaoDto);

    mockMvc.perform(post("/admin/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated());
  }

  @Test
  void 일반_API_카카오_상품_생성_시도시_400() throws Exception {
    ProductRequestDto kakaoDto = new ProductRequestDto("카카오프렌즈", 3000, "kakaofriends.png");
    String json = objectMapper.writeValueAsString(kakaoDto);

    mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());
  }
}
