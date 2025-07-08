package gift;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthenticationFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("토큰 없이 요청 시 401UNAUTHORIZED 에러가 발생한다")
    @Test
    void 토큰없이_요청_시_401_에러발생() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("유효하지 않은 토큰으로 요청 시 401 반환")
    @Test
    void 유효하지_않은_토큰_401_에러발생() throws Exception {
        mockMvc.perform(get("/api/products")
                        .header("Authorization", "Bearer invalid.jwt.token"))
                .andExpect(status().isUnauthorized());
    }
}

