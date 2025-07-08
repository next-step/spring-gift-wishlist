package gift;

import gift.controller.MemberController;
import gift.exception.DuplicateMemberException;
import gift.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @DisplayName("유효한 이메일과 비밀번호로 회원가입에 성공한다")
    @Test
    void 회원가입_성공() throws Exception{
        var response = mockMvc.perform(
                post("/api/members/register")
                        .contentType("application/json")
                        .content("""
                            {
                                "email": "test@email.com",
                                "password": "abc1234"
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
                        "password": "abc1234"
                     }
                """))
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }
}
