package gift;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.entity.ApprovedProduct;
import gift.repository.ApprovedProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class ProductViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApprovedProductRepository approvedProductRepository;

    @Test
    @DisplayName("상품 등록 성공 - '카카오' 포함된 승인된 상품명")
    void createProduct_success_withApprovedName() throws Exception {

        // '카카오' 포함된 승인된 상품명 추가
        approvedProductRepository.save(new ApprovedProduct("카카오 프렌즈 볼펜"));

        // when & then
        mockMvc.perform(post("/admin/products/new")
                .param("name", "카카오 프렌즈 볼펜")
                .param("price", "15000")
                .param("imageUrl", "https://image.com/item.jpg")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/admin/products"));
    }

}
