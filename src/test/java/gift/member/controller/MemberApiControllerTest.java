package gift.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.member.dto.MemberLoginRequest;
import gift.member.dto.MemberRegisterRequest;
import gift.member.dto.MemberTokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MemberApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String testEmail;
    private String testPassword;

    @BeforeEach
    void setUp() {
        testEmail = "test" + System.currentTimeMillis() + "@test.com";
        testPassword = "12345678";
    }

    @Test
    @DisplayName("회원가입 후 로그인 - 200")
    void registerAndLogin() throws Exception{
        MemberRegisterRequest registerRequest = new MemberRegisterRequest(testEmail, testPassword);

        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isString());

        MemberLoginRequest loginRequest = new MemberLoginRequest(testEmail, testPassword);

        mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString());
    }

    @Test
    @DisplayName("로그인 후 product list 조회 - 200")
    void loginAndAccessResource() throws Exception {
        MemberRegisterRequest registerRequest = new MemberRegisterRequest(testEmail, testPassword);

        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isString());

        MemberLoginRequest loginRequest = new MemberLoginRequest(testEmail, testPassword);

        MvcResult loginResult = mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = loginResult.getResponse().getContentAsString();
        String token = objectMapper.readValue(jsonResponse, MemberTokenResponse.class).token();

        mockMvc.perform(get("/api/products")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("토큰 없이 product list 조회 - 401")
    void accessResourceWithoutToken() throws Exception{
        mockMvc.perform(get("/admin/products"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 product list 조회 - 401")
    void accessResourceWithNotValidToken() throws Exception{
        mockMvc.perform(get("/admin/products")
                .header("Authentication", "Bearer test"))
                .andExpect(status().isUnauthorized());
    }
}
