package gift.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
class AdminItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("상품 등록 성공 테스트")
    void createItem_Success() throws Exception {
        mockMvc.perform(post("/admin/items")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "테스트 성공 상품")
                .param("price", "12000")
                .param("imageUrl", "success.jpg"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/items"));
    }

    @Test
    @DisplayName("상품 등록 실패 테스트 - 유효성 검사")
    void createItem_Fail_Validation() throws Exception {
        mockMvc.perform(post("/admin/items")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "카카오 상품")
                .param("price", "10000")
                .param("imageUrl", "fail.jpg"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/items/form"))
            .andExpect(model().hasErrors());
    }
}