package gift.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.exception.InvalidLoginException;
import gift.user.controller.UserController;
import gift.user.dto.LoginRequestDto;
import gift.user.dto.LoginResponseDto;
import gift.user.dto.RegisterRequestDto;
import gift.user.dto.RegisterResponseDto;
import gift.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserService userService;

  @Test
  void 회원가입_성공() throws Exception {
    //given
    String requestBody = """
           {
          "email" : "admin@email.com",
          "password" : "password"
        }
        """;

    RegisterResponseDto mockResponse = new RegisterResponseDto("");

    when(userService.registerUser(any(RegisterRequestDto.class))).thenReturn(mockResponse);

    mockMvc.perform(post("/api/users/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.token").value(""));
  }

  @Test
  void 로그인_성공() throws Exception {
    //given
    String requestBody = """
        {
        "email": "admin@email.com",
        "password": "password"
        }
        """;

    LoginResponseDto mockResponse = new LoginResponseDto("");

    when(userService.loginUser(any(LoginRequestDto.class))).thenReturn(mockResponse);

    mockMvc.perform(post("/api/users/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value(""));
  }

  @Test
  void 회원가입시_이메일_미기입_예외발생() throws Exception {
    //given
    String requestBody = """
        {
        "email": "",
        "password": "password"
        }
        """;
    mockMvc.perform(post("/api/users/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].field").value("email"));
  }

  @Test
  void 회원가입시_비밀번호_미기입_예외발생() throws Exception {
    //given
    String requestBody = """
        {
        "email": "admin@email.com",
        "password": ""
        }
        """;
    mockMvc.perform(post("/api/users/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].field").value("password"));
  }
  @Test
  void 로그인시_사용자없음_예외발생() throws Exception {
    // given
    String requestBody = """
            {
                "email": "unknown@email.com",
                "password": "password"
            }
            """;

    when(userService.loginUser(any(LoginRequestDto.class))).thenThrow(new InvalidLoginException());

    mockMvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.code").value("INVALID_LOGIN"))
        .andExpect(jsonPath("$.message").value("이메일 또는 비밀번호가 일치하지 않습니다."))
        .andExpect(jsonPath("$.errors").isEmpty());
  }


}

