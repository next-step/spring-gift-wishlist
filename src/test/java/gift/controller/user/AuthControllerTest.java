// src/test/java/gift/controller/user/AuthControllerTest.java
package gift.controller.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.member.AuthRequest;
import gift.dto.member.AuthResponse;
import gift.service.member.MemberService;
import gift.util.BasicAuthUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AuthController 단위 테스트 (Fixture 적용)")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private gift.util.JwtUtil jwtUtil;

    @Test
    @DisplayName("POST /api/members/register - 회원가입 성공 (201)")
    void registerSuccess() throws Exception {
        // 픽스처 제거: 직접 이메일/비밀번호 지정
        AuthRequest req = new AuthRequest("user@test.com", "pwd");
        AuthResponse resp = new AuthResponse("token123");

        Mockito.when(memberService.register(req)).thenReturn(resp);

        mockMvc.perform(post("/api/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("token123"));
    }


    @Test
    @DisplayName("POST /api/members/login - 로그인 성공 (200)")
    void loginSuccess() throws Exception {
        // 로그인 테스트에서는 Member 픽스처를 사용하지 않습니다.
        String basicHeader = "Basic dXNlcjp3b3Jk"; // 사용자:비밀번호 => user:word
        AuthResponse resp = new AuthResponse("jwt-token");

        // memberService.login 호출만 목(mock) 설정
        Mockito.when(memberService.login("user", "word")).thenReturn(resp);

        try (MockedStatic<BasicAuthUtil> util = Mockito.mockStatic(BasicAuthUtil.class)) {
            util.when(() -> BasicAuthUtil.parse(basicHeader))
                    .thenReturn(new BasicAuthUtil.Credentials("user", "word"));

            mockMvc.perform(post("/api/members/login")
                            .header("Authorization", basicHeader))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("jwt-token"));
        }
    }

    @Test
    @DisplayName("POST /api/members/login - 인증 실패 시 401")
    void loginFailureUnauthorized() throws Exception {
        String basicHeader = "Basic aW52YWxpZDppbnZhbGlk"; // invalid:invalid
        
        Mockito.when(memberService.login("invalid", "invalid"))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

        try (MockedStatic<BasicAuthUtil> util = Mockito.mockStatic(BasicAuthUtil.class)) {
            util.when(() -> BasicAuthUtil.parse(basicHeader))
                    .thenReturn(new BasicAuthUtil.Credentials("invalid", "invalid"));

            mockMvc.perform(post("/api/members/login")
                            .header("Authorization", basicHeader))
                    .andExpect(status().isUnauthorized());
        }
    }
}
