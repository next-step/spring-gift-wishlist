package gift.member;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.config.TestConfig;
import gift.dto.MemberLoginRequestDto;
import gift.dto.MemberLoginResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberTest extends TestConfig {

  byte[] keyBytes = Base64.getDecoder().decode("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=");
  SecretKey key = Keys.hmacShaKeyFor(keyBytes);

  Long createdMemberId;

  @BeforeEach
  void 초기_회원_등록() throws Exception {
    MemberLoginRequestDto memberLoginRequestdto = new MemberLoginRequestDto("swj010324@naver.com", "password");
    String json = objectMapper.writeValueAsString(memberLoginRequestdto);

    MvcResult result = mockMvc.perform(post("/api/members/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated())
        .andReturn();

    String responseBody = result.getResponse().getContentAsString();
    MemberLoginResponseDto responseDto = objectMapper.readValue(responseBody, MemberLoginResponseDto.class);
    String token = responseDto.token();

    Claims claims = Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();

    createdMemberId = Long.valueOf(claims.getSubject());
  }

  @AfterEach
  void 테스트_종료_모든_기록_삭제() throws Exception {
    if (createdMemberId != null) {
      mockMvc.perform(post("/admin/members/" + createdMemberId + "/delete"));
    }
  }

  @Test
  void 형식에_맞지_않는_이메일로_가입_시도_400() throws Exception {
    MemberLoginRequestDto memberLoginRequestDto = new MemberLoginRequestDto("swj", "password");

    String json = objectMapper.writeValueAsString(memberLoginRequestDto);

    MvcResult result = mockMvc.perform(post("/api/members/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andReturn();
  }

  @Test
  void 이미_가입된_이메일로_가입_시도_409() throws Exception {
    MemberLoginRequestDto memberLoginRequestDto = new MemberLoginRequestDto("swj010324@naver.com", "password");

    String json = objectMapper.writeValueAsString(memberLoginRequestDto);

    MvcResult result = mockMvc.perform(post("/api/members/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isConflict())
        .andReturn();
  }

  @Test
  void 가입된_회원_로그인시_200() throws Exception {
    MemberLoginRequestDto memberLoginRequestDto = new MemberLoginRequestDto("swj010324@naver.com", "password");

    String json = objectMapper.writeValueAsString(memberLoginRequestDto);

    MvcResult result = mockMvc.perform(post("/api/members/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  void 존재하지_않는_회원_아이디_입력한_경우_404() throws Exception {
    MemberLoginRequestDto memberLoginRequestDto = new MemberLoginRequestDto("swj@naver.com", "password");

    String json = objectMapper.writeValueAsString(memberLoginRequestDto);

    MvcResult result = mockMvc.perform(post("/api/members/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isNotFound())
        .andReturn();
  }

  @Test
  void 가입된_회원_아이디_틀린_경우_401() throws Exception {
    MemberLoginRequestDto memberLoginRequestDto = new MemberLoginRequestDto("swj010324@naver.com", "!password");

    String json = objectMapper.writeValueAsString(memberLoginRequestDto);

    MvcResult result = mockMvc.perform(post("/api/members/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isUnauthorized())
        .andReturn();
  }
}

