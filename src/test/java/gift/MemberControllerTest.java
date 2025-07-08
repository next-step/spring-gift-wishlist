package gift;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.controller.MemberController;
import gift.dto.request.MemberRequest;
import gift.exception.DuplicateMemberException;
import gift.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("유효한 이메일과 비밀번호로 회원가입에 성공한다")
    @Test
    void 회원가입_성공() throws Exception{
        var response = mockMvc.perform(
                post("/api/members/register")
                        .contentType("application/json")
                        .content("""
                            {
                                "email": "test@email.com",
                                "pwd": "abc1234"
                            }
                        """)
            ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).contains("회원가입이 완료되었습니다.");
    }

    @DisplayName("이미 가입된 이메일로 회원가입 시도 시 409CONFLICT 에러가 발생한다")
    @Test
    void 회원가입_중복_실패() throws Exception{
        willThrow(new DuplicateMemberException())
                .given(memberService).register(any(),any());

        var response = mockMvc.perform(post("/api/members/register")
                .contentType("application/json")
                .content("""
                     {
                        "email": "dup@email.com",
                        "pwd": "abc1234"
                     }
                """))
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @DisplayName("로그인 성공 시 토큰을 반환한다")
    @Test
    void 로그인_성공() throws Exception{
        MemberRequest request = new MemberRequest("test@email.com", "abc1234");
        when(memberService.login(request.email(), request.pwd()))
                .thenReturn("fake.jwt.token");

        mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake.jwt.token"));
    }

    @DisplayName("잘못된 비밀번호로 로그인 시 403Forbidden 에러가 발생한다")
    @Test
    void 로그인_실패_비밀번호_불일치() throws Exception {
        MemberRequest request = new MemberRequest("test@email.com", "wrong");
        when(memberService.login(request.email(), request.pwd()))
                .thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다."));

        mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

    }

    @DisplayName("존재하지 않는 계정으로 로그인 시 403Forbidden 에러가 발생한다")
    @Test
    void 로그인_실패_계정없음() throws Exception {
        MemberRequest request = new MemberRequest("nonexistent@email.com", "abc1234");

        when(memberService.login(request.email(), request.pwd()))
                .thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "회원이 존재하지 않습니다."));

        mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
