package gift.controller.api;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.ItemRequest;
import gift.entity.Item;
import gift.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ItemRepository itemRepository;

    private Item testItem1;

    @BeforeEach
    void setUp() {
        testItem1 = itemRepository.save(new Item(null, "API 테스트 상품", 1000, "api.jpg"));
        itemRepository.save(new Item(null, "두 번째 상품", 2000, "api2.jpg"));
    }

    @Test
    @DisplayName("API - 전체 상품 목록 조회 테스트")
    void getAllItems() throws Exception {
        mockMvc.perform(get("/api/products")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("API - ID로 상품 조회 성공 테스트")
    void getItemById_Success() throws Exception {
        mockMvc.perform(get("/api/products/" + testItem1.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(testItem1.getId()))
            .andExpect(jsonPath("$.name").value("API 테스트 상품"));
    }

    @Test
    @DisplayName("API - 상품 등록 성공 테스트")
    void createItem_Success() throws Exception {
        ItemRequest itemRequest = new ItemRequest("새 API 상품", 5000, "new_api.jpg");
        String requestBody = objectMapper.writeValueAsString(itemRequest);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("API - 상품 수정 성공 테스트")
    void updateItem_Success() throws Exception {
        ItemRequest itemRequest = new ItemRequest("수정된 API 상품", 1500, "updated.jpg");
        String requestBody = objectMapper.writeValueAsString(itemRequest);

        mockMvc.perform(put("/api/products/" + testItem1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("수정된 API 상품"));
    }

    @Test
    @DisplayName("API - 상품 삭제 성공 테스트")
    void deleteItem_Success() throws Exception {
        mockMvc.perform(delete("/api/products/" + testItem1.getId()))
            .andExpect(status().isNoContent());
    }
}