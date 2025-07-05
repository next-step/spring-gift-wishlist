package gift.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.LoginRequestDto;
import gift.dto.RegisterRequestDto;
import gift.dto.TokenResponse;
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

    @Test
    @DisplayName("회원가입 API 호출 시 201 Created와 토큰을 반환한다")
    void register_api_test() throws Exception {
        RegisterRequestDto request = new RegisterRequestDto();
        request.setEmail("test@email.com");
        request.setPassword("password");

        mockMvc.perform(post("/api/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()) // 201 Created
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void login_api_test() throws Exception {

        RegisterRequestDto registerRequest = new RegisterRequestDto();
        registerRequest.setEmail("test@email.com");
        registerRequest.setPassword("password");
        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));

        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setEmail("test@email.com");
        loginRequest.setPassword("password");

        mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void login_api_fail_wrong_password() throws Exception {
        RegisterRequestDto registerRequest = new RegisterRequestDto();
        registerRequest.setEmail("test@email.com");
        registerRequest.setPassword("password");
        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setEmail("test@email.com");
        loginRequest.setPassword("wrong_password");
        mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isForbidden()); // 403 Forbidden
    }

    @Test
    void protected_api_without_token() throws Exception {
        mockMvc.perform(get("/api/members/me"))
                .andExpect(status().isUnauthorized()); // 401 Unauthorized
    }

    @Test
    void protected_api_with_token() throws Exception {
        RegisterRequestDto registerRequest = new RegisterRequestDto();
        registerRequest.setEmail("test@email.com");
        registerRequest.setPassword("password");

        MvcResult registerResult = mockMvc.perform(post("/api/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setEmail("test@email.com");
        loginRequest.setPassword("password");

        MvcResult loginResult = mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        TokenResponse tokenResponse = objectMapper.readValue(responseBody, TokenResponse.class);
        String token = tokenResponse.getToken();

        mockMvc.perform(get("/api/members/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists()) // id 필드가 있는지 확인
                .andExpect(jsonPath("$.email").value("test@email.com")); // email 값이 올바른지 확인
    }


}