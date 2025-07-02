package gift;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createProduct_NameTooLong_ShouldReturnValidationError() throws Exception {
        mockMvc.perform(post("/admin/products-add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "이것은15자를초과한상품이름입니다")
                        .param("price", "1000")
                        .param("imageUrl", "image.jpg"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("product", "name"))
                .andExpect(model().attributeHasFieldErrorCode("product", "name", "Size"))
                .andExpect(view().name("admin/product-add"));
    }

    @Test
    void createProduct_InvalidSpecialCharacters_ShouldReturnValidationError() throws Exception {
        mockMvc.perform(post("/admin/products-add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "잘못된@상품이름")
                        .param("price", "1000")
                        .param("imageUrl", "image.jpg"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("product", "name"))
                .andExpect(model().attributeHasFieldErrorCode("product", "name", "Pattern"))
                .andExpect(view().name("admin/product-add"));
    }

    @Test
    void createProduct_KakaoWithoutApproval_ShouldReturnMdApprovalError() throws Exception {
        mockMvc.perform(post("/admin/products-add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "카카오 선물세트")
                        .param("price", "1000")
                        .param("imageUrl", "image.jpg"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("product", "name"))
                .andExpect(model().attributeHasFieldErrorCode("product", "name", "md.not.approved"))
                .andExpect(view().name("admin/product-add"));
    }

    @Test
    void editProduct_KakaoWithApproval_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/admin/4")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "카카오 선물세트")
                        .param("price", "1000")
                        .param("imageUrl", "image.jpg"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin"));
    }
}
